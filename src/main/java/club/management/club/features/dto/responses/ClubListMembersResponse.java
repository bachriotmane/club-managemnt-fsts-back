package club.management.club.features.dto.responses;

import club.management.club.shared.dtos.ListSuccessResponse;

import java.io.Serializable;


public record ClubListMembersResponse(
        String nomClub,
        String logo,
        Integer nbrStudent,
        ListSuccessResponse<MembersListDTO> membersListDTO
) implements Serializable {
}
