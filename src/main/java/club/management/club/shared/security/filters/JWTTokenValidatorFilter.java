package club.management.club.shared.security.filters;

import club.management.club.features.entities.User;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.users.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtService;
    private final UserService userService;
    private final HandlerExceptionResolver exceptionResolver;
    @Autowired
    public JWTTokenValidatorFilter(JwtTokenService jwtService, UserService userService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver){
        this.jwtService = jwtService;
        this.userService = userService;
        this.exceptionResolver =handlerExceptionResolver;
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader(AUTHORIZATION);
            final String jwt;
            final String userEmail;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User validateUser = userService.findUserByEmail(userEmail);
                var claims = jwtService.parseJwtClaims(jwt);
                Map<String, String> credentials = new HashMap<>();
                credentials.put("id", claims.get("id", String.class));
                    if (jwtService.isTokenValid(jwt, validateUser)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userEmail,
                                credentials,
                                userService.getGrantedAuthorities(validateUser.getAuthorities())
                        );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }


            }
        filterChain.doFilter(request, response);
        }catch (Exception exception){
            exceptionResolver.resolveException(request, response, null, exception);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/auth");
    }
}
