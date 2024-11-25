package club.management.club.features.controllers;


import club.management.club.features.entities.Integration;
import club.management.club.features.services.integration.IntegrationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrations")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class IntegrationController {

    private final IntegrationService integrationService;


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntegration(@PathVariable String id) {
        integrationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Integration> saveIntegration(@RequestBody Integration integration) {
        Integration savedIntegration = integrationService.save(integration);
        return new ResponseEntity<>(savedIntegration, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/accepter")
    public ResponseEntity<String> accepterIntegration(@PathVariable String id) {
        try {
            // Appel du service pour accepter l'intégration
            integrationService.accepterIntegration(id);
            return ResponseEntity.ok("Intégration acceptée avec succès");
        } catch (Exception e) {
            // Gestion des erreurs
            return ResponseEntity.status(500).body("Erreur lors de l'acceptation de l'intégration");
        }
    }
}

