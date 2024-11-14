package club.management.club.dto;

import java.util.Date;

public record PublicationDTO(
        String id,
        String title,
        String description,
        Date date,
        boolean isPublic,
        String publisher
) {
}
