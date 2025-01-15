package org.keretrendszer.beadando.masterverse.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Posts
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "post_content", nullable = false)
    private String postContent;

    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_post_user"))
    private Users userId;

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImages> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Posts() {}

    public Posts(String postContent, Users userId)
    {
        this.postContent = postContent;
        this.userId = userId;
    }

    public Posts(String postContent, List<PostImages> postImages, Users userId)
    {
        this.postContent = postContent;
        this.postImages = postImages;
        this.userId = userId;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getPostContent()
    {
        return postContent;
    }

    public void setPostContent(String postContent)
    {
        this.postContent = postContent;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public Users getUserId()
    {
        return userId;
    }

    public void setUserId(Users userId)
    {
        this.userId = userId;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

    public List<PostImages> getPostImages()
    {
        return postImages;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;

        Posts posts = (Posts) o;
        return getUserId().equals(posts.getUserId());
    }

    @Override
    public int hashCode()
    {
        return getUserId().hashCode();
    }
}