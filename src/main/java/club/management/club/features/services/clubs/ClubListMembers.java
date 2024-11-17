package club.management.club.features.services.clubs;

import club.management.club.features.Specifications.IntegrationSpecifications;
import club.management.club.features.dto.responses.ClubListMembersResponse;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Historique;
import club.management.club.features.entities.Integration;
import club.management.club.features.enums.StatutDemande;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.shared.dtos.ListSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubListMembers {

    private final IntegrationRepository integrationRepository;

    public ListSuccessResponse<ClubListMembersResponse> getAllMembers(Pageable paging, String studentName, String clubUuid) {
        var spec = IntegrationSpecifications.withStudentNameAndClubUuid(studentName, clubUuid);

        var integrations = integrationRepository.findAll(spec, paging);

        var members = getData(integrations);

        return new ListSuccessResponse<>(
                members,
                integrations.getTotalElements(),
                integrations.getTotalPages(),
                integrations.hasNext()
        );
    }

    private Set<ClubListMembersResponse> getData(Page<Integration> integrations) {

        return integrations.getContent().stream()
                .map(i -> new ClubListMembersResponse(
                        i.getEtudiant().getId(),
                        i.getEtudiant().getFirstName(),
                        i.getEtudiant().getLastName(),
                        i.getEtudiant().getImgProfile(),
                        getIntegrationDate(i.getEtudiant()),
                        i.getEtudiant().getFiliere(),
                        i.getMemberRole().name()
                ))
                .collect(Collectors.toSet());
    }



}