package club.management.club.features.dto.responses;

import java.time.LocalDateTime;

public record PublicationDTO(
        String id,
        String title,
        String description,
        LocalDateTime date,
        boolean isPublic,
        String publisher
) {
}
