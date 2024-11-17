package club.management.club.features.services.clubs;

import club.management.club.features.Specifications.IntegrationSpecifications;
import club.management.club.features.dto.responses.ClubListMembersResponse;
import club.management.club.features.dto.responses.MembersListDTO;
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

        var spec = IntegrationSpecifications.withStudentNameAndClubUuid(studentName, clubUuid);
        var integrations = integrationRepository.findAll(spec, paging);

        var members = getData(integrations);

        return new SuccessResponse<>(new ClubListMembersResponse(
                club.getNom(),
                club.getLogo(),
                club.getIntegrations().size(),
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
                        i.getEtudiant().getId(),
                        i.getEtudiant().getFirstName(),
                        i.getEtudiant().getLastName(),
                        i.getEtudiant().getImgProfile(),
                        i.getIntegrationDate(),
                        i.getEtudiant().getFiliere(),
                        i.getMemberRole().name()
                ))
                .collect(Collectors.toSet());
    }
}
