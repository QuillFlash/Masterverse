package org.keretrendszer.beadando.masterverse.repository;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsersRepository extends JpaRepository<Users, Long>
{
    Users findByUsername(String username);
}
