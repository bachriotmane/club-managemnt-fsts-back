package club.management.club.controllers;

import club.management.club.dto.PublicationDTO;
import club.management.club.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        return publicationsService.getAllPublications(page, size, true, isPublic, keyword, fromDate, toDate);
    }
}
