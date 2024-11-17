package club.management.club.features.dto.responses;

import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
public record ClubDetailsResponse(
        String uuid,
        String nom,
        String description,
        Date createdAt,
        String logo,
        String instagramme,
        Integer nbrMembres,
        String nomFondateur,
        List<String> activites,
        List<ProfilsDetailsDto> profilsDetailsDto
) implements Serializable {
}
