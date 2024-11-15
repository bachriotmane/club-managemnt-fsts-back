package club.management.club.features.controllers;

import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.dto.responses.ClubListResponse;
import club.management.club.features.services.clubs.ClubList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
@Tag(name = "Clubs API")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubsController {

    private final ClubList clubList;

    @GetMapping
    @Operation(summary = "Get all clubs.")
    public ListSuccessResponse<ClubListResponse> getAllClubs(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String nomClub) {
        var paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return clubList.getAllClubs(paging, nomClub);
    }
}
