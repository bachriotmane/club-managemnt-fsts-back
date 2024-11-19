package club.management.club.features.dto.responses;

import java.io.Serializable;

public record ClubSimpleDTO (
    String clubId,
    String clubName
) implements Serializable {
}
