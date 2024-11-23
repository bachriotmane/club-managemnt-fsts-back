package club.management.club.features.dto.responses;

public record UserRolesInsideClubResponse(
        String clubName,
        String clubId,
        String userId,
        String userRole
) {
}
