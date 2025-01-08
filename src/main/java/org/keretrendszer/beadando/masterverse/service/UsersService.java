package org.keretrendszer.beadando.masterverse.service;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsersService
{
    private final IUsersRepository iUsersRepository;

    public UsersService(IUsersRepository iUsersRepository)
    {
        this.iUsersRepository = iUsersRepository;
    }

    public List<Users> getAllUsers()
    {
        return iUsersRepository.findAll();
    }

    public Users getUserById(long id)
    {
        return iUsersRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No users found"));
    }
}
