package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.db_read_helpers.ValidationHelper;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.CommentImages;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentsController
{
    private final CommentsService commentsService;
    private final UsersService usersService;
    private final PostsService postsService;

    public CommentsController(CommentsService commentsService,
                              UsersService usersService,
                              PostsService postsService)
    {
        this.commentsService = commentsService;
        this.usersService = usersService;
        this.postsService = postsService;
    }

    @GetMapping("/posts/{post_id}/create_comment")
    public String showCreateCommentForm(@PathVariable("post_id") long postId,
                                        Model model)
    {
        Posts post = postsService.getAPostById(postId);
        if (post == null)
        {
            throw new RuntimeException("ERROR: Post not found.");
        }
        model.addAttribute("comment", new Comment());
        model.addAttribute("post", post);
        return "create_comment";
    }

    @GetMapping("/modify_comment/{comment_id}")
    public String modifyComment(@PathVariable("comment_id") long commentId, Model model)
    {
        Comment existingComment = commentsService.getACommentById(commentId);
        if (existingComment == null) return "redirect:/error";
        model.addAttribute("modifiedComment", existingComment);
        return "modify_comment";
    }

    @PostMapping("/posts/{post_id}/create_comment")
    public String createComment(@PathVariable("post_id") long postId,
                                @ModelAttribute("comment") Comment comment,
                                @RequestParam(name = "uploadedCommentImages", required = false)
                                List<MultipartFile> uploadedCommentImages,
                                @AuthenticationPrincipal MasterverseUserDetails currentUser)
    throws IOException
    {
        if (currentUser == null)
        {
            throw new RuntimeException("ERROR: You must be logged in to create a comment.");
        }
        Posts post = postsService.getAPostById(postId);
        long userId = currentUser.getId();
        Users loggedInUser = usersService.getUserById(userId);
        String commentContent = Optional.ofNullable(comment.getCommentContent()).orElse("");
        comment.setCommentContent(commentContent.trim());
        comment.setPostId(post);
        comment.setUserId(loggedInUser);
        if (uploadedCommentImages != null)
        {
            for (MultipartFile commentImage : uploadedCommentImages)
            {
                if (! commentImage.isEmpty())
                {
                    CommentImages databaseSlotForImage = new CommentImages();
                    databaseSlotForImage.setCommentId(comment);
                    databaseSlotForImage.setImage(commentImage.getBytes());
                    comment.getCommentImages().add(databaseSlotForImage);
                }
            }
        }
        commentsService.saveComment(comment);
        return "redirect:/";
    }

    @PutMapping("/modify_comment/{comment_id}")
    public String modifyComment(@PathVariable("comment_id") long commentId,
                                @ModelAttribute("modifiedPost") Comment comment,
                                @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        if (currentUser == null)
        {
            throw new RuntimeException("ERROR: You must be logged in to create a post.");
        }
        long userId = currentUser.getId();
        Users loggedInUser = usersService.getUserById(userId);
        Comment existingComment = commentsService.getACommentById(commentId);
        ValidationHelper validationHelper = new ValidationHelper();
        validationHelper.validateCommentExistence(existingComment, commentId);
        validationHelper.validateUserExistence(loggedInUser);
        validationHelper.validatePermissions(loggedInUser, existingComment.getUserId(), "comment");
        String postNewContent = Optional.ofNullable(comment.getCommentContent()).orElse("");
        existingComment.setCommentContent(postNewContent);
        existingComment.setUserId(existingComment.getUserId());
        commentsService.saveComment(existingComment);
        return "redirect:/";
    }
}
