package club.management.club.features.services.clubs;

import club.management.club.features.dto.responses.ClubListMembersResponse;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.shared.dtos.ListSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubListMembers {
    private  final ClubRepository clubRepository;

    public ListSuccessResponse<ClubListMembersResponse> getAllMembers(PageRequest paging, String studentName, String uuid) {
        return null;
    }
}

