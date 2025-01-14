package org.keretrendszer.beadando.masterverse.controller;
import org.keretrendszer.beadando.masterverse.db_read_helpers.PostDataRequestHelper;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Roles;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.PostsService;
import org.keretrendszer.beadando.masterverse.service.RolesService;
import org.keretrendszer.beadando.masterverse.service.UsersService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    public UsersController(UsersService usersService, RolesService rolesService, PostsService postsService,
                           PostDataRequestHelper postDataRequestHelper,
                           PasswordEncoder passwordEncoder)
    {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.postsService = postsService;
        this.postDataRequestHelper = postDataRequestHelper;
        this.passwordEncoder = passwordEncoder;
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
        if (user == null)
        {
            return "redirect:/error/404";
        }
        List<Posts> userPosts = postsService.getPostsById(user.getId());
        Map<String, Object> processedData = postDataRequestHelper.processPostsData(userPosts, currentUser);
        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        model.addAttribute("postImages", processedData.get("postImages"));
        model.addAttribute("likesForUsersPosts", processedData.get("likesForPosts"));
        model.addAttribute("hasUserLikedAPost", processedData.get("hasUserLikedAPost"));
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

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Roles userRole = rolesService.getRoleById(2); // id: 2 = "user" szerepkör; szándékos a kódba égetés
        user.getRoles().add(userRole);
        usersService.saveUser(user);
        return "redirect:/login";
    }
}
