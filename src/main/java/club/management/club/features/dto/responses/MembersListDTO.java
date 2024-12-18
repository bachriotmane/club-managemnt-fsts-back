package club.management.club.features.dto.responses;

import club.management.club.features.enums.MemberRole;

import java.io.Serializable;
import java.util.Date;

public record MembersListDTO(
        String uuidIntegration,
        String uuid,
        String firstName,
        String lastName,
        String imgProfile,
        Date dateIntegration,
        String filiere,
        String roleName,
        MemberRole memberRole,
        boolean deactivate
)implements Serializable {
}
