package club.management.club.features.services.integration.Impl;

import club.management.club.features.dto.responses.MembersListDTO;
import club.management.club.features.entities.Integration;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.integration.IntegrationService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {
    private final IntegrationRepository integrationRepo ;
    private final JwtTokenService jwtTokenService;

    @Override
    public Integration save(Integration integration) {
        return integrationRepo.save(integration);
    }

    @Override
    @Transactional
    public SuccessResponse<MembersListDTO> editRoleStudent(String uuidIntegration, String roleName, MemberRole memberRole) {
        var integration = integrationRepo.findById(uuidIntegration)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.INTEGRATION_NOT_FOUND));

        if (roleName == null || roleName.isBlank()) {
            throw new BadRequestException(ValidationConstants.ROLE_NAME_IS_MANDATORY);
        }

        if (memberRole == null) {
            throw new BadRequestException(ValidationConstants.MEMBER_ROLE_IS_MANDATORY);
        }

        boolean isValidRole = EnumSet.allOf(MemberRole.class).contains(memberRole);
        if (!isValidRole) {
            throw new BadRequestException(ValidationConstants.ROLE_NAME_IS_NOT_VALID);
        }

        integration.setRoleName(roleName);
        integration.setMemberRole(memberRole);

        var updatedIntegration = integrationRepo.save(integration);

        return new SuccessResponse<>(
                new MembersListDTO(
                        updatedIntegration.getId(),
                        updatedIntegration.getEtudiant().getId(),
                        updatedIntegration.getEtudiant().getFirstName(),
                        updatedIntegration.getEtudiant().getLastName(),
                        updatedIntegration.getEtudiant().getImgProfile() != null
                                ? updatedIntegration.getEtudiant().getImgProfile().getId()
                                : null,
                        updatedIntegration.getIntegrationDate(),
                        updatedIntegration.getEtudiant().getFiliere(),
                        updatedIntegration.getRoleName(),
                        updatedIntegration.getMemberRole()
                )
        );
    }

    @Override
    @Transactional
    public SuccessResponse<Boolean> deleteIntegration(Authentication authentication, String uuid) {
        var integration = integrationRepo.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.INTEGRATION_NOT_FOUND));

        var uuidStudent = jwtTokenService.getUserId(authentication);

        if (integration.getEtudiant().getId().equals(uuidStudent)) {
            throw new BadRequestException(ValidationConstants.NOT_AUTHORIZED_TO_DELETE_INTEGRATION);
        }

        if (integration.getMemberRole() == MemberRole.ADMIN) {
            throw new BadRequestException(ValidationConstants.NOT_AUTHORIZED_TO_DELETE_ADMINISTRATEUR);
        }

        integrationRepo.deleteById(uuid);
        return new SuccessResponse<>(true);
    }

}
