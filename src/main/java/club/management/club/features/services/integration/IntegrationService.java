package club.management.club.features.services.integration;

import club.management.club.features.dto.responses.MembersListDTO;
import club.management.club.features.entities.Integration;
import club.management.club.features.enums.MemberRole;
import club.management.club.shared.dtos.SuccessResponse;

public interface IntegrationService {
    Integration save(Integration integration);
    SuccessResponse<MembersListDTO> editRoleStudent(String uuidIntegration , String roleName, MemberRole memberRole);

    SuccessResponse<Boolean> deleteIntegration(String uuid);
}
