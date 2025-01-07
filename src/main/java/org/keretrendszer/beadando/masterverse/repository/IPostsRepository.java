package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostsRepository extends JpaRepository<Posts, Long> {}
