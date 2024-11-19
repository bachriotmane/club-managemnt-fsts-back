package club.management.club.features.services.clubs;

import club.management.club.features.dto.responses.ClubSimpleDTO;
import club.management.club.features.entities.Club;

import java.util.List;

public interface ClubService {
    Club findById(String Id);
    Club save(Club club);
    List<Club> getNotJoinedClubs(String studentId);
}
