package club.management.club.shared.dtos;

public record ResetPasswordRequest(
        String password,
        String token
) {
}
