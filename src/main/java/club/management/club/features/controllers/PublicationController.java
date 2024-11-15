package club.management.club.features.controllers;

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
    public Page<PublicationDTO> getPublications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) boolean isPublic,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        return publicationsService.getAllPublications(page, size, true, isPublic, keyword, fromDate, toDate);
    }
}