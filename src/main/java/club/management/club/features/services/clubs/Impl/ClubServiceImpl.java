package club.management.club.features.services.clubs.Impl;

import club.management.club.features.dto.requests.ClubEditRequest;
import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.mappers.ClubMapper;
import club.management.club.features.repositories.*;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final DemandeRepository demandeRepository ;
    private final EventRepository eventRepository;
    private final PublicationRepository publicationRepository;
    private final IntegrationRepository integrationRepository;
    @Override
    public Club findById(String id) {
        return clubRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Club","Id",id)
        );
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

}
