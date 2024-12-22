package club.management.club.features.mappers;

import club.management.club.features.dto.requests.UserEditRequest;
import club.management.club.features.dto.responses.EtudiantDto;
import club.management.club.features.entities.Authority;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.repositories.AuthorityRepo;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.shared.Constants.Roles;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class EtudiantMapper {

    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;
   private final AuthorityRepo authorityRepository;
    @Value("${application.security.password-regex}")
    private String passwordRegex;
    @Value("${email.domain:.fst@uhp.ac.ma}")
    private String emailDomain;
    private final JwtTokenService jwtTokenService;


    public EtudiantDto toEtudiantDto(Etudiant etudiant) {
        return new EtudiantDto(
            etudiant.getId(),
            etudiant.getFirstName(),
            etudiant.getLastName(),
            etudiant.getEmail(),
            etudiant.getCne(),
            etudiant.getFiliere(),
            etudiant.getFacebook(),
            etudiant.getInstagram(),
            etudiant.getWhatsapp(),
            etudiant.getImgProfile() != null ? etudiant.getImgProfile().getId() : null,
            etudiant.getImgCover() != null ? etudiant.getImgCover().getId() : null,
            clubRepository.findByIntegrationsMemberRoleAndIntegrationsEtudiantId(
                    MemberRole.MEMBER,etudiant.getId()).stream().map(Club::getNom).toList(),
            clubRepository.findByIntegrationsMemberRoleAndIntegrationsEtudiantId(
                    MemberRole.ADMIN,etudiant.getId()).stream().map(club -> club.getNom()).toList()
        );
    }

    public Etudiant toEtudiant(UserEditRequest etudiantEditRequest, Etudiant existingEtudiant, Authentication authentication) {
        Etudiant etudiant = existingEtudiant;

        if (etudiantEditRequest.firstName() != null && !etudiantEditRequest.firstName().isEmpty()) {
            etudiant.setFirstName(etudiantEditRequest.firstName());
        }

        if (etudiantEditRequest.lastName() != null && !etudiantEditRequest.lastName().isEmpty()) {
            etudiant.setLastName(etudiantEditRequest.lastName());
        }

        if (etudiantEditRequest.email() != null && !etudiantEditRequest.email().isEmpty()) {
            if (!isEmailValid(etudiantEditRequest.email())) {
                throw new BadRequestException(ValidationConstants.EMAIL_FORMAT_INVALID);
            }
            etudiant.setEmail(etudiantEditRequest.email());
        }

        if (etudiantEditRequest.cne() != null && !etudiantEditRequest.cne().isEmpty()) {
            etudiant.setCne(etudiantEditRequest.cne());
        }

        if (etudiantEditRequest.cin() != null && !etudiantEditRequest.cin().isEmpty()) {
            etudiant.setCin(etudiantEditRequest.cin());
        }

        if (etudiantEditRequest.filiere() != null && !etudiantEditRequest.filiere().isEmpty()) {
            etudiant.setFiliere(etudiantEditRequest.filiere());
        }
        etudiant.setAccountLocked(etudiantEditRequest.accountLocked());

        if (etudiantEditRequest.password() != null && !etudiantEditRequest.password().isEmpty()) {
            if (!isPasswordValid(etudiantEditRequest.password())) {
                throw new BadRequestException(ValidationConstants.PASSWORD_FORMAT_INVALID);
            }
            etudiant.setPassword(passwordEncoder.encode(etudiantEditRequest.password()));
        }

        if (etudiantEditRequest.role() != null) {
            String role = etudiantEditRequest.role();
            var userId = jwtTokenService.getUserId(authentication);
            if (userId.equals(etudiant.getId()) && !etudiantEditRequest.role().equals(Roles.ROLE_SUPERADMIN)) {
                throw new BadRequestException(ValidationConstants.NOT_AUTHORIZED_CHANGE_YOUR_ROLE_CONTACT_SUPPORT);
            }
            if (Roles.ROLE_ADMIN.equals(role) || Roles.ROLE_SUPERADMIN.equals(role)) {
                Authority authority = authorityRepository.findByName(role);
                boolean isCurrentUser = authority.getUsers().stream()
                        .anyMatch(user -> user.getId().equals(etudiant.getId()));
                if (!isCurrentUser && !authority.getUsers().isEmpty()) {
                    String errorMessage;

                    if (Roles.ROLE_SUPERADMIN.equals(role)) {
                        errorMessage = ValidationConstants.ROLE_SUPERADMIN_ALREADY_ASSIGNED;
                    } else {
                        errorMessage = ValidationConstants.ROLE_ADMIN_ALREADY_ASSIGNED;
                    }

                    throw new BadRequestException(errorMessage);
                }
            }

            Authority authority = authorityRepository.findByName(role);
            if (authority != null) {
                etudiant.setAuthorities(new HashSet<>());
                Set<Authority> authorities = new HashSet<>();
                authorities.add(authority);
                etudiant.setAuthorities(authorities);
            }
            else {
                throw new BadRequestException("Rôle introuvable.");
            }
        }



        return etudiant;
    }

    private boolean isEmailValid(String email) {
        if (email == null || emailDomain == null) {
            return false;
        }

        if (!email.endsWith(emailDomain)) {
            return false;
        }

        String localPart = email.substring(0, email.length() - emailDomain.length());
        int firstDotIndex = localPart.indexOf('.');

        if (firstDotIndex == -1 || firstDotIndex == 0 || firstDotIndex == localPart.length() - 1) {
            return false;
        }

        String subPart = localPart.substring(firstDotIndex + 1);
        int secondDotIndex = subPart.indexOf('.');
        if (secondDotIndex != -1) {
            subPart = subPart.substring(0, secondDotIndex);
        }

        return subPart.length() == 3;
    }
    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8 || passwordRegex == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
