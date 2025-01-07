package org.keretrendszer.beadando.masterverse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment_images")
public class CommentImages
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment commentId;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    public CommentImages() {}

    public CommentImages(Comment commentId, byte[] image)
    {
        this.commentId = commentId;
        this.image = image;
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

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }
}