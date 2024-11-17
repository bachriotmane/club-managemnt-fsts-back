package club.management.club.features.services.clubs;

import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.mappers.ClubMapper;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ClubDetails {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    public SuccessResponse<ClubDetailsResponse> get(String uuid) {
        var club = clubRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.CLUB_NOT_FOUND));

        ClubDetailsResponse clubDetailsResponse = clubMapper.toClubDetailsResponse(club);

        return new SuccessResponse<>(clubDetailsResponse);
    }
}
