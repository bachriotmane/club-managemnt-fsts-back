package club.management.club.features.services.demandes;

import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
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
    Demande getDemandeById(String id);
    Demande updateDemande(String id, Demande demande);
    Demande save(Demande demande);
    Demande findById(String id);
}
