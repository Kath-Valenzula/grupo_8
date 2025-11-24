package com.frontend.frontend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final CustomAuthenticationProvider authProvider;

    public WebSecurityConfig(CustomAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
        log.info("WebSecurityConfig inicializado con provider {}", authProvider.getClass().getSimpleName());
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customProvider) {
        log.info("Creando AuthenticationManager exclusivo con CustomAuthenticationProvider");
        return new ProviderManager(List.of(customProvider));
    }

    @Bean
    public CustomLoginFilter customLoginFilter(AuthenticationManager authManager) {
        CustomLoginFilter filter = new CustomLoginFilter();
        filter.setAuthenticationManager(authManager);
        filter.setFilterProcessesUrl("/login");
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.sendRedirect("/recetas");
        });
        filter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.sendRedirect("/login?error=true");
        });
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        log.info("Configurando SecurityFilterChain con AuthenticationManager {}", authManager.getClass().getSimpleName());

        http
            .authenticationManager(authManager)
            .authenticationProvider(authProvider)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/home", "/login", "/js/**", "/css/**", "/img/**", "/recetas", "/recetas/**", "/register").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterAt(customLoginFilter(authManager), UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );
        return http.build();
    }

}
