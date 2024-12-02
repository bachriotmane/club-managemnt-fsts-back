package club.management.club.features.Specifications;

import club.management.club.features.entities.Demande;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.enums.TypeDemande;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class DemandeSpecifications {

    public static Specification<Demande> withType(TypeDemande type) {
        return (root, query, builder) -> {
            if (type == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("type"), type);
        };
    }
    public static Specification<Demande> withNom(String nom) {
        return (root, query, builder) -> {
            if (nom == null || nom.isEmpty()) {
                return null;
            }

            String pattern = "%" + nom.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("etudiantDemandeur").get("firstName")), pattern),
                    builder.like(builder.lower(root.get("etudiantDemandeur").get("lastName")), pattern)
            );
        };
    }


    public static Specification<Demande> withStudentId(String idStudent) {
        return (root, query, builder) -> {
            if (idStudent == null || idStudent.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("etudiantDemandeur").get("id"), idStudent);
        };
    }

    public static Specification<Demande> excludeTypeIntegrationClub() {
        return (root, query, builder) -> builder.notEqual(root.get("type"), TypeDemande.INTEGRATION_CLUB);
    }
    public static Specification<Demande> includeOnlyTypeIntegrationClub() {
        return (root, query, builder) -> builder.equal(root.get("type"), TypeDemande.INTEGRATION_CLUB);
    }

    public static Specification<Demande> withStudentAsAdminInClub(String idStudent) {
        return (root, query, builder) -> {
            if (idStudent == null || idStudent.isEmpty()) {
                return null;
            }

           Join<Object, Object> clubJoin = root.join("club", JoinType.INNER);
            Join<Object, Object> integrationJoin = clubJoin.join("integrations", JoinType.INNER);

            return builder.and(
                    builder.equal(integrationJoin.get("etudiant").get("id"), idStudent),
                    builder.equal(integrationJoin.get("memberRole"), MemberRole.ADMIN),
                    builder.notEqual(root.get("etudiantDemandeur").get("id"),idStudent)
            );
        };
    }

    public static Specification<Demande> withClubId(String uuidClub) {
        return (root, query, builder) -> {
            if (uuidClub == null || uuidClub.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("club").get("id"), uuidClub);
        };
    }

}
