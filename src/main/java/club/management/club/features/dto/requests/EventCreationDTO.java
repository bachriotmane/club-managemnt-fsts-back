package club.management.club.features.dto.requests;

import java.time.LocalDateTime;

record EventCreationDTO(
        String EventName,
        String description,
        String location,
        LocalDateTime EventDate,
        Double budget
) {
}
