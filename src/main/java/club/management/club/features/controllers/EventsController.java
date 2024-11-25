package club.management.club.features.controllers;

import club.management.club.features.dto.requests.EventRequest;
import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.services.events.EventsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class EventsController {
    private final EventsService eventsService;

    @PutMapping("/{id}/accepter")
    public ResponseEntity<String> accepterEvenement(@PathVariable String id) {
        try {
            // Appel du service pour accepter l'événement
            eventsService.accepterEvenement(id);
            return ResponseEntity.ok("Événement accepté avec succès");
        } catch (Exception e) {
            // Gestion des erreurs
            return ResponseEntity.status(500).body("Erreur lors de l'acceptation de l'événement");
        }
    }

    @GetMapping
    public Page<EventResponseDTO> getPublications(@ModelAttribute EventRequest eventRequest) {
        return eventsService.getAllEvents(eventRequest);
    }

    @GetMapping("/{id}")
    public EventResponseDTO getPublicationById(@PathVariable String id){
        return eventsService.get(id);
    }
    
    @GetMapping("/home-events")
    public List<EventResponseDTO> getHomeEvents(@RequestParam(defaultValue = "7") int limit) {
        return eventsService.getHomeEvents(limit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenementById(@PathVariable String id) {
        eventsService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retourne un statut 204 No Content
    }


}
