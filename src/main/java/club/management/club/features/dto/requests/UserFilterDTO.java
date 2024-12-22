package club.management.club.features.dto.requests;

public record UserFilterDTO(
        String userName,
        String role,
        String cin,
        String cne,
        String uuidClub
) { }
