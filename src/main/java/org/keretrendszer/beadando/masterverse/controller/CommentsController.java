package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.CommentImages;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.CommentsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentsController
{
    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService)
    {
        this.commentsService = commentsService;
    }

    @GetMapping({"/index"})
    public String getAllComments(Model model,
                                 @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        List<Comment> allComments = commentsService.getAllComments();
        List<CommentImages> allCommentImages = commentsService.getAllCommentImages();
        Map<Long, Long> likesForAllComments = new HashMap<>();
        Map<Long, Boolean> hasUserLikedAComment = new HashMap<>();
        for (Comment comment : allComments)
        {
            long commentId = comment.getId();
            long likeCount = commentsService.countCommentLikes(commentId);
            likesForAllComments.put(commentId, likeCount);
            if (currentUser != null)
            {
                long userId = currentUser.getId();
                boolean isCommentLiked = commentsService.hasUserLikedAComment(commentId, userId);
                hasUserLikedAComment.put(commentId, isCommentLiked);
            }
            else hasUserLikedAComment.put(commentId, false);
        }
        model.addAttribute("comments", allComments);
        model.addAttribute("commentImages", allCommentImages);
        model.addAttribute("likesForAllComments", likesForAllComments);
        model.addAttribute("hasUserLikedAComment", hasUserLikedAComment);
        return "index";
    }
}
