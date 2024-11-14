package club.management.club.shared.security.config;

import club.management.club.features.entities.User;
import club.management.club.features.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getPrincipal().toString();
        String pwd = authentication.getCredentials().toString();
        User user = userService.findUserByEmail(email);
        if (passwordEncoder.matches(pwd, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, null, userService.getGrantedAuthorities(user.getAuthorities()));
        } else {
            throw new BadCredentialsException(" passwords does not matches!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
