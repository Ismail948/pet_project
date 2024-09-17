package Pet_project.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // Injected dependencies for user details service and access denied handler
    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configure authorization rules for HTTP requests
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // Allow access to these URLs without authentication
                    .requestMatchers("/register", "/login", "/error", "/", "/about", "/contact","/successreg","/explore").permitAll() 
                    // Require ADMIN role to access /admin URL
                    .requestMatchers("/admin").hasRole("ADMIN") 
                    // Allow access to URLs under /images without authentication
                    .requestMatchers("/image/**").permitAll() 
                    // Allow ADMIN or USER roles to access /user URL
                    .requestMatchers("/user").hasAnyRole("ADMIN", "USER") 
                    // Require authentication for all other requests
                    .anyRequest().authenticated() 
            )
            // Configure CSRF protection
            .csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository())) 
            // Configure form-based login
            .formLogin(formLogin ->
                formLogin
                    // Specify the login page URL
                    .loginPage("/login") 
                    .permitAll() // Allow access to the login page without authentication
                    // Redirect to home page on successful login
                    .defaultSuccessUrl("/", true)
                    // Redirect to login page with error parameter on login failure
                    .failureUrl("/login?error=true")
            )
            // Configure logout settings
            .logout(logout ->
                logout
                    // Specify the logout URL
                    .logoutUrl("/logout")
                    // Invalidate HTTP session on logout
                    .invalidateHttpSession(true)
                    // Delete JSESSIONID cookie on logout
                    .deleteCookies("JSESSIONID")
            )
            // Configure exception handling
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    // Use custom access denied handler
                    .accessDeniedHandler(accessDeniedHandler) 
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define the PasswordEncoder bean using BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Set the custom UserDetailsService for retrieving user details
        provider.setUserDetailsService(userDetailsService);
        // Set the PasswordEncoder for password validation
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
