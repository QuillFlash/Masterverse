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
@Table(name = "post_images")
public class PostImages
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts postId;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    public PostImages() {}

    public PostImages(Posts postId, byte[] image)
    {
        this.postId = postId;
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

    public Posts getPostId()
    {
        return postId;
    }

    public void setPostId(Posts postId)
    {
        this.postId = postId;
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