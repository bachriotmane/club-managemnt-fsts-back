package club.management.club.features.services.clubs.Impl;

import club.management.club.features.dto.responses.ClubSimpleDTO;
import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.services.clubs.ClubService;
import club.management.club.shared.exceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
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


}
