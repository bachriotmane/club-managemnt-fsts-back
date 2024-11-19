package club.management.club.features.controllers;

import club.management.club.features.dto.requests.ClubCreationDTO;
import club.management.club.features.dto.requests.EventCreationDTO;
import club.management.club.features.dto.requests.IntegrationCreationDTO;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.*;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.features.services.demandes.DemandeService;
import club.management.club.features.services.events.EventsService;
import club.management.club.features.services.historiques.HistoriqueService;
import club.management.club.features.services.integration.IntegrationService;
import club.management.club.features.services.users.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/demandes")
@RequiredArgsConstructor
public class DemandeController {
    private final DemandeService demandeService;
    private final UserService userService;
    private final ClubService clubService;
    private final IntegrationService integrationService;
    private final HistoriqueService historiqueService;
    private final EventsService eventsService;


    @GetMapping
    public Page<DemandeDTO> getAllDemandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return demandeService.getAllDemandes(PageRequest.of(page, size));
    }

    @GetMapping("/filter")
    public Page<DemandeDTO> filterDemandesByType(
            @RequestParam TypeDemande type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return demandeService.filterDemandesByType(type, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<Demande> addDemande(@RequestBody Demande demande) {
        Demande savedDemande = demandeService.addDemande(demande);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDemande);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable String id) {
        Demande demande = demandeService.getDemandeById(id);
        return ResponseEntity.ok(demande);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(
            @PathVariable String id,
            @RequestBody Demande demande
    ) {
        Demande updatedDemande = demandeService.updateDemande(id, demande);
        return ResponseEntity.ok(updatedDemande);
    }

    @PostMapping("/integration/depose")
    @Transactional
    public ResponseEntity<?> CreateIntegrationDemande(
            @RequestParam String clubId,
            @RequestBody @Valid  IntegrationCreationDTO integrationCreationDTO ,
            Authentication authentication
    ) {
        String userEmail = authentication.getPrincipal().toString();
        Etudiant etudiant = (Etudiant) userService.findUserByEmail(userEmail);

        Club savedClub = clubService.findById(clubId);

        Integration integration = Integration
                .builder()
                .etudiant(etudiant)
                .isValid(false)
                .roleName("member")
                .memberRole(MemberRole.MEMBER)
                .club(savedClub)
                .build();
        Integration savedIntegration = integrationService.save(integration);

        savedClub.getIntegrations().add(integration);
        clubService.save(savedClub);

        Historique historique = Historique.builder()
                .date(LocalDateTime.now())
                .titre("Demande d'integration")
                .description("Demande posée par l'étudiant "+userService.getFullName(etudiant))
                .build();
        historiqueService.save(historique);

        Demande demande =Demande.builder()
                .date(new Date())
                .statutDemande(StatutDemande.EN_COURS)
                .type(TypeDemande.INTEGRATION_CLUB)
                .motivation(integrationCreationDTO.motivation())
                .etudiantDemandeur(etudiant)
                .club(savedClub)
                .historiques(List.of(historique))
                .integration(savedIntegration)
                .build();
        demandeService.save(demande);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/creation/depose")
    @Transactional
    public ResponseEntity<?> ClubCreationDemande(
            @RequestBody @Valid ClubCreationDTO clubCreationDTO ,
            Authentication authentication
    ) {
        String userEmail = authentication.getPrincipal().toString();
        Etudiant etudiant = (Etudiant) userService.findUserByEmail(userEmail);

        Club club = Club.builder()
                .nom(clubCreationDTO.nomClub())
                .description(clubCreationDTO.description())
                .activites(clubCreationDTO.activities())
                .instagramme(clubCreationDTO.instagram())
                .isValid(false)
                .build();

        Club savedClub = clubService.save(club);

        Integration integration = Integration
                .builder()
                .etudiant(etudiant)
                .isValid(false)
                .roleName("Admin")
                .memberRole(MemberRole.ADMIN)
                .club(savedClub)
                .build();
        Integration savedIntegration = integrationService.save(integration);

        Historique historique = Historique.builder()
                .date(LocalDateTime.now())
                .titre("Demande de Creation d'un Club")
                .description("Demande posée par l'étudiant "+userService.getFullName(etudiant))
                .build();
        Historique savedHistorique = historiqueService.save(historique);

        savedClub.getIntegrations().add(integration);
        clubService.save(savedClub);

        Demande demande =Demande.builder()
                .date(new Date())
                .statutDemande(StatutDemande.EN_COURS)
                .type(TypeDemande.CREATION_CLUB)
                .etudiantDemandeur(etudiant)
                .club(savedClub)
                .historiques(List.of(historique))
                .integration(savedIntegration)
                .build();
        demandeService.save(demande);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/organization/depose")
    @Transactional
    public ResponseEntity<?> eventCreationDemande(
            @RequestParam String clubId,
            @RequestBody @Valid EventCreationDTO eventCreationDTO ,
            Authentication authentication
    ) {
        String userEmail = authentication.getPrincipal().toString();
        Etudiant etudiant = (Etudiant) userService.findUserByEmail(userEmail);

        Club savedClub = clubService.findById(clubId);

        Evenement evenement = Evenement.builder()
                .isValid(false)
                .club(savedClub)
                .nom(eventCreationDTO.EventName())
                .description(eventCreationDTO.description())
                .budget(eventCreationDTO.budget())
                .date(eventCreationDTO.eventDate())
                .location(eventCreationDTO.location())
                .build();
        Evenement savedEvent = eventsService.save(evenement);

        Historique historique = Historique.builder()
                .date(LocalDateTime.now())
                .titre("Demande de Creation d'un Evenement")
                .description("Demande posée par l'étudiant "+userService.getFullName(etudiant))
                .build();
        Historique savedHistorique = historiqueService.save(historique);

        Demande demande =Demande.builder()
                .date(new Date())
                .statutDemande(StatutDemande.EN_COURS)
                .type(TypeDemande.EVENEMENT)
                .etudiantDemandeur(etudiant)
                .club(savedClub)
                .organisationEvenement(savedEvent)
                .historiques(List.of(savedHistorique))
                .historiques(List.of(historique))
                .build();
        demandeService.save(demande);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/historique/{id}")
    public ResponseEntity<List<Historique>> getDemandeHistorique(@PathVariable String id){
        Demande savedDemande = demandeService.getDemandeById(id);
        return ResponseEntity.ok(savedDemande.getHistoriques());
    }
}

