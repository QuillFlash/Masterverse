package org.keretrendszer.beadando.masterverse.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
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

    public PostsController(PostsService postsService, UsersService usersService)
    {
        this.postsService = postsService;
        this.usersService = usersService;
    }

    @GetMapping({"/"})
    public String getAllPosts(Model model,
                              @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        List<Posts> allPosts = postsService.getAllPosts();
        List<PostImages> allPostImages = postsService.getAllPostImages();
        Map<Long, Long> likesForAllPosts = new HashMap<>();
        Map<Long, Boolean> hasUserLikedAPost = new HashMap<>();
        for (Posts post : allPosts)
        {
            long postId = post.getId();
            long likeCount = postsService.countPostLikes(postId);
            likesForAllPosts.put(postId, likeCount);
            if (currentUser != null)
            {
                long userId = currentUser.getId();
                boolean isPostLiked = postsService.hasUserLikedAPost(postId, userId);
                hasUserLikedAPost.put(postId, isPostLiked);
            }
            else
            {
                hasUserLikedAPost.put(postId, false);
            }
        }
        model.addAttribute("posts", allPosts);
        model.addAttribute("postImages", allPostImages);
        model.addAttribute("likesForAllPosts", likesForAllPosts);
        model.addAttribute("hasUserLikedAPost", hasUserLikedAPost);
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
        if (loggedInUser == null)
        {
            throw new IllegalArgumentException("ERROR: This user does not exist in the database.");
        }
        if (existingPost == null)
        {
            throw new IllegalArgumentException("ERROR: No post found with post ID " + postId + ".");
        }
        if (!existingPost.getUserId().equals(loggedInUser))
        {
            throw new RuntimeException("You don't have permission to modify this post!");
        }
        String postNewContent = Optional.ofNullable(post.getPostContent()).orElse("");
        existingPost.setPostContent(postNewContent);
        existingPost.setUserId(existingPost.getUserId());
        postsService.savePost(existingPost);
        return "redirect:/";
    }
}