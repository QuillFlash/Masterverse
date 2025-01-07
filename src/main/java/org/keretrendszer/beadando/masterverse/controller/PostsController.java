package org.keretrendszer.beadando.masterverse.controller;
import java.util.List;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.service.PostsService;
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
    public String getAllPosts(Model model)
    {
        List<Posts> allPosts = this.postsService.getAllPosts();
        List<PostImages> allPostImages = this.postsService.getAllPostImages();
        model.addAttribute("posts", allPosts);
        model.addAttribute("postImages", allPostImages);
        return "index";
    }
}