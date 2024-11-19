package club.management.club.features.dto.requests;

import java.util.List;

public record ClubCreationDTO(
        String nomClub,
        String description,
        String instagram,
        List<String> activities
) {
}
