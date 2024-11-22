package club.management.club.features.services.demandes.impl;

import club.management.club.features.Specifications.DemandeSpecifications;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.mappers.DemandeMapper;
import club.management.club.features.repositories.DemandeRepository;
import club.management.club.features.services.demandes.DemandeService;
import club.management.club.features.services.demandes.DemandeService;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
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
                .map(DemandeMapper::toLiteDto);
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
                .map(DemandeMapper::toLiteDto);
    }

    @Override
    public List<DemandeDTO> getDemandesByEtudiant(String etudiantId) {
        return demandeRepository.findAll(
                        (root, query, builder) -> builder.equal(root.get("etudiantDemandeur").get("id"), etudiantId)
                ).stream()
                .map(DemandeMapper::toLiteDto)
                .collect(Collectors.toList());
    }

    @Override
    public Demande addDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande getDemandeById(String id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande","DemandeId",id));
    }



    public DemandeDTO updateDemandeStatus(String id, StatutDemande statutDemande) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        demande.setStatutDemande(statutDemande);
        demandeRepository.save(demande);
        // OTMANE : Complétez cette fonctionnalité
        // 1. Créer un objet Historique avec un titre basé sur le statut :
        //    - "Flan a accepté votre demande"
        //    - "Flan a refusé votre demande"
        //    (Remarque : Le titre 'Flan' sera récupéré depuis le Controller)
        //
        // 2. Après cela, il faut valider l'entité concernée (Club, Intégration ou Événement)
        //    en fonction du statut :
        //    - Si `Status == VALID`, définir `isValid = true`.
        //    - Sinon, simplement supprimer le Club, l'Événement ou l'Intégration.

        return DemandeMapper.toLiteDto(demande);
    }

    @Override
    public Demande save(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande findById(String id) {
        return demandeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Demande","demandeId",id)
        );
    }

}
