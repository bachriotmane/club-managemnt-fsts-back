package club.management.club.features.services.clubs;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.dto.responses.ClubListResponse;
import club.management.club.features.entities.Club;
import club.management.club.features.repositories.ClubRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.stream.Collectors;

import club.management.club.features.Specifications.ClubSpecifications;
import club.management.club.features.services.auths.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClubList {
    private final ClubRepository clubRepository;
    private final JwtTokenService jwtTokenService;
    public ListSuccessResponse<ClubListResponse> getAllClubs(Authentication authentication,
                                                             Pageable pageable,
                                                             String nomClub,
                                                             boolean isMyClubs
    ) {
        var idStudent = isMyClubs ? jwtTokenService.getUserId(authentication) : null;
        var spec = ClubSpecifications.withNom(nomClub)
                . and( !isMyClubs ? ClubSpecifications.withStudentId(idStudent) : ClubSpecifications.withStudentIdForMyClub(idStudent))
                .and(ClubSpecifications.withIsValid(true));

//        if(isMyClubs)  spec.and(ClubSpecifications.withStudentIdForMyClub(idStudent));
//        else           spec.and(ClubSpecifications.withStudentId(idStudent));


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
                        club.getLogo()!= null ? club.getLogo().getId() : null,
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
    
    
    public ListSuccessResponse<ClubListResponse> getCarouselClubs(int limit) {
        var pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());

        var clubPage = clubRepository.findAll(pageable);

        var serviceResponses = clubPage.getContent().stream()
                .map(club -> new ClubListResponse(
                        club.getId(),
                        club.getNom(),
                        club.getDescription(),
                        club.getCreatedAt(),
                        club.getLogo()!= null ? club.getLogo().getId() : null,
                        club.getInstagramme()
                ))
                .collect(Collectors.toSet());

        
        return new ListSuccessResponse<>(
                serviceResponses,
                clubPage.getTotalElements(),
                1,
                false
        );
    }
}

