package club.management.club.features.dto.responses;

import java.io.Serializable;

public record ClubNameDTO(
        String nom,
        String uuid
)implements Serializable {
}
