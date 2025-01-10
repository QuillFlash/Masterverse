package org.keretrendszer.beadando.masterverse.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
              joinColumns = {@JoinColumn(name = "user_id")},
              inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "userId",
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    private List<Posts> userPosts = new ArrayList<>();

    @OneToMany(mappedBy = "userId",
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    private List<Comment> userComments = new ArrayList<>();

    @OneToMany(mappedBy = "followed",
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    private List<FollowFlow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower",
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    private List<FollowFlow> following = new ArrayList<>();

    public Users() {}

    public Users(String username, String email, String password, byte[] profilePicture)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public byte[] getProfilePicture()
    {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture)
    {
        this.profilePicture = profilePicture;
    }

    public List<Posts> getUserPosts()
    {
        return userPosts;
    }

    public void setUserPosts(List<Posts> userPosts)
    {
        this.userPosts = userPosts;
    }

    public List<Comment> getUserComments()
    {
        return userComments;
    }

    public void setUserComments(List<Comment> userComments)
    {
        this.userComments = userComments;
    }

    public List<FollowFlow> getFollowers()
    {
        return followers;
    }

    public void setFollowers(List<FollowFlow> followers)
    {
        this.followers = followers;
    }

    public List<FollowFlow> getFollowing()
    {
        return following;
    }

    public void setFollowing(List<FollowFlow> following)
    {
        this.following = following;
    }

    public Set<Roles> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Roles> roles)
    {
        this.roles = roles;
    }
}