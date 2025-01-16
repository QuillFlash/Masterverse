package org.keretrendszer.beadando.masterverse.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.keretrendszer.beadando.masterverse.db_read_helpers.CommentDataRequestHelper;
import org.keretrendszer.beadando.masterverse.db_read_helpers.PostDataRequestHelper;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Roles;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.CommentsService;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.keretrendszer.beadando.masterverse.service.RolesService;
import org.keretrendszer.beadando.masterverse.service.UsersService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
public class UsersController
{
    private final UsersService usersService;
    private final RolesService rolesService;
    private final PostsService postsService;
    private final PostDataRequestHelper postDataRequestHelper;
    private final PasswordEncoder passwordEncoder;
    private final CommentsService commentsService;
    private final CommentDataRequestHelper commentDataRequestHelper;

    public UsersController(UsersService usersService,
                           RolesService rolesService,
                           PostsService postsService,
                           PostDataRequestHelper postDataRequestHelper,
                           PasswordEncoder passwordEncoder,
                           CommentsService commentsService,
                           CommentDataRequestHelper commentDataRequestHelper)
    {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.postsService = postsService;
        this.postDataRequestHelper = postDataRequestHelper;
        this.passwordEncoder = passwordEncoder;
        this.commentsService = commentsService;
        this.commentDataRequestHelper = commentDataRequestHelper;
    }

    @GetMapping("/login")
    public String showLoginForm()
    {
        return "login";
    }

    @GetMapping("/users")
    public String listUsers(Model model)
    {
        List<Users> allUsers = usersService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("/users/{id}/profile")
    public String viewUserProfile(@PathVariable long id,
                                  Model model,
                                  @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        Users user = usersService.getUserById(id);
        if (user == null) return "redirect:/error/404";
        List<Posts> userPosts = postsService.getPostsById(user.getId());
        List<Comment> comments = commentsService.getAllComments();
        Map<String, Object> processedPostData = postDataRequestHelper.processPostsData(userPosts, currentUser);
        Map<String, Object> processedCommentData = commentDataRequestHelper.processCommentsData(comments, currentUser);
        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        model.addAttribute("postImages", processedPostData.get("postImages"));
        model.addAttribute("likesForUsersPosts", processedPostData.get("likesForPosts"));
        model.addAttribute("hasUserLikedAPost", processedPostData.get("hasUserLikedAPost"));
        model.addAttribute("comments", comments);
        model.addAttribute("commentImages", processedCommentData.get("commentImages"));
        model.addAttribute("likesForComments", processedCommentData.get("likesForComments"));
        model.addAttribute("hasUserLikedAComment", processedCommentData.get("hasUserLikedAComment"));
        if (currentUser != null)
        {
            Users loggedInUser = usersService.getUserByUsername(currentUser.getUsername());
            model.addAttribute("currentUser", loggedInUser);
        }
        else model.addAttribute("currentUser", null);
        return "profile";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("user", new Users());
        return "register";
    }

    @GetMapping("/first_setup")
    public String showFirstSetupRegistrationForm(Model model)
    {
        model.addAttribute("user", new Users());
        long userCount = usersService.getAllUsers().size();
        if (userCount == 0) return "first_setup";
        return "redirect:/";
    }

    @GetMapping("/register_admin")
    public String showAdminRegistrationForm(Model model)
    {
        model.addAttribute("user", new Users());
        return "register_admin";
    }

    @PostMapping({"first_setup", "/register", "register_admin"})
    public String registerUser(@ModelAttribute Users user, @RequestParam String role)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Roles assignedRole;
        if ("admin".equalsIgnoreCase(role)) assignedRole = rolesService.getRoleById(1); // admin szerepkör
        else assignedRole = rolesService.getRoleById(2); // user szerepkör
        user.getRoles().add(assignedRole);
        usersService.saveUser(user);
        return "redirect:/";
    }
}
