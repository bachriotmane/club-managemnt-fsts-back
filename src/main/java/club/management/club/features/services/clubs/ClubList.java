package club.management.club.features.services.clubs;

import club.management.club.features.Specifications.ClubSpecifications;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.dto.responses.ClubListResponse;
import club.management.club.features.entities.Club;
import club.management.club.features.repositories.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubList {
    private final ClubRepository clubRepository;

    public ListSuccessResponse<ClubListResponse> getAllClubs(Pageable pageable, String nomClub,String idStudent) {
        var spec = ClubSpecifications.withNom(nomClub)
                .and(ClubSpecifications.withStudentId(idStudent));
        var clubPage = clubRepository.findAll(spec, pageable);

        var serviceResponses = getData(clubPage);
        return new ListSuccessResponse<>(
                serviceResponses,
                clubPage.getTotalElements(),
                clubPage.getTotalPages(),
                clubPage.hasNext()
        );
    }

    private Set<ClubListResponse> getData(Page<Club> clubPage) {
        return clubPage.getContent().stream()
                .map(club -> new ClubListResponse(
                        club.getId(),
                        club.getNom(),
                        club.getDescription(),
                        club.getCreatedAt(),
                        club.getLogo(),
                        club.getInstagramme()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ListSuccessResponse<ClubListResponse> getClubsWhereUserAdmin(Pageable pageable, String uid) {
        var spec = ClubSpecifications.withAdminOrModeratorRole(uid);
        var clubPage = clubRepository.findAll(spec, pageable);
        var serviceResponses = getData(clubPage);
        return new ListSuccessResponse<>(
                serviceResponses,
                clubPage.getTotalElements(),
                clubPage.getTotalPages(),
                clubPage.hasNext()
        );
    }
}
