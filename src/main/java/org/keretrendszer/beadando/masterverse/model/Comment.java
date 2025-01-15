package org.keretrendszer.beadando.masterverse.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_post_id"),
                referencedColumnName = "id")
    private Posts postId;

    @ManyToOne
    @JoinColumn(name = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_id"),
                referencedColumnName = "id")
    private Users userId;

    @OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentImages> commentImages = new ArrayList<>();

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Comment() {}

    public Comment(String commentContent, Posts postId, Users userId)
    {
        this.commentContent = commentContent;
        this.postId = postId;
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

    public Posts getPostId()
    {
        return postId;
    }

    public void setPostId(Posts postId)
    {
        this.postId = postId;
    }

    public Users getUserId()
    {
        return userId;
    }

    public void setUserId(Users userId)
    {
        this.userId = userId;
    }

    public List<CommentImages> getCommentImages()
    {
        return commentImages;
    }

    public void setCommentImages(List<CommentImages> commentImages)
    {
        this.commentImages = commentImages;
    }

    public String getCommentContent()
    {
        return commentContent;
    }

    public void setCommentContent(String commentContent)
    {
        this.commentContent = commentContent;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
}