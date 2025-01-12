package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ICommentLikesRepository extends JpaRepository<CommentLikes, Long>
{
    long countByCommentId_Id(long commentId);
    boolean existsByCommentId_IdAndUserId_Id(long commentId, long userId);
    Optional<CommentLikes> findByCommentId_IdAndUserId_Id(long commentId, long userId);
}
