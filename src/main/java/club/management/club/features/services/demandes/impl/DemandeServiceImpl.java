package club.management.club.features.services.demandes.impl;

import club.management.club.features.Specifications.DemandeSpecifications;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.dto.responses.DemandeDTO2;
import club.management.club.features.dto.responses.DemandeDTO3;
import club.management.club.features.entities.*;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.mappers.DemandeMapper;
import club.management.club.features.repositories.*;
import club.management.club.features.entities.Demande;
import club.management.club.features.repositories.DemandeRepository;
import club.management.club.features.services.auths.AuthorityService;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.demandes.DemandeService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.exceptionHandler.BadRequestException;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final IntegrationRepository integrationRepository;
    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;
    private final HistoriqueRepo historiqueRepo;
    private final JwtTokenService jwtTokenService;
    private final  UserRepo userRepo;

    private DemandeMapper demandeMapper;

    public DemandeServiceImpl(DemandeRepository demandeRepository , DemandeMapper demandeMapper, IntegrationRepository integrationRepository, ClubRepository clubRepository, EventRepository eventRepository, HistoriqueRepo historiqueRepo, JwtTokenService jwtTokenService, EtudiantRepository etudiantRepository, AuthorityService authorityService, UserRepo userRepo) {
        this.demandeRepository = demandeRepository;
        this.integrationRepository = integrationRepository;
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
        this.historiqueRepo = historiqueRepo;
        this.jwtTokenService = jwtTokenService;
        this.userRepo = userRepo;

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
    public Page<DemandeDTO> filterDemandesByType(
            Authentication authentication,
            String type,
            Pageable pageable,
            String nom,
            boolean isMyDemandes,
            String  uuidClub
    ) {
        var idStudent = jwtTokenService.getUserId(authentication);

        var studentOpt = userRepo.findById(idStudent);
        if (studentOpt.isEmpty()) {
            throw new BadRequestException(ValidationConstants.USER_NOT_FOUND);
        }
        var student = studentOpt.get();

        boolean isUser = student.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_USER".equals(auth.getName()));

        var typeDemande = "ALL".equalsIgnoreCase(type) ? null : TypeDemande.valueOf(type.toUpperCase());

        var spec = DemandeSpecifications.withType(typeDemande)
                .and(DemandeSpecifications.withNom(nom))
                .and(DemandeSpecifications.withClubId(uuidClub));


        if (isUser) { //student
            spec = spec.and(!isMyDemandes ?
                            DemandeSpecifications.withStudentAsAdminInClub(idStudent)
                            :DemandeSpecifications.withStudentId(idStudent))
                    .and(!isMyDemandes ?DemandeSpecifications.includeOnlyTypeIntegrationClub() : null);
        } else { //admin
            spec = spec.and(DemandeSpecifications.excludeTypeIntegrationClub());
        }

        return demandeRepository.findAll(spec, pageable)
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
    public DemandeDTO updateDemandeStatus(String id, StatutDemande statutDemande, String agent) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        demande.setStatutDemande(statutDemande);
        Historique historique = Historique.builder().date(LocalDateTime.now()).build();
        if(statutDemande.equals(StatutDemande.REFUSE)){
            historique.setTitre("Votre demande a ete refuse");
            historique.setDescription("La demande a ete refuse par " + agent);
            demande.setStatutDemande(StatutDemande.REFUSE);
            if(demande.getType().equals(TypeDemande.INTEGRATION_CLUB)){
                Integration integration = integrationRepository.findById(demande.getIntegration().getId()).orElseThrow(
                        () -> new RuntimeException("integration not found")
                );
                Club club = clubRepository.findById(integration.getClub().getId()).orElseThrow(
                        () -> new RuntimeException("club not found")
                );
                final String s = integration.getId();
                club.getIntegrations().removeIf((c)->c.getId().equals(s));
                clubRepository.save(club);
                System.out.println("Cas Refuse : demandeId = "+ demande.getIntegration().getId() + " " + id + " " + statutDemande + " " + agent);
                demande.setIntegration(null);
                demande = demandeRepository.save(demande);
                integration.setClub(null);
                integration.setEtudiant(null);
                integration = integrationRepository.save(integration);
                integrationRepository.deleteById(integration.getId());
                demande = demandeRepository.save(demande);
            }
            else if(demande.getType().equals(TypeDemande.CREATION_CLUB)){
                Club club = clubRepository.findById(demande.getClub().getId()).orElseThrow(
                        () -> new RuntimeException("Club not found")
                );
                demande.setClub(null);
                demande = demandeRepository.save(demande);
                List<Integration> integrations = club.getIntegrations();
                club.setIntegrations(null);
                club = clubRepository.save(club);
                integrations.forEach(
                       ( i )->{
                           i.setClub(null);
                           i.setEtudiant(null);
                           integrationRepository.deleteById(i.getId());
                       }
                );
                clubRepository.deleteById(club.getId());
            }
            else if(demande.getType().equals(TypeDemande.EVENEMENT)){
                Evenement evenement = eventRepository.findById(demande.getOrganisationEvenement().getId()).orElseThrow(
                        () -> new RuntimeException("Event not found")
                );
                demande.setOrganisationEvenement(null);
                demande = demandeRepository.save(demande);
                Club club = evenement.getClub();
                club.getEvenements().removeIf((e)->e.getId().equals(evenement.getId()));
                clubRepository.save(club);
                eventRepository.deleteById(evenement.getId());
            }
        }

        if(statutDemande.equals(StatutDemande.ACCEPTE)){
            historique.setTitre("Votre demande a ete accepte");
            historique.setDescription("La demande a ete accepte par " + agent);
            demande.setStatutDemande(StatutDemande.ACCEPTE);
            if(demande.getType().equals(TypeDemande.INTEGRATION_CLUB)){
                Integration integration = integrationRepository.findById(demande.getIntegration().getId()).orElseThrow(
                        () -> new RuntimeException("integration not found")
                );
                integration.setValid(true);
                integrationRepository.save(integration);
            }
            else if(demande.getType().equals(TypeDemande.CREATION_CLUB)){
                Club club = clubRepository.findById(demande.getClub().getId()).orElseThrow(
                        () -> new RuntimeException("Club not found")
                );
                Integration integration = integrationRepository.findById(demande.getIntegration().getId()).orElseThrow(
                        () -> new RuntimeException("integration not found")
                );
                integration.setValid(true);
                club.setValid(true);
                clubRepository.save(club);
            }
            else if(demande.getType().equals(TypeDemande.EVENEMENT)){
                Evenement evenement = eventRepository.findById(demande.getOrganisationEvenement().getId()).orElseThrow(
                        () -> new RuntimeException("Event not found")
                );
                evenement.setValid(true);
                eventRepository.save(evenement);
            }
        }
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
        historique = historiqueRepo.save(historique);
        demande.getHistoriques().add(historique);
        demandeRepository.save(demande);
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
    public Demande getDemandeById2(String id) {
        return demandeRepository.findById(id).orElseThrow(()-> new RuntimeException("Demande not found"));
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
