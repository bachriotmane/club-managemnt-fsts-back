package club.management.club.features.services.demandes;

import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.dto.responses.DemandeDTO2;
import club.management.club.features.dto.responses.DemandeDTO3;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DemandeService {
    List<Demande> getAllDemandess();

    Page<DemandeDTO> getAllDemandes(Pageable pageable);

    void deleteDemande(String id);

    Page<DemandeDTO> filterDemandesByType(TypeDemande type, Pageable pageable);


    List<DemandeDTO> getDemandesByEtudiant(String etudiantId);

    Demande addDemande(Demande demande);
    DemandeDTO getDemandeById(String id);

    DemandeDTO updateDemandeStatus(String id, StatutDemande statutDemande, String agent);
    Demande save(Demande demande);
    DemandeDTO2 findById(String id);

    List<DemandeDTO3> getDemandesByDemandeurId(String demandeurId);

}
