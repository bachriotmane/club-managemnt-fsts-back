package club.management.club.features.dto.responses;

import java.io.Serializable;
import java.time.LocalDateTime;

public record HistoriqueDTO(
     String id,
     String titre,
     String description,
     LocalDateTime date
) implements Serializable {
}
