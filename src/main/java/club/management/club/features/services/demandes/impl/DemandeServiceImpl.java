package club.management.club.features.services.demandes.impl;

import club.management.club.features.Specifications.DemandeSpecifications;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.mappers.DemandeMapper;
import club.management.club.features.repositories.DemandeRepository;
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
                .map(DemandeMapper::toLiteDto); // Utilise une version simplifiée du DTO
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

    @Override
    public Demande updateDemande(String id, Demande demande) {
        Demande existingDemande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande","DemandeId",id));

        existingDemande.setDate(demande.getDate());
        existingDemande.setStatutDemande(demande.getStatutDemande());
        existingDemande.setType(demande.getType());
        existingDemande.setEtudiantDemandeur(demande.getEtudiantDemandeur());
        existingDemande.setClub(demande.getClub());
        existingDemande.setIntegration(demande.getIntegration());
        existingDemande.setHistoriques(demande.getHistoriques());
        existingDemande.setOrganisationEvenement(demande.getOrganisationEvenement());

        return demandeRepository.save(existingDemande);
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
