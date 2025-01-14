package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IPostsRepository extends JpaRepository<Posts, Long>
{
    List<Posts> findAllByUserId_Id(long userId);
}
