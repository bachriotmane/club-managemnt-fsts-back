package club.management.club.shared.dtos;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String email
) {
}
