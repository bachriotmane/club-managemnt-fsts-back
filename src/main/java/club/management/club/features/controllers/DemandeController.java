package club.management.club.features.controllers;

import club.management.club.features.dto.requests.DemandeCreationDTO;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Demande;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Integration;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.features.services.demandes.impl.DemandeService;
import club.management.club.features.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demandes")
@RequiredArgsConstructor
public class DemandeController {
    private final DemandeService demandeService;
    private final UserService userService;
    private final ClubService clubService;

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
    @PostMapping("/depose")
    public ResponseEntity<Demande> CreateGeneralDemande(@RequestBody DemandeCreationDTO demandeDTO , Authentication authentication) {
        String userEmail = authentication.getPrincipal().toString();
        Etudiant etudiant = (Etudiant) userService.findUserByEmail(userEmail);
        if (demandeDTO.demandeType() == TypeDemande.INTEGRATION_CLUB){
            String clubId = demandeDTO.integration().clubId();
            Club savedClub = clubService.findById(clubId);
            Integration integration = new Integration();
            Integration
                    .builder()
                    .etudiant(etudiant)
                    .isValid(false)
                    .roleName("member")
                    .club(savedClub)
                    .build();
        }

        return null;
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
}

