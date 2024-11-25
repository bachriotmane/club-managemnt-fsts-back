package club.management.club.features.mappers;

import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.dto.responses.DemandeDTO2;
import club.management.club.features.dto.responses.DemandeDTO3;
import club.management.club.features.entities.Demande;
import org.springframework.stereotype.Component;

@Component
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

    public static DemandeDTO2 toLiteDto2(Demande demande) {
        return DemandeDTO2.builder()
                .id(demande.getId())
                .statutDemande(demande.getStatutDemande())
                .type(demande.getType())
                .idClub(demande.getClub() != null ? demande.getClub().getId() : null)
                .idEvent(demande.getOrganisationEvenement() != null ? demande.getOrganisationEvenement().getId() : null)
                .idIntegration(demande.getIntegration() != null ? demande.getIntegration().getId() : null)
                .build();
    }
    public DemandeDTO3 toDemandeDTO3(Demande demande) {
        System.out.println("to demandedto"+demande.getId()+  demande.getDescription() + demande.getEtudiantDemandeur().getId());
        return DemandeDTO3.builder()
                .id(demande.getId())
                .description(demande.getDescription())
                .idEtudiant(demande.getEtudiantDemandeur() != null ? demande.getEtudiantDemandeur().getId() : null)
                .build();
    }


}
