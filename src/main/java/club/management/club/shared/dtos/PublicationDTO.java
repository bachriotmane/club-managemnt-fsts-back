package club.management.club.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record PublicationDTO(
        String id,
        String title,
        String description,
        LocalDateTime date,
        boolean isPublic,
        String publisher
) {
}
