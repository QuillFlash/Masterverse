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
@Table(name = "follow_flow")
public class FollowFlow
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_follower_id"),
                referencedColumnName = "id")
    private Users follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_followed_id"),
                referencedColumnName = "id")
    private Users followed;

    public FollowFlow() {}

    public FollowFlow(Users follower, Users followed)
    {
        this.follower = follower;
        this.followed = followed;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Users getFollower()
    {
        return follower;
    }

    public void setFollower(Users follower)
    {
        this.follower = follower;
    }

    public Users getFollowed()
    {
        return followed;
    }

    public void setFollowed(Users followed)
    {
        this.followed = followed;
    }
}