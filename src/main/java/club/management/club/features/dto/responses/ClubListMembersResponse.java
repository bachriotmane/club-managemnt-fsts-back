package club.management.club.features.dto.responses;

import java.io.Serializable;
import java.util.Date;

public record ClubListMembersResponse(
        String uuid,
        String firstName,
        String lastName,
        String imgProfile,
        Date dateIntegration,
        String filiere,
        String role
) implements Serializable {
}
