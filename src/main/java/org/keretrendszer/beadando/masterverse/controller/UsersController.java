package org.keretrendszer.beadando.masterverse.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.keretrendszer.beadando.masterverse.db_read_helpers.CommentDataRequestHelper;
import org.keretrendszer.beadando.masterverse.db_read_helpers.FollowTrackingHelper;
import org.keretrendszer.beadando.masterverse.db_read_helpers.PostDataRequestHelper;
import org.keretrendszer.beadando.masterverse.db_read_helpers.ValidationHelper;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.keretrendszer.beadando.masterverse.model.Roles;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;
import org.keretrendszer.beadando.masterverse.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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
    private final FollowTrackingHelper followTrackingHelper;
    private final FollowFlowService followFlowService;

    public UsersController(UsersService usersService, RolesService rolesService,
                           PostsService postsService, PostDataRequestHelper postDataRequestHelper,
                           PasswordEncoder passwordEncoder, CommentsService commentsService,
                           CommentDataRequestHelper commentDataRequestHelper,
                           FollowTrackingHelper followTrackingHelper, FollowFlowService followFlowService)
    {
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.postsService = postsService;
        this.postDataRequestHelper = postDataRequestHelper;
        this.passwordEncoder = passwordEncoder;
        this.commentsService = commentsService;
        this.commentDataRequestHelper = commentDataRequestHelper;
        this.followTrackingHelper = followTrackingHelper;
        this.followFlowService = followFlowService;
    }

    @GetMapping("/login")
    public String showLoginForm()
    {
        return "login";
    }

    @GetMapping("/users")
    public String listUsers(Model model,
                            @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        List<Users> allUsers = usersService.getAllUsers();
        if (currentUser != null)
        {
            long loggedInUser = currentUser.getId();
            model.addAttribute("loggedInUser", loggedInUser);
        }
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
        long followerCount = followFlowService.countFollowers(id);
        model.addAttribute("followerCount", followerCount);
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
            Map<String, Object> followData = followTrackingHelper.processFollowData(id, currentUser);
            model.addAttribute("followerCount", followData.get("followerCount"));
            model.addAttribute("isUserFollowingOthers", followData.get("isUserFollowingOthers"));
            model.addAttribute("currentUser", loggedInUser);
        }
        else
        {
            model.addAttribute("currentUser", null);
            model.addAttribute("followerCount", null);
        }
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

    @GetMapping("/followers/{id}")
    public String showFollowersForm(@PathVariable long id,
                                    Model model,
                                    @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        List<Users> followers = followFlowService.getFollowersFromDatabase(id);
        long loggedInUserId = currentUser.getId();
        model.addAttribute("followers", followers);
        model.addAttribute("loggedInUserId", loggedInUserId);
        return "followers";
    }

    @GetMapping("/followed_users/{id}")
    public String showFollowedUsersForm(@PathVariable long id,
                                        Model model,
                                        @AuthenticationPrincipal MasterverseUserDetails currentUser)
    {
        List<Users> following = followFlowService.getFollowingFromDatabase(id);
        long loggedInUserId = currentUser.getId();
        model.addAttribute("following", following);
        model.addAttribute("loggedInUserId", loggedInUserId);
        return "followed_users";
    }

    @GetMapping("/modify_user/{user_id}")
    public String showModifyUserForm(@PathVariable("user_id") long userId, Model model)
    {
        Users user = usersService.getUserById(userId);
        if (user == null) return "redirect:/error/404";
        model.addAttribute("modifiedUser", user);
        return "modify_user";
    }

    @PostMapping({"first_setup", "/register", "/register_admin"})
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

    @PutMapping("/modify_user/{user_id}")
    public String modifyUser(@ModelAttribute Users user,
                             @PathVariable("user_id") long userId,
                             @RequestParam(name = "uploadedProfilePicture", required = false)
                             List<MultipartFile> uploadedProfilePicture,
                             @AuthenticationPrincipal MasterverseUserDetails currentUser)
    throws IOException
    {
        if (currentUser == null) return "redirect:/error/404";
        Users existingUser = usersService.getUserById(userId);
        if (user.getUsername() != null && !user.getUsername().isEmpty())
            existingUser.setUsername(user.getUsername());
        if (user.getEmail() != null && !user.getEmail().isEmpty())
            existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty())
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if (uploadedProfilePicture != null && !uploadedProfilePicture.isEmpty())
        {
            for (MultipartFile profilePicture : uploadedProfilePicture)
                existingUser.setProfilePicture(profilePicture.getBytes());
        }
        usersService.saveUser(existingUser);
        return "redirect:/modify_user/{user_id}";
    }

    @DeleteMapping("/delete_user_account/{user_id}")
    public String deleteAccount(@PathVariable("user_id") long userId,
                                @AuthenticationPrincipal MasterverseUserDetails currentUser,
                                HttpServletRequest request)
    {
        if (currentUser == null) return "redirect:/error/404";
        Users userToDelete = usersService.getUserById(userId);
        ValidationHelper validationHelper = new ValidationHelper();
        validationHelper.validateUserExistence(userToDelete);
        usersService.deleteUserAccount(userToDelete);
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
