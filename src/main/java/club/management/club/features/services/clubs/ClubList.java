package club.management.club.features.services.clubs;
import club.management.club.features.entities.User;
import club.management.club.features.repositories.UserRepo;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubList {
    private final ClubRepository clubRepository;
    private final JwtTokenService jwtTokenService;
    private final UserRepo userRepo;

    public ListSuccessResponse<ClubListResponse> getAllClubs(Authentication authentication,
                                                             Pageable pageable,
                                                             String nomClub,
                                                             boolean isMyClubs,
                                                                String principalEmail

    ) {
        var  idUser = jwtTokenService.getUserId(authentication);
        var idStudent = isMyClubs ?  idUser : null;
        var  isAdmin = userRepo.findById(idUser)
                .map(user -> user.getAuthorities().stream()
                        .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getName())))
                .orElse(false);
        var spec = ClubSpecifications.withNom(nomClub)
                .and(ClubSpecifications.withIsValid(true));

         if(isMyClubs) {
             spec = spec.and(ClubSpecifications.withStudentIdForMyClub(idStudent))
                     .and(ClubSpecifications.withoutBlockedClubs());
         }
         if( !isAdmin){
             spec = spec.and( ClubSpecifications.withStudentId(idStudent))
                     .and(ClubSpecifications.withoutBlockedClubs());
         }

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

       return  clubPage.getContent().stream()
                .map(club -> new ClubListResponse(
                        club.getId(),
                        club.getNom(),
                        club.getDescription(),
                        club.getCreatedAt(),
                        club.getLogo()!= null ? club.getLogo().getId() : null,
                        club.getInstagramme(),
                        club.isBlocked()
                )).collect(Collectors.toSet());
    }

    public ListSuccessResponse<ClubListResponse> getClubsWhereUserAdmin(Pageable pageable, String uid) {
        var spec = ClubSpecifications.withAdminOrModeratorRole(uid)
                .and(ClubSpecifications.withoutBlockedClubs());
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

        Specification<Club> specification = ClubSpecifications.withIsValid(true);
        var clubPage = clubRepository.findAll(specification, pageable);

        var serviceResponses = clubPage.getContent().stream()
                .map(club -> new ClubListResponse(
                        club.getId(),
                        club.getNom(),
                        club.getDescription(),
                        club.getCreatedAt(),
                        club.getLogo()!= null ? club.getLogo().getId() : null,
                        club.getInstagramme(),
                        club.isBlocked()

                ))
                .filter(club -> !club.isBlocked())
                .collect(Collectors.toSet());

        
        return new ListSuccessResponse<>(
                serviceResponses,
                clubPage.getTotalElements(),
                1,
                false
        );
    }
}

