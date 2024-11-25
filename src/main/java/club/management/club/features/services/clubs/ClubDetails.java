package club.management.club.features.services.clubs;

import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.entities.Demande;
import club.management.club.features.mappers.ClubMapper;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.repositories.DemandeRepository;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubDetails {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final JwtTokenService jwtTokenService;
    private final IntegrationRepository integrationRepository;
    private final DemandeRepository demandeRepository;

    public SuccessResponse<ClubDetailsResponse> get(Authentication authentication, String uuid) {
        var club = clubRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.CLUB_NOT_FOUND));

        var uuidUser = jwtTokenService.getUserId(authentication);

        String roleName = null;
        String statusDemande = "";

        var demandeOpt = demandeRepository.findIntegrationDemandeByEtudiantIdAndClubId(uuidUser, club.getId());
        if (demandeOpt.isPresent()) {
            var demande = demandeOpt.get();
            statusDemande = demande.getStatutDemande().toString();

            var integrationOpt = integrationRepository.findByEtudiantIdAndClubId(uuidUser, club.getId());
            if (integrationOpt.isPresent()) {
                var integration = integrationOpt.get();
                roleName = integration.getMemberRole().toString();
            }
        }

        var etudiantDemandeur = demandeRepository.findEtudiantDemandeurbyClub(club);
        var nomFondateur = (etudiantDemandeur != null) ?
                etudiantDemandeur.getFirstName() + " " + etudiantDemandeur.getLastName() :
                "Non spécifié";

        var clubDetailsResponse = clubMapper.toClubDetailsResponse(club)
                .withUpdates(statusDemande, roleName, nomFondateur);

        return new SuccessResponse<>(clubDetailsResponse);
    }


}
