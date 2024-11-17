package club.management.club.features.dto.responses;

import club.management.club.shared.dtos.ListSuccessResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public record ClubListMembersResponse(
        String nomClub,
        String logo,
        Integer nbrStudent,
        ListSuccessResponse<MembersListDTO> membersListDTO
) implements Serializable {
}
