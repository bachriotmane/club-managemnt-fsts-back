package club.management.club.controllers;

import club.management.club.entities.Demande;
import club.management.club.services.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;


    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeService.getAllDemandes();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        return demande.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Demande createOrUpdateDemande(@RequestBody Demande demande) {
        return demandeService.saveOrUpdateDemande(demande);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        demandeService.deleteDemande(id);
        return ResponseEntity.noContent().build();
    }
}
