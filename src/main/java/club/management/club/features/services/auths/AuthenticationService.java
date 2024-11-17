package club.management.club.features.services.auths;

import club.management.club.features.dto.requests.AuthenticationRequest;
import club.management.club.features.dto.requests.RegistrationRequest;
import club.management.club.features.dto.responses.AuthenticationResponse;
import club.management.club.features.services.email.EmailService;
import club.management.club.features.services.email.EmailTemplateName;
import club.management.club.features.entities.Authority;
import club.management.club.features.entities.MailToken;
import club.management.club.features.entities.User;
import club.management.club.features.services.users.UserService;
import club.management.club.shared.exceptionHandler.MailDontValidateException;
import club.management.club.shared.exceptions.AccountAlreadyActivated;
import club.management.club.shared.exceptions.AccountNotFoundException;
import club.management.club.shared.exceptions.TokenNotMatchException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthorityService authorityService;
    private final EmailService emailService;
    private final MailTokenService mailTokenService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        Authority authority = authorityService.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .accountLEnabled(false)
                .authorities(Set.of(authority))
                .build();
        userService.saveUser(user);
        sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user =  userService.findUserByEmail(request.getEmail());
        if (!user.isAccountLEnabled()){
            throw new MailDontValidateException();
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Map<String, Object> claims = new HashMap<>();
        String userEmail =  auth.getPrincipal().toString();
        claims.put("fullName", userService.getFullName(user));
        claims.put("accountCompleted",true);
        claims.put("id",user.getId());
        var jwtToken = jwtTokenService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .JWTToken(jwtToken)
                .build();
    }

//    @Transactional
    public void activateAccount(String token) throws MessagingException {
        MailToken savedToken = mailTokenService.findMailTokenByToken(token)
                .orElseThrow(() -> new RuntimeException("No token match the provided one"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userService.findUserById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setAccountLEnabled(true);
        userService.saveUser(user);
        mailTokenService.deleteTokenById(savedToken.getId());
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = MailToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .user(user)
                .build();
        mailTokenService.saveToken(token);
        return generatedToken;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                userService.getFullName(user) ,
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
                );
    }

    private void sendForgetPasswordEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                userService.getFullName(user) ,
                EmailTemplateName.FORGOT_PASSWORD,
                activationUrl,
                newToken,
                "Request to reset password"
                );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public void resendActivation(String email) throws MessagingException {
        if(email.isEmpty()){
            throw new AccountNotFoundException("Email is mandatory");
        }
        User user = userService.findUserByEmail(email);
        if (user.isAccountLEnabled()) {
            throw new AccountAlreadyActivated("Account is already activated");
        }
        sendValidationEmail(user);
    }

    public void forgotPassword(String email) throws MessagingException {
        if(email.isEmpty()){
            throw new AccountNotFoundException("Email is mandatory");
        }
        User user = userService.findUserByEmail(email);
        if (!user.isAccountLEnabled()) {
            throw new MailDontValidateException();
        }
        sendForgetPasswordEmail(user);

    }

    public void resetPassword(String token, String password) {
        MailToken savedToken = mailTokenService.findMailTokenByToken(token)
                .orElseThrow(() -> new TokenNotMatchException("No token match the provided one"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userService.findUserById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        mailTokenService.deleteTokenById(savedToken.getId());
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userService.findUserByEmail(email);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);
    }
}
