package club.management.club.features.dto.responses;


public record ListUsersResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String cin,
        String cne,
        String filiere,
        boolean accountLocked,
        String role

) {
}
