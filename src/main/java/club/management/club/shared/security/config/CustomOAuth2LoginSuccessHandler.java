package club.management.club.shared.security.config;

import club.management.club.features.entities.Authority;
import club.management.club.features.entities.User;
import club.management.club.features.services.auths.AuthorityService;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final AuthorityService authorityService;
    private final JwtTokenService jwtTokenService;
    @Value("${url.frontend}")
    private String frontendUrl;
    private final String JWT_SECRET = "your_jwt_secret_key"; // Ideally store securely in env variables

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        var oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            Authority authority = authorityService.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
            User newUser = User.builder()
                    .email(email)
                    .firstName(oAuth2User.getAttribute("given_name"))
                    .lastName(oAuth2User.getAttribute("family_name"))
                    .accountCompleted(false)
                    .authorities(Set.of(authority))
                    .build();
            user = Optional.ofNullable(userService.saveUser(newUser));
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", userService.getFullName(user.get()));
        claims.put("accountCompleted",user.get().isAccountCompleted());
        var jwtToken = jwtTokenService.generateToken(claims, user.get());
        // Send token in response
        response.setHeader("Authorization", "Bearer " + jwtToken);
        response.sendRedirect(frontendUrl+"/home");
    }
}
