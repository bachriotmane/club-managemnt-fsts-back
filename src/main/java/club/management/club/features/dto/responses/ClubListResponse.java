package club.management.club.features.dto.responses;

import java.io.Serializable;
import java.util.Date;

public record ClubListResponse(
         String uuid,
         String nom,
         String description,
         Date createdAt,
         String logo,
         String instagramme

) implements Serializable {
}
