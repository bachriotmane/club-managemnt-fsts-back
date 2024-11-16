package club.management.club.features.controllers;

import club.management.club.features.services.auths.AuthenticationService;
import club.management.club.features.dto.requests.RegistrationRequest;
import club.management.club.features.dto.requests.AuthenticationRequest;
import club.management.club.features.dto.responses.AuthenticationResponse;
import club.management.club.shared.dtos.ChangePasswordRequest;
import club.management.club.shared.dtos.ResetPasswordRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        authenticationService.activateAccount(token);
    }

    @GetMapping("/resend-activation")
    public void resendActivation(
            @RequestParam String email
    ) throws MessagingException {
        authenticationService.resendActivation(email);
    }

    @GetMapping("/forgot-password")
    public void forgotPassword(
            @RequestParam String email
    ) throws MessagingException {
        authenticationService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public void resetPassword(
            @RequestBody ResetPasswordRequest request
            ) {
        authenticationService.resetPassword(request.token(), request.password());
    }

    @PostMapping("/change-password")
    public void changePassword(
            @RequestBody ChangePasswordRequest request
            ) {
        authenticationService.changePassword(request.email(), request.oldPassword(), request.newPassword());
    }
}
