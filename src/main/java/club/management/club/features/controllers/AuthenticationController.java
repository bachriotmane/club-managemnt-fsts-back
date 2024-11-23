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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
        @RequestPart("request") @Valid RegistrationRequest request,
        @RequestPart("coverImage") MultipartFile coverImage,
        @RequestPart("profileImage") MultipartFile profileImage
    ) throws MessagingException, IOException {
        authenticationService.register(request , coverImage, profileImage);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
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
