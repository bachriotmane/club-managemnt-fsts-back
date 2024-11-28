package club.management.club.features.services.clubs.Impl;

import club.management.club.features.dto.requests.ClubEditRequest;
import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.dto.responses.ClubNameDTO;
import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.mappers.ClubMapper;
import club.management.club.features.repositories.*;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final DemandeRepository demandeRepository ;
    private final EventRepository eventRepository;
    private final PublicationRepository publicationRepository;
    private final IntegrationRepository integrationRepository;
    private final JwtTokenService jwtTokenService;
    private  final UserRepo userRepo;

    @Override
    public Club findById(String id) {
        return clubRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Club","Id",id)
        );
    }
    @Override
    public void accepterClub(String id) {
        // Récupérer le club par ID
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trouvé"));

        // Mettre à jour l'état du club (actif)
        club.setValid(true);

        // Sauvegarder le club mis à jour
        clubRepository.save(club);
    }

    @Override
    public List<ClubNameDTO> getAllClubs(Authentication authentication) {
        var idUser = jwtTokenService.getUserId(authentication);
        var studentOpt = userRepo.findById(idUser);
        if (studentOpt.isEmpty()) {
            throw new BadRequestException(ValidationConstants.USER_NOT_FOUND);
        }
        var student = studentOpt.get();

        boolean isUser = student.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_USER".equals(auth.getName()));
        //probleme liste in My  demandes  link club
        if (isUser) {
            var clubsFromDemandes = demandeRepository.findAllByEtudiantDemandeur(student).stream()
                    .map(demande -> new ClubNameDTO(demande.getClub().getNom(), demande.getClub().getId()))
                    .distinct()
                    .toList();

            var clubsAsAdmin = clubRepository.findAll().stream()
                    .filter(club -> club.getIntegrations().stream()
                            .anyMatch(integration -> integration.getEtudiant().equals(student) &&
                                    integration.getMemberRole() == MemberRole.ADMIN))
                    .map(club -> new ClubNameDTO(club.getNom(), club.getId()))
                    .toList();

            return Stream.concat(clubsFromDemandes.stream(), clubsAsAdmin.stream())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            return clubRepository.findAll().stream()
                    .map(club -> new ClubNameDTO(club.getNom(), club.getId()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Club save(Club club) {
        return clubRepository.save(club);
    }

    @Override
    public List<Club> getNotJoinedClubs(String studentId) {
        return clubRepository.findAllClubsWhereUserNotJoined(studentId);
    }

    @Override
    public List<Club> findClubsWhereUserIsAdmin(String email) {
        return clubRepository.findClubsWhereUserIsAdmin(email,MemberRole.ADMIN);
    }


    @Override
    @Transactional
    public SuccessResponse<Boolean> deleteClub(String uuid) {
        var club = clubRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.CLUB_NOT_FOUND));

        demandeRepository.deleteByClubId(uuid);
        integrationRepository.deleteAll(club.getIntegrations());
        publicationRepository.deleteByClubId(uuid);
        eventRepository.deleteByClubId(uuid);
        clubRepository.deleteById(uuid);

        return new SuccessResponse<>(true);
    }


    @Override
    @Transactional
    public SuccessResponse<ClubDetailsResponse> editClub(ClubEditRequest clubEditRequest, String uuid) {
        var oldClub = clubRepository.findById(uuid)
                .orElseThrow(()-> new BadRequestException(ValidationConstants.CLUB_NOT_FOUND));
        var newClub =  clubMapper.toPatch(clubEditRequest,oldClub);
            var clubResponse = clubRepository.save(newClub);

        return new SuccessResponse<>(clubMapper.toClubDetailsResponse(clubResponse));
    }

    @Override
    public void deleteById(String id) {
        if (!clubRepository.existsById(id)) {
            throw new EntityNotFoundException("Club avec ID " + id + " non trouvé");
        }
        clubRepository.deleteById(id);
    }
}
