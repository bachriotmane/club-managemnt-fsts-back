package club.management.club.features.controllers;

import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Publication;
import club.management.club.features.services.publications.PublicationsService;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publications")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PublicationController {
    private final PublicationsService publicationsService;

    @GetMapping
    public Page<PublicationDTO> getPublications(@ModelAttribute PublicationsRequest publicationsRequest) {
        return publicationsService.getAllPublications(publicationsRequest);
    }

    @GetMapping("/{id}")
    public PublicationDTO getPublicationById(@PathVariable String id){
        return publicationsService.get(id);
    }

    @PostMapping
    public PublicationDTO createPublication(@RequestBody PublicationDTO publicationDTO){
        return publicationsService.create(publicationDTO);
    }

    @GetMapping("/home")
    public Page<PublicationDTO> getHomePublications() {
        // Créer un objet PublicationsRequest avec une taille de page de 7 et une page 0
        PublicationsRequest publicationsRequest = new PublicationsRequest(
                0,  // Page 0 (première page)
                7,  // Taille de la page = 7 publications
                null, // Pas de filtre pour le mot-clé
                null, // Pas de filtre pour le statut public
                null, // Pas de filtre pour la date de début
                null, // Pas de filtre pour la date de fin
                null  // Pas de filtre pour l'ID utilisateur
        );

        // Appel du service avec l'objet PublicationsRequest
        return publicationsService.getAllPublications(publicationsRequest);
    }

    @PostMapping("/{publicationId}/image/{imageId}")
    public SuccessResponse<PublicationDTO> associateImageWithPublication(
            @PathVariable String publicationId,
            @PathVariable String imageId) {
        PublicationDTO pub = publicationsService.addImageToPublication(publicationId, imageId);
        return new SuccessResponse<>(
                pub
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePublication(@PathVariable String id) {
        try {
            publicationsService.deletePublicationById(id);
            return ResponseEntity.ok("Publication deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public PublicationDTO updatePublication(@PathVariable String id, @RequestBody PublicationDTO publicationDTO){
        return publicationsService.update(id, publicationDTO);
    }
}
