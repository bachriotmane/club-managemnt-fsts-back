package club.management.club.features.controllers;

import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.services.clubs.ClubDetails;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.dto.responses.ClubListResponse;
import club.management.club.features.services.clubs.ClubList;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
@Tag(name = "Clubs API")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubsController {

    private final ClubList clubList;
    private final ClubDetails clubDetails;

    @GetMapping
    @Operation(summary = "Get all clubs.")
    public ListSuccessResponse<ClubListResponse> getAllClubs(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String nomClub,
                                                             @RequestParam(defaultValue = "") String idUser
    ) {
        var paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return clubList.getAllClubs(paging, nomClub,idUser);
    }
    @GetMapping("/club/{uuid}")
    @Operation(summary = "Get club  details by UUID.")
    public SuccessResponse<ClubDetailsResponse> getDetails(@PathVariable("uuid") String uuid) {
        return clubDetails.get(uuid);
    }
}
