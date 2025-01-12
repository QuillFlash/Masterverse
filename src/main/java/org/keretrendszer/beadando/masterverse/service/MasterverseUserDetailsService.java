package org.keretrendszer.beadando.masterverse.service;
import org.keretrendszer.beadando.masterverse.model.Users;
import org.keretrendszer.beadando.masterverse.repository.IUsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.keretrendszer.beadando.masterverse.security.MasterverseUserDetails;

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

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                                                       .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                       .collect(Collectors.toList());

        return new MasterverseUserDetails(user.getId(),
                user.getUsername(), user.getEmail(),
                user.getPassword(), authorities);
    }
}