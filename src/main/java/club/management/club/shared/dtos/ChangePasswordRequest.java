package club.management.club.shared.dtos;

public record ChangePasswordRequest(
        String oldPassword,
        //add validate to check the new Password
        String newPassword,
        String email
) {
}
