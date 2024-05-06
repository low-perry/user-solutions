package example.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

   @Bean 
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request ->
            request
                    .requestMatchers("/users/**")
                    .hasRole("USER-OWNER")
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
       return http.build();
   }

   @Bean
   PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }

   @Bean
  UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
    org.springframework.security.core.userdetails.User.UserBuilder users = org.springframework.security.core.userdetails.User.builder();
   UserDetails admin = users
     .username("admin")
     .password(passwordEncoder.encode("abc123"))
     .roles("USER-OWNER") // No roles for now
     .build();
    
    UserDetails darioOwnsNoUsers = users
      .username("dario")
      .password(passwordEncoder.encode("abc123"))
      .roles("NON-OWNER") // No roles for now
      .build();
   return new InMemoryUserDetailsManager(admin, darioOwnsNoUsers);
  }
}
