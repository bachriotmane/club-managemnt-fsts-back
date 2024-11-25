package club.management.club.features.services.clubs;

import club.management.club.features.dto.requests.ClubEditRequest;
import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.dto.responses.ClubSimpleDTO;
import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import club.management.club.shared.dtos.SuccessResponse;

import java.util.List;

public interface ClubService {
    Club findById(String Id);
    Club save(Club club);
    List<Club> getNotJoinedClubs(String studentId);

    List<Club> findClubsWhereUserIsAdmin(String email);
    SuccessResponse<Boolean>  deleteClub(String uuid);
    SuccessResponse<ClubDetailsResponse> editClub(ClubEditRequest clubEditRequest,String uuid);

    void deleteById(String id);

    void accepterClub(String id);
}
