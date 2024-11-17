package club.management.club.features.controllers;

import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publications")
@AllArgsConstructor
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
}
