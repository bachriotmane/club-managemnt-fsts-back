package club.management.club.features.services.demandes;

import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DemandeService {

    Page<DemandeDTO> getAllDemandes(Pageable pageable);

    void deleteDemande(String id);

    Page<DemandeDTO> filterDemandesByType(TypeDemande type, Pageable pageable);

    List<DemandeDTO> getDemandesByEtudiant(String etudiantId);

    Demande addDemande(Demande demande);
    DemandeDTO getDemandeById(String id);

    DemandeDTO updateDemandeStatus(String id, StatutDemande statutDemande);
    Demande save(Demande demande);
    Demande findById(String id);
    int countDemandesByEtudiant(String etudiantId);
    int countIntegrationDemandesByEtudiant(String etudiantId);

}
