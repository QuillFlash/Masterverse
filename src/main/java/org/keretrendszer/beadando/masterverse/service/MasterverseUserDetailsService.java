package org.keretrendszer.beadando.masterverse.service;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class MasterverseUserDetailsService implements UserDetailsService
{
    private final IUsersRepository iUsersRepository;

    public MasterverseUserDetailsService(IUsersRepository iUsersRepository)
    {
        this.iUsersRepository = iUsersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Users user = iUsersRepository.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        var authorities = user.getRoles().stream()
                              .map(role -> new SimpleGrantedAuthority(role.getName()))
                              .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}