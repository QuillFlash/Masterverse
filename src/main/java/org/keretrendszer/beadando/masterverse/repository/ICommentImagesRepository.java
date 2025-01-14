package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.CommentImages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ICommentImagesRepository extends JpaRepository<CommentImages, Long>
{
    List<CommentImages> findAllByCommentId_Id(long commentId);
}
