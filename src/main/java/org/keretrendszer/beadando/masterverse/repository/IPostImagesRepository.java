package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.PostImages;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IPostImagesRepository extends JpaRepository<PostImages, Long>
{
    List<PostImages> findAllByPostId_Id(long postId);
}
