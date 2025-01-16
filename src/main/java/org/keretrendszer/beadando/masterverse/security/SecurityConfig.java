package org.keretrendszer.beadando.masterverse.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                                "/users/**", "/users/{id}/profile", "/profile",
                                "/login", "/register", "/first_setup").permitAll()  // Ezek az oldalak mindenkinek elérhetők
                        .requestMatchers(HttpMethod.DELETE, "/delete_user_account/**").authenticated()
                        .anyRequest().authenticated()  // Az összes többi hitelesítést igényel
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Sikeres bejelentkezés után főoldal
                        .permitAll() // Bejelentkezési oldal mindenki számára elérhető
                ) // bejelentkezési oldal
                .logout(logout -> logout
                        .logoutUrl("/logout") // Kijelentkezés végpontja
                        .logoutSuccessUrl("/") // Kijelentkezés után főoldal
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(); // BCrypt hashelési algoritmus használata jelszavakhoz
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration)
    throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
