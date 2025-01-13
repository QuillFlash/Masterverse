package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.CommentsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentLikesController
{
    private final CommentsService commentsService;

    public CommentLikesController(CommentsService commentsService)
    {
        this.commentsService = commentsService;
    }

    @PostMapping("/{commentId}/like")
    public String likePost(@PathVariable long commentId,
                           @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long userId = currentUser.getId();
        commentsService.likeComment(commentId, userId);
        return "redirect:/";
    }

    @PostMapping("/{commentId}/remove_like")
    public String removeLikeFromPost(@PathVariable long commentId,
                                     @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long userId = currentUser.getId();
        commentsService.removeLikeFromComment(commentId, userId);
        return "redirect:/";
    }
}
