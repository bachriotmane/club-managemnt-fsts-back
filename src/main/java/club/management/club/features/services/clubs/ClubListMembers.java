package club.management.club.features.services.clubs;

import club.management.club.features.Specifications.ClubSpecifications;
import club.management.club.features.Specifications.IntegrationSpecifications;
import club.management.club.features.dto.responses.ClubListMembersResponse;
import club.management.club.features.dto.responses.MembersListDTO;
import club.management.club.features.dto.responses.UserRolesInsideClubResponse;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Integration;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubListMembers {

    private final IntegrationRepository integrationRepository;
    private final ClubRepository clubRepository;

    public SuccessResponse<ClubListMembersResponse> getAllMembers(Pageable paging, String studentName, String clubUuid) {

        var club = clubRepository.findById(clubUuid)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.CLUB_NOT_FOUND));

        var spec = IntegrationSpecifications.withStudentNameAndClubUuid(studentName, clubUuid)
                .and(IntegrationSpecifications.withIsValid(true));

        var integrations = integrationRepository.findAll(spec, paging);

        var members = getData(integrations);

        return new SuccessResponse<>(new ClubListMembersResponse(
                club.getNom(),
                club.getLogo()!= null ? club.getLogo().getId() : null,
                (int) club.getIntegrations().stream().filter(Integration::isValid).count(),
                new ListSuccessResponse<>(
                        members,
                        integrations.getTotalElements(),
                        integrations.getTotalPages(),
                        integrations.hasNext()
                )
        ));
    }

    private Set<MembersListDTO> getData(Page<Integration> integrations) {
        return integrations.getContent().stream()
                .map(i -> new MembersListDTO(
                        i.getId(),
                        i.getEtudiant().getId(),
                        i.getEtudiant().getFirstName(),
                        i.getEtudiant().getLastName(),
                        i.getEtudiant().getImgProfile()!= null ? i.getEtudiant().getImgProfile().getId() : null,
                        i.getIntegrationDate(),
                        i.getEtudiant().getFiliere(),
                        i.getRoleName(),
                        i.getMemberRole()
                ))
                .collect(Collectors.toSet());
    }

    public List<UserRolesInsideClubResponse> userRolesInsideClubResponse(String userId) {
        // Find all clubs where user is admin || moderateur
        var integrations = integrationRepository.findAllByEtudiantId(userId);
        return integrations.stream()
                .map(i -> new UserRolesInsideClubResponse(
                        i.getClub().getNom(),
                        i.getClub().getId(),
                        i.getEtudiant().getId(),
                        i.getMemberRole().name()
                ))
                .collect(Collectors.toList());
    }
}
