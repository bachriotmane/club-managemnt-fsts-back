package club.management.club.features.controllers;

import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.dto.responses.ClubListMembersResponse;
import club.management.club.features.services.clubs.ClubDetails;
import club.management.club.features.services.clubs.ClubListMembers;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
@Tag(name = "Clubs API")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubsController {

    private final ClubList clubList;
    private final ClubDetails clubDetails;
    private final ClubListMembers clubListMembers;

    @GetMapping
    @Operation(summary = "Get all clubs.")
    public ListSuccessResponse<ClubListResponse> getAllClubs(Authentication authentication,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String nomClub,
                                                             @RequestParam(name = "idMyClubs", required = false) boolean isMyClubs
    ) {
        var paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return clubList.getAllClubs(authentication,paging, nomClub,isMyClubs);
    }
    @GetMapping("/club/{uuid}")
    @Operation(summary = "Get club  details by UUID.")
    public SuccessResponse<ClubDetailsResponse> getDetails(@PathVariable("uuid") String uuid) {
        return clubDetails.get(uuid);
    }

    @GetMapping("/{uuid}/admin")
    @Operation(summary = "Get club where memeber is admin.")
    public ListSuccessResponse<ClubListResponse> getClubAdmin(
            @PathVariable("uuid") String uuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return clubList.getClubsWhereUserAdmin(paging,uuid);
        }
    @GetMapping("/club/{uuid}/members")
    @Operation(summary = "Get all members by UUID.")
    public SuccessResponse<ClubListMembersResponse>  getAllMembers(@PathVariable("uuid") String uuid,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(required = false) String studentName
    ) {

        var paging = PageRequest.of(page, size, Sort.by("integrationDate").descending());
        return clubListMembers.getAllMembers(paging, studentName, uuid);
    }
}
