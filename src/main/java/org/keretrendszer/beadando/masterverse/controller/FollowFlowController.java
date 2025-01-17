package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.FollowFlowService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class FollowFlowController
{
    private final FollowFlowService followFlowService;

    public FollowFlowController(FollowFlowService followFlowService)
    {
        this.followFlowService = followFlowService;
    }

    @PostMapping("/{id}/follow")
    public String followProfile(@PathVariable long id,
                                @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long userId = currentUser.getId();
        followFlowService.followUser(userId, id);
        return "redirect:/users/{id}/profile";
    }

    @PostMapping("/{id}/unfollow")
    public String unfollowProfile(@PathVariable long id,
                                  @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        long userId = currentUser.getId();
        followFlowService.removeFollowFromUser(userId, id);
        return "redirect:/users/{id}/profile";
    }
}
