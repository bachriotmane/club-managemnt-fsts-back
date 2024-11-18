package club.management.club.features.services.demandes.impl;

import club.management.club.features.Specifications.DemandeSpecifications;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.mappers.DemandeMapper;
import club.management.club.features.repositories.DemandeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public Page<DemandeDTO> getAllDemandes(Pageable pageable) {
        return demandeRepository.findAll(pageable)
                .map(DemandeMapper::toLiteDto); // Utilisation de DemandeMapper pour inclure le CNE
    }

    @Override
    public void deleteDemande(String id) {
        if (!demandeRepository.existsById(id)) {
            throw new RuntimeException("Demande introuvable avec l'id : " + id);
        }
        demandeRepository.deleteById(id);
    }

    @Override
    public Page<DemandeDTO> filterDemandesByType(TypeDemande type, Pageable pageable) {
        return demandeRepository.findAll(DemandeSpecifications.withType(type), pageable)
                .map(DemandeMapper::toLiteDto); // Utilisation de DemandeMapper pour inclure le CNE
    }

    @Override
    public List<DemandeDTO> getDemandesByEtudiant(String etudiantId) {
        // Ici, nous récupérons toutes les demandes de l'étudiant, puis nous appliquons le DemandeMapper
        return demandeRepository.findAll(
                        (root, query, builder) -> builder.equal(root.get("etudiantDemandeur").get("id"), etudiantId)
                ).stream()
                .map(DemandeMapper::toLiteDto) // Assurez-vous que DemandeMapper inclut le CNE
                .collect(Collectors.toList());
    }

    @Override
    public Demande addDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande getDemandeById(String id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable avec l'ID : " + id));
    }



    public DemandeDTO updateDemandeStatus(String id, StatutDemande statutDemande) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        demande.setStatutDemande(statutDemande); // Mise à jour du statut
        demandeRepository.save(demande); // Sauvegarder la demande mise à jour
        return DemandeMapper.toLiteDto(demande); // Retourner le DemandeDTO après mise à jour
    }

}
