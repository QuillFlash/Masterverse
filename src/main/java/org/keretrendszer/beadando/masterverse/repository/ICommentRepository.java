package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> findAllByPostId_Id(long postId);
}
