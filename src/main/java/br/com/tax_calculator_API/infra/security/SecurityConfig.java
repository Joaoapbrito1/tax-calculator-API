package br.com.tax_calculator_API.infra.security;

import br.com.tax_calculator_API.infra.jwt.JwtAuthenticationEntryPoint;
import br.com.tax_calculator_API.infra.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/api/user/login").permitAll();
                    authorize.requestMatchers( "/api/user/register").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/api/tipos").authenticated();
                    authorize.requestMatchers(HttpMethod.GET, "/api/tipos/{id}").authenticated();
                    authorize.requestMatchers(HttpMethod.POST, "/api/tipos").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.PUT, "/api/tipos").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.POST, "/api/tipos/calculo").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE, "/api/tipos/{id}").hasRole("ADMIN");
                    authorize.requestMatchers( "/v3/api-docs/**","swagger-ui/**","swagger-ui.html").permitAll();
                    authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());

        http.exceptionHandling( exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}