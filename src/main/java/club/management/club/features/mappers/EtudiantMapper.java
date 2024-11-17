package club.management.club.features.mappers;

import club.management.club.features.dto.responses.EtudiantDto;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.User;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.repositories.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EtudiantMapper {

    private final ClubRepository clubRepository;

    public EtudiantDto toEtudiantDto(Etudiant etudiant) {
        return new EtudiantDto(
            etudiant.getId(),
            etudiant.getFirstName(),
            etudiant.getLastName(),
            etudiant.getEmail(),
            etudiant.getCin(),
            etudiant.getCne(),
            etudiant.getFiliere(),
            etudiant.getFacebook(),
            etudiant.getInstagram(),
            etudiant.getWhatsapp(),
            clubRepository.findByIntegrationsMemberRoleAndIntegrationsEtudiantId(
                    MemberRole.MEMBER,etudiant.getId()).stream().map(Club::getNom).toList(),
            clubRepository.findByIntegrationsMemberRoleAndIntegrationsEtudiantId(
                    MemberRole.ADMIN,etudiant.getId()).stream().map(club -> club.getNom()).toList()
        );
    }
}
