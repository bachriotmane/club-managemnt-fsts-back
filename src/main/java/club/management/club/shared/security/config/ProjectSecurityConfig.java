package club.management.club.shared.security.config;

import club.management.club.shared.security.filters.JWTTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ProjectSecurityConfig {
    private final JWTTokenValidatorFilter jwtTokenValidatorFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

//            http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((requests) -> requests
//                        .anyRequest().permitAll()
//                )
//                 // enable the default form login behavior provided by Spring Security.
//                .formLogin(Customizer.withDefaults())
//                 //enable the default HTTP Basic authentication configuration.
//                 // Clients need to send an Authorization header with a base64-encoded username and password.
//                .httpBasic(Customizer.withDefaults());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    // specifies which HTTP headers the client is allowed to include in the request
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    // refers to the response headers that the client (browser) is allowed to access.
                    config.setExposedHeaders(Arrays.asList("Authorization"));
                    // his specifies how long the results of a pre-flight request
                    // (OPTIONS request) can be cached by the client (browser).
                    config.setMaxAge(3600L);
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenValidatorFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/test/get").hasRole("USER")
                        .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/test/post").hasRole("ADMIN")
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/test/get")
                        .authenticated()
                        .requestMatchers("/notices","/contact","/auth/**", "/login/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/**").permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/login-success",true)
                        .failureUrl("/login-failure")
                        .successHandler(customOAuth2LoginSuccessHandler)
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                )
                .addFilterBefore(jwtTokenValidatorFilter, OAuth2LoginAuthenticationFilter.class);;

        return http.build();
    }


}
