package club.management.club.features.dto.responses;

import java.time.LocalDateTime;

public record EventResponseDTO(
        String id,
        String nom,
        String description,
        LocalDateTime date,
        String publisher,
        long membersLength,
        String clubId,
        String location,
        String instagram,
        String imageId
) {
}
