package club.management.club.features.services.clubs.Impl;

import club.management.club.features.dto.requests.ClubEditRequest;
import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.dto.responses.ClubSimpleDTO;
import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.mappers.ClubMapper;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
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
    public SuccessResponse<Boolean> deleteClub(String uuid) {
        var isFind =  clubRepository.existsById(uuid);
        if(! isFind) {
            throw  new BadRequestException(ValidationConstants.CLUB_NOT_FOUND);
        }
        clubRepository.deleteById(uuid);
        return new SuccessResponse<>(true);
    }

    @Override
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
