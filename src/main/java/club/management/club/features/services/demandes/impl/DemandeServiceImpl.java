package club.management.club.features.services.demandes.impl;

import club.management.club.features.Specifications.DemandeSpecifications;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.dto.responses.DemandeDTO2;
import club.management.club.features.dto.responses.DemandeDTO3;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.mappers.DemandeMapper;
import club.management.club.features.repositories.DemandeRepository;
import club.management.club.features.services.demandes.DemandeService;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;

    private DemandeMapper demandeMapper;

    public DemandeServiceImpl(DemandeRepository demandeRepository , DemandeMapper demandeMapper) {
        this.demandeRepository = demandeRepository;

    }

    @Override
    public List<DemandeDTO3> getDemandesByDemandeurId(String demandeurId) {
        System.out.println("enter service");
        List<Demande> demandes = demandeRepository.findByEtudiantDemandeurId(demandeurId);
        System.out.println(demandes);
        return demandes.stream()
                .map(this::toDemandeDTO3) // Utilisation de l'instance injectée
                .collect(Collectors.toList());
    }
    public DemandeDTO3 toDemandeDTO3(Demande demande) {
        System.out.println("to demandedto"+demande.getId()+  demande.getDescription() + demande.getEtudiantDemandeur().getId());
        return DemandeDTO3.builder()
                .id(demande.getId())
                .description(demande.getDescription())
                .idEtudiant(demande.getEtudiantDemandeur() != null ? demande.getEtudiantDemandeur().getId() : null)
                .build();
    }


    @Override
    public List<Demande> getAllDemandess() {
        // Récupérer toutes les demandes depuis la base de données
        return demandeRepository.findAll();
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
    public DemandeDTO getDemandeById(String id) {
        // Récupérer la demande
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande", "DemandeId", id));

        // Convertir en DemandeDTO
        return DemandeDTO.builder()
                .id(demande.getId())
                .cne(demande.getEtudiantDemandeur().getCne()) // Vérifiez que le champ existe
                .date(demande.getDate())
                .description(demande.getDescription())
                .statutDemande(demande.getStatutDemande())
                .build();
    }

    @Override
    public DemandeDTO2 findById(String id) {
        // Récupérer la demande
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande", "DemandeId", id));

        // Convertir en DemandeDTO2 avec des vérifications pour éviter les NullPointerExceptions
        return DemandeDTO2.builder()
                .id(demande.getId())
                .statutDemande(demande.getStatutDemande())
                .type(demande.getType())
                .idClub(demande.getClub() != null ? demande.getClub().getId() : null)
                .idEvent(demande.getOrganisationEvenement() != null ? demande.getOrganisationEvenement().getId() : null)
                .idIntegration(demande.getIntegration() != null ? demande.getIntegration().getId() : null)
                .build();
    }


}
