package org.keretrendszer.beadando.masterverse.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/images/**", "/users",
                                "users/**", "/profile", "/home", "/about").permitAll()  // Ezek az oldalak elérhetők
                        .anyRequest().denyAll()  // Az összes többi letiltva
                )
                .formLogin(AbstractHttpConfigurer::disable)  // A bejelentkező oldal teljes tiltása
                .logout(AbstractHttpConfigurer::disable);    // A kijelentkezési endpoint tiltása is

        return http.build();
    }
}
