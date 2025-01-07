package org.keretrendszer.beadando.masterverse.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

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

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}