package club.management.club.features.dto.responses;

import java.io.Serializable;

public record ProfilsDetailsDto(
        String uuid,
        String nom,
        String imgProfile

) implements Serializable {
}
