package club.management.club.features.services.clubs;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.dto.responses.ClubListResponse;
import club.management.club.features.entities.Club;
import club.management.club.features.repositories.ClubRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.stream.Collectors;
import java.util.List;

import club.management.club.features.Specifications.ClubSpecifications;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.features.dto.responses.ClubListResponse;
import club.management.club.features.entities.Club;
import club.management.club.features.repositories.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    
    public ListSuccessResponse<ClubListResponse> getCarouselClubs(int limit) {
        // Créer un Pageable pour limiter les résultats
        var pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());

        // Obtenir les clubs depuis le repository
        var clubPage = clubRepository.findAll(pageable);

        // Mapper les entités Club en ClubListResponse (DTO)
        var serviceResponses = clubPage.getContent().stream()
                .map(club -> new ClubListResponse(
                        club.getId(),
                        club.getNom(),
                        club.getDescription(),
                        club.getCreatedAt(),
                        club.getLogo(),
                        club.getInstagramme()
                ))
                .collect(Collectors.toSet());

        
        return new ListSuccessResponse<>(
                serviceResponses,
                clubPage.getTotalElements(),
                1, // Vous pourriez calculer un totalPages différent si nécessaire
                false // Supposons qu'il n'y a pas de "page suivante" dans ce cas précis
        );
    }
}

