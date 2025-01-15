package org.keretrendszer.beadando.masterverse.controller;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.keretrendszer.beadando.masterverse.db_read_helpers.CommentDataRequestHelper;
import org.keretrendszer.beadando.masterverse.db_read_helpers.PostDataRequestHelper;
import org.keretrendszer.beadando.masterverse.db_read_helpers.ValidationHelper;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.CommentsService;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.keretrendszer.beadando.masterverse.service.UsersService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostsController
{
    private final PostsService postsService;
    private final UsersService usersService;
    private final PostDataRequestHelper postDataRequestHelper;
    private final CommentsService commentsService;
    private final CommentDataRequestHelper commentDataRequestHelper;

    public PostsController(PostsService postsService, UsersService usersService, PostDataRequestHelper postDataRequestHelper,
                           CommentsService commentsService, CommentDataRequestHelper commentDataRequestHelper)
    {
        this.postsService = postsService;
        this.usersService = usersService;
        this.postDataRequestHelper = postDataRequestHelper;
        this.commentsService = commentsService;
        this.commentDataRequestHelper = commentDataRequestHelper;
    }

    @GetMapping({"/"})
    public String getAllPostsAndComments(Model model,
                                         @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        List<Posts> allPosts = postsService.getAllPosts();
        List<Comment> allComments = commentsService.getAllComments();
        Map<String, Object> processedPostData = postDataRequestHelper.processPostsData(allPosts, currentUser);
        Map<String, Object> processedCommentData = commentDataRequestHelper.processCommentsData(allComments, currentUser);
        model.addAttribute("posts", allPosts);
        model.addAttribute("postImages", processedPostData.get("postImages"));
        model.addAttribute("likesForAllPosts", processedPostData.get("likesForPosts"));
        model.addAttribute("hasUserLikedAPost", processedPostData.get("hasUserLikedAPost"));
        model.addAttribute("comments", allComments);
        model.addAttribute("commentImages", processedCommentData.get("commentImages"));
        model.addAttribute("likesForComments", processedCommentData.get("likesForComments"));
        model.addAttribute("hasUserLikedAComment", processedCommentData.get("hasUserLikedAComment"));
        return "index";
    }

    @GetMapping("/create_post")
    public String showCreatePostForm(Model model)
    {
        model.addAttribute("post", new Posts());
        return "create_post";
    }

    @GetMapping("/modify_post/{post_id}")
    public String modifyPost(@PathVariable("post_id") long postId, Model model)
    {
        Posts existingPost = postsService.getAPostById(postId);
        if (existingPost == null) return "redirect:/error";
        model.addAttribute("modifiedPost", existingPost);
        return "modify_post";
    }

    @PostMapping("/create_post")
    public String createPost(@ModelAttribute("post") Posts post,
                             @RequestParam(name = "post_images", required = false)
                             List<MultipartFile> postImages,
                             @AuthenticationPrincipal MasterverseUserDetails currentUser)
    throws IOException
    {
        if (currentUser == null)
        {
            throw new RuntimeException("ERROR: You must be logged in to create a post.");
        }
        long userId = currentUser.getId();
        Users loggedInUser = usersService.getUserById(userId);
        String postContent = Optional.ofNullable(post.getPostContent()).orElse("");
        post.setPostContent(postContent.trim());
        post.setUserId(loggedInUser);
        if (postImages != null)
        {
            for (MultipartFile postImage : postImages)
            {
                if (!postImage.isEmpty())
                {
                    PostImages databaseSlotForImage = new PostImages();
                    databaseSlotForImage.setPostId(post);
                    databaseSlotForImage.setImage(postImage.getBytes());
                    post.getPostImages().add(databaseSlotForImage);
                }
            }
        }
        postsService.savePost(post);
        return "redirect:/";
    }

    @PutMapping("/modify_post/{post_id}")
    public String modifyPost(@PathVariable("post_id") long postId,
                             @ModelAttribute("modifiedPost") Posts post,
                             @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        if (currentUser == null)
        {
            throw new RuntimeException("ERROR: You must be logged in to create a post.");
        }
        long userId = currentUser.getId();
        Users loggedInUser = usersService.getUserById(userId);
        Posts existingPost = postsService.getAPostById(postId);
        ValidationHelper validationHelper = new ValidationHelper();
        validationHelper.validatePostExistence(existingPost, postId);
        validationHelper.validateUserExistence(loggedInUser);
        validationHelper.validatePermissions(loggedInUser, existingPost.getUserId(), "post");
        String postNewContent = Optional.ofNullable(post.getPostContent()).orElse("");
        existingPost.setPostContent(postNewContent);
        existingPost.setUserId(existingPost.getUserId());
        postsService.savePost(existingPost);
        return "redirect:/";
    }
}