package club.management.club.features.controllers;

import club.management.club.features.dto.requests.ClubEditRequest;
import club.management.club.features.dto.responses.*;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.mappers.ClubsMapper;
import club.management.club.features.services.clubs.ClubDetails;
import club.management.club.features.services.clubs.ClubListMembers;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.services.clubs.ClubList;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import java.util.stream.Stream;
@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
@Tag(name = "Clubs API")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubsController {

    private final ClubList clubList;
    private final ClubDetails clubDetails;
    private final ClubListMembers clubListMembers;
    private final ClubService clubService;
    private  final ClubsMapper clubsMapper;


    @GetMapping
    @Operation(summary = "Get all clubs.")
    public ListSuccessResponse<ClubListResponse> getAllClubs(Authentication authentication,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String nomClub,
                                                             @RequestParam(required = false) boolean isMyClubs
    ) {
        var paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return clubList.getAllClubs(authentication,paging, nomClub,isMyClubs);
    }
    @GetMapping("/club/{uuid}")
    @Operation(summary = "Get club  details by UUID.")
    public SuccessResponse<ClubDetailsResponse> getDetails(Authentication authentication,
                                                           @PathVariable("uuid") String uuid)
    {
        return clubDetails.get(authentication,uuid);
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
    
    @GetMapping("/home-clubs")
    @Operation(summary = "Get a limited number of clubs for the homepage carousel.")
    public ListSuccessResponse<ClubListResponse> getHomeClubs(
            @RequestParam(defaultValue = "7") int limit) {
        return clubList.getCarouselClubs(limit);
    }

    @GetMapping("/notJoinedClubs")
    public ResponseEntity<List<ClubSimpleDTO>> getAllNotJoinedClubs(Authentication authentication){
        String userEmail = authentication.getPrincipal().toString();

        List<ClubSimpleDTO> clubSimpleDTOS = clubService.getNotJoinedClubs(userEmail)
                .stream().map(clubsMapper::ToClubSimpleDTO).toList();

        return ResponseEntity.ok(clubSimpleDTOS);
    }

    @GetMapping("/adminClub")
    public ResponseEntity<List<ClubSimpleDTO>> getClubsWhereUserIsAdmin(Authentication authentication){
        String userEmail = authentication.getPrincipal().toString();

        List<ClubSimpleDTO> clubSimpleDTOS = clubService.findClubsWhereUserIsAdmin(userEmail)
                .stream().map(clubsMapper::ToClubSimpleDTO).toList();

        return ResponseEntity.ok(clubSimpleDTOS);
    }

    @GetMapping("/{userId}/roles")
    public List<UserRolesInsideClubResponse> getUserRolesInsideClub(@PathVariable String userId){
        return clubListMembers.userRolesInsideClubResponse(userId);
    }
    @Operation(summary = "Delete Club by UUID.")
    @DeleteMapping("/club/delete/{uuid}")
    public  SuccessResponse<Boolean>  deleteClub(@PathVariable String  uuid){
        return clubService.deleteClub(uuid);
    }
    @Operation(summary = "Edit club by UUID.")
    @PatchMapping(path = "/club/{uuid}")
    public  SuccessResponse<ClubDetailsResponse>  editClub(@RequestBody @Valid ClubEditRequest clubCreationDTO, @PathVariable String uuid){
        return  clubService.editClub(clubCreationDTO,uuid);
    }

    @GetMapping("/member-roles")
    public List<String> getMemberRoles() {
        return Stream.of(MemberRole.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClubById(@PathVariable String id) {
        clubService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/accepter")
    public ResponseEntity<String> accepterClub(@PathVariable String id) {
        try {
            // Appel du service pour accepter le club
            clubService.accepterClub(id);
            return ResponseEntity.ok("Club accepté avec succès");
        } catch (Exception e) {
            // Gestion des erreurs
            return ResponseEntity.status(500).body("Erreur lors de l'acceptation du club");
        }
    }
}
