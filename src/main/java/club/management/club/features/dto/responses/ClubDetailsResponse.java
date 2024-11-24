package club.management.club.features.dto.responses;



import java.io.Serializable;
import java.util.Date;
import java.util.List;
public record ClubDetailsResponse(
        String uuid,
        boolean isIntegrated,
        String roleName,
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
    public ClubDetailsResponse withUpdates(Boolean isIntegrated, String roleName, String nomFondateur) {
        return new ClubDetailsResponse(
                this.uuid,
                isIntegrated != null ? isIntegrated : this.isIntegrated,
                roleName != null ? roleName : this.roleName,
                this.nom,
                this.description,
                this.createdAt,
                this.logo,
                this.instagramme,
                this.nbrMembres,
                nomFondateur != null ? nomFondateur : this.nomFondateur,
                this.activites,
                this.profilsDetailsDto
        );
    }
}

