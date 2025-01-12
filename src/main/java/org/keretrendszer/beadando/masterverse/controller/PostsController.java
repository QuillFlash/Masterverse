package org.keretrendszer.beadando.masterverse.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostsController
{
    private final PostsService postsService;

    public PostsController(PostsService postsService)
    {
        this.postsService = postsService;
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
            else hasUserLikedAPost.put(postId, false);
        }
        model.addAttribute("posts", allPosts);
        model.addAttribute("postImages", allPostImages);
        model.addAttribute("likesForAllPosts", likesForAllPosts);
        model.addAttribute("hasUserLikedAPost", hasUserLikedAPost);
        return "index";
    }
}