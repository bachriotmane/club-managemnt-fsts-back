package club.management.club.features.mappers;

import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;

public class DemandeMapper {

    public static DemandeDTO toLiteDto(Demande demande) {
        return DemandeDTO.builder()
                .id(demande.getId())
                .date(demande.getDate())
                .statutDemande(demande.getStatutDemande())
                // Récupère le CNE de l'étudiant et l'ajoute dans le DTO
                .cne(demande.getEtudiantDemandeur() != null ? demande.getEtudiantDemandeur().getCne() : null)
                .build();
    }
}
