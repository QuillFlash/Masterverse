package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IPostLikesRepository extends JpaRepository<PostLikes, Long>
{
    long countByPostId_Id(long postId);
    boolean existsByPostId_IdAndUserId_Id(long postId, long userId);
    Optional<PostLikes> findByPostId_IdAndUserId_Id(long postId, long userId);
}
