package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.keretrendszer.beadando.masterverse.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class UsersController
{
    private final UsersService usersService;
    private final PostsService postsService;

    public UsersController(UsersService usersService, PostsService postsService)
    {
        this.usersService = usersService;
        this.postsService = postsService;
    }

    @GetMapping("/users")
    public String listUsers(Model model)
    {
        List<Users> allUsers = usersService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String viewUserProfile(@PathVariable long id, Model model)
    {
        Users user = usersService.getUserById(id);
        Posts userPosts = postsService.getPostsByUserId(id);
        PostImages userPostAttachments = postsService.getAttachmentsByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        model.addAttribute("postAttachments", userPostAttachments);
        return "profile";
    }

}
