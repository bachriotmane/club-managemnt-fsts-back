package club.management.club.features.services.demandes.impl;

import club.management.club.features.Specifications.DemandeSpecifications;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.dto.responses.DemandeDTO2;
import club.management.club.features.dto.responses.DemandeDTO3;
import club.management.club.features.dto.responses.DemandeDetailsDTO;
import club.management.club.features.entities.*;
import club.management.club.features.entities.Demande;
import club.management.club.features.entities.Integration;
import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.mappers.DemandeMapper;
import club.management.club.features.repositories.*;
import club.management.club.features.entities.Demande;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.repositories.DemandeRepository;
import club.management.club.features.services.auths.AuthorityService;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.features.services.demandes.DemandeService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.exceptionHandler.BadRequestException;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private final EtudiantRepository etudiantRepository;

    private DemandeMapper demandeMapper;

    public DemandeServiceImpl(DemandeRepository demandeRepository , DemandeMapper demandeMapper, IntegrationRepository integrationRepository, ClubRepository clubRepository, EventRepository eventRepository, HistoriqueRepo historiqueRepo, JwtTokenService jwtTokenService, AuthorityService authorityService, UserRepo userRepo, EtudiantRepository etudiantRepository) {
        this.demandeRepository = demandeRepository;
        this.integrationRepository = integrationRepository;
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
        this.historiqueRepo = historiqueRepo;
        this.jwtTokenService = jwtTokenService;
        this.userRepo = userRepo;
        this.etudiantRepository = etudiantRepository;

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

    @Override
    public DemandeDetailsDTO getDemandeDetails(String id) {
        Demande demande = demandeRepository.findById(id).orElseThrow(()-> new RuntimeException("Demande not found"));
        return  new DemandeDetailsDTO(
                demande.getId(),
                demande.getType(),
                demande.getStatutDemande(),
                demande.getEtudiantDemandeur().getFirstName() + " " + demande.getEtudiantDemandeur().getLastName(),
                demande.getEtudiantDemandeur().getId(),
                demande.getClub() == null ? null : demande.getClub().getNom(),
                demande.getClub() == null ? null : demande.getClub().getId(),
                demande.getMotivation(),
                demande.getClub() == null ? null : demande.getClub().getInstagramme(),
                demande.getDescription(),
                demande.getClub() == null ? null:demande.getClub().getActivites(),
                demande.getOrganisationEvenement() == null ? null : demande.getOrganisationEvenement().getNom(),
                demande.getOrganisationEvenement() == null ? null :  demande.getOrganisationEvenement().getLocation(),
                demande.getOrganisationEvenement() == null ? 0.0 :  demande.getOrganisationEvenement().getBudget(),
                demande.getOrganisationEvenement() == null ? null :  demande.getOrganisationEvenement().getDate(),
                demande.getOrganisationEvenement() == null ? null :  demande.getOrganisationEvenement().getDescription()


        );

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
    @Transactional
    public DemandeDTO updateDemandeStatus(String id, StatutDemande statutDemande, String agent, String commentaire) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        System.out.println(commentaire);
        demande.setStatutDemande(statutDemande);
        Historique historique = Historique.builder().date(LocalDateTime.now()).build();
        historique.setRaison(commentaire);
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

                // Nullify the reference in Demande
                demande.setClub(null);
                demandeRepository.save(demande);

                // Get the list of integrations associated with the club
                List<Integration> integrations = club.getIntegrations();

                // Nullify club reference
                club.setIntegrations(null);
                clubRepository.save(club);

                // Handle each Integration
                integrations.forEach(integration -> {
                    // Nullify the reference in Demande for each Integration
                    List<Demande> demandes = demandeRepository.findByIntegrationId(integration.getId());
                    demandes.forEach(d -> {
                        d.setIntegration(null);
                        demandeRepository.save(d);
                    });

                    // Nullify references in Integration and delete it
                    integration.setClub(null);
                    integration.setEtudiant(null);
                    integrationRepository.deleteById(integration.getId());
                });

                // Finally, delete the club
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
                integration = integrationRepository.save(integration);
                Etudiant etudiant = etudiantRepository.findById(integration.getEtudiant().getId()).orElseThrow(
                        () -> new RuntimeException("etudiant not found")
                );
                if(etudiant.getIntegrations() == null){
                    etudiant.setIntegrations(new ArrayList<>());
                }
                assert etudiant.getIntegrations() != null;
                etudiant.getIntegrations().add(integration);
                etudiantRepository.save(etudiant);
                Club club = clubRepository.findById(integration.getClub().getId()).orElseThrow(
                        () -> new RuntimeException("club not found")
                );
                if(club.getIntegrations() == null){
                    club.setIntegrations(new ArrayList<>());
                }
                club.getIntegrations().add(integration);
                clubRepository.save(club);
            }
            else if(demande.getType().equals(TypeDemande.CREATION_CLUB)){
                Club club = clubRepository.findById(demande.getClub().getId()).orElseThrow(
                        () -> new RuntimeException("Club not found")
                );
                Integration integration = integrationRepository.findById(demande.getIntegration().getId()).orElseThrow(
                        () -> new RuntimeException("integration not found")
                );
                integration.setValid(true);
                integration.setIntegrationDate(new Date());
                club.setValid(true);
                clubRepository.save(club);
                integrationRepository.save(integration);

            }
            else if(demande.getType().equals(TypeDemande.EVENEMENT)){
                Evenement evenement = eventRepository.findById(demande.getOrganisationEvenement().getId()).orElseThrow(
                        () -> new RuntimeException("Event not found")
                );
                evenement.setValid(true);
                Club club = clubRepository.findById(evenement.getClub().getId()).orElseThrow(()->
                        new RuntimeException("Club not found")
                );
                evenement = eventRepository.save(evenement);
                club.getEvenements().add(evenement);
                clubRepository.save(club);
            }
        }
        historique.setRaison(commentaire);
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

    @Override
    public int countDemandesByEtudiant(String etudiantId) {
        return demandeRepository.countByEtudiantDemandeur_Id(etudiantId);
    }


    @Override
    public int countIntegrationDemandesByAdmin(String adminId) {
        // Récupérer les ID des clubs administrés directement depuis la base
        List<String> adminClubIds = integrationRepository.findClubIdsByEtudiantIdAndMemberRole(adminId, MemberRole.ADMIN);

        if (adminClubIds.isEmpty()) {
            return 0; // Aucun club administré
        }

        // Compter les demandes d'intégration liées à ces clubs
        return demandeRepository.countByTypeAndClubIdIn(TypeDemande.INTEGRATION_CLUB, adminClubIds);
    }

}
