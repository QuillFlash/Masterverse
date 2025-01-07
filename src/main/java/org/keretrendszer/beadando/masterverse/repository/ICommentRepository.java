package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommentRepository extends JpaRepository<Comment, Long> {}
