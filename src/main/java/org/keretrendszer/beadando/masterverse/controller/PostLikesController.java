package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostLikesController
{
    private final PostsService postsService;

    public PostLikesController(PostsService postsService)
    {
        this.postsService = postsService;
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable long postId,
                           @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long userId = currentUser.getId();
        postsService.likePost(postId, userId);
        return "redirect:/";
    }

    @PostMapping("/{postId}/remove_like")
    public String removeLikeFromPost(@PathVariable long postId,
                                     @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long userId = currentUser.getId();
        postsService.removeLikeFromPost(postId, userId);
        return "redirect:/";
    }
}
