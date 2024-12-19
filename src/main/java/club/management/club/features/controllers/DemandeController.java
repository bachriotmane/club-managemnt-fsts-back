package club.management.club.features.controllers;

import club.management.club.features.dto.requests.ClubCreationDTO;
import club.management.club.features.dto.requests.EventCreationDTO;
import club.management.club.features.dto.requests.IntegrationCreationDTO;
import club.management.club.features.dto.requests.UpdateDemandeStatusDTO;
import club.management.club.features.dto.responses.*;
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
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SecurityRequirement(name = "Bearer Authentication")
public class DemandeController {
    private static final Logger log = LoggerFactory.getLogger(DemandeController.class);
    private final DemandeService demandeService;
    private final UserService userService;
    private final ClubService clubService;
    private final IntegrationService integrationService;
    private final HistoriqueService historiqueService;
    private final EventsService eventsService;

    @GetMapping("/demandeur/{demandeurId}")
    public List<DemandeDTO3> getDemandesByDemandeurId(@PathVariable String demandeurId) {
        System.out.println(demandeurId);
        return demandeService.getDemandesByDemandeurId(demandeurId);
    }


    @GetMapping("/dmd")
    public List<Demande> getAllDemandes() {
        return demandeService.getAllDemandess();
    }

    @GetMapping
    public Page<DemandeDTO> getAllDemandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return demandeService.getAllDemandes(PageRequest.of(page, size));
    }

    @GetMapping("/filter")
    public Page<DemandeDTO> filterDemandesByType(
            Authentication authentication,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) boolean isMyDemandes,
            @RequestParam(required = false) String uuidClub
    ) {
        return demandeService.filterDemandesByType(authentication,type, PageRequest.of(page, size),nom,isMyDemandes,uuidClub);
    }

    @PostMapping
    public ResponseEntity<Demande> addDemande(@RequestBody Demande demande) {
        Demande savedDemande = demandeService.addDemande(demande);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDemande);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeDTO> getDemandeById(@PathVariable String id) {
        DemandeDTO demandeDTO = demandeService.getDemandeById(id);
        return ResponseEntity.ok(demandeDTO);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<DemandeDTO> updateDemandeStatus(
            @PathVariable String id,
            @RequestBody UpdateDemandeStatusDTO statutDemande
    ) {
        System.out.println(statutDemande);
        DemandeDTO updatedDemande = demandeService.updateDemandeStatus(id, statutDemande.statutDemande(), statutDemande.agent(), statutDemande.comment());
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
                .deactivate(false)
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
                .nom(eventCreationDTO.eventName())
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
                .build();
        demandeService.save(demande);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Edit Student Roles by integration UUID.")
    @PutMapping("/edit-integration/{uuid}")
    public SuccessResponse<MembersListDTO> editRoleStudent(
            @PathVariable String uuid,
            @RequestParam String roleName,
            @RequestParam String memberRole) {
        var role = MemberRole.valueOf(memberRole.toUpperCase());

        return integrationService.editRoleStudent(uuid, roleName, role);
    }
    @DeleteMapping("/integration/delete/{uuid}")
    public SuccessResponse<Boolean> deleteIntegration(Authentication authentication,@PathVariable String uuid){
        return integrationService.deleteIntegration(authentication,uuid);
    }

    @GetMapping("/demande/{id}")
    public ResponseEntity<DemandeDTO2> findById(@PathVariable String id) {
        DemandeDTO2 demandeDTO2 = demandeService.findById(id);
        return ResponseEntity.ok(demandeDTO2);
    }

    @GetMapping("/historique/{id}")
    public ResponseEntity<List<Historique>> getDemandeHistorique(@PathVariable String id){
        Demande savedDemande = demandeService.getDemandeById2(id);
        return ResponseEntity.ok(savedDemande.getHistoriques());
    }

    @GetMapping("/demande-details/{id}")
    public DemandeDetailsDTO getDemandeDetails(@PathVariable String id){
        return demandeService.getDemandeDetails(id);
    }



    @GetMapping("/count")
    public ResponseEntity<Integer> countDemandesByEtudiant(@RequestParam String etudiantId) {
        int count = demandeService.countDemandesByEtudiant(etudiantId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/integration")
    public ResponseEntity<Integer> countIntegrationDemandesByAdmin(@RequestParam String adminId) {
        int count = demandeService.countIntegrationDemandesByAdmin(adminId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/creation-en-cours")
    public ResponseEntity<Integer> countCreationDemandesEnCours() {
        int count = demandeService.countCreationDemandesEnCours();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/evenement-en-cours")
    public ResponseEntity<Integer> countEvenementDemandesEnCours() {
        int count = demandeService.countEvenementDemandesEnCours();
        return ResponseEntity.ok(count);
    }

}


