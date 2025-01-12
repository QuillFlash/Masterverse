package org.keretrendszer.beadando.masterverse.model;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment_likes")
public class CommentLikes
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_comment_likes_comment"))
    private Comment commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_comment_likes_user"))
    private Users userId;

    public CommentLikes() {}

    public CommentLikes(Comment commentId, Users userId)
    {
        this.commentId = commentId;
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

    public Comment getCommentId()
    {
        return commentId;
    }

    public void setCommentId(Comment commentId)
    {
        this.commentId = commentId;
    }

    public Users getUserId()
    {
        return userId;
    }

    public void setUserId(Users userId)
    {
        this.userId = userId;
    }
}