package club.management.club.features.controllers;

import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import club.management.club.features.services.demandes.impl.DemandeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
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

    @PutMapping("/{id}/status")
    public ResponseEntity<DemandeDTO> updateDemandeStatus(
            @PathVariable String id,
            @RequestBody StatutDemande statutDemande
    ) {
        DemandeDTO updatedDemande = demandeService.updateDemandeStatus(id, statutDemande); // Utilisation du service pour la mise à jour et la conversion
        return ResponseEntity.ok(updatedDemande);
    }

}

