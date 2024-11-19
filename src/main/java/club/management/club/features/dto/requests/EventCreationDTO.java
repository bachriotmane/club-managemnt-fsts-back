package club.management.club.features.dto.requests;

import java.time.LocalDateTime;

public record EventCreationDTO(
        String clubId,
        String EventName,
        String description,
        String location,
        LocalDateTime eventDate,
        Double budget
) {
}
