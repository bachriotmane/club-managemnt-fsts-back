package club.management.club.features.Specifications;

import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Integration;
import club.management.club.features.enums.MemberRole;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;


public class ClubSpecifications {

    public static Specification<Club> withNom(String nom) {
        return (root, query, builder) ->
                (nom == null || nom.isEmpty())
                        ? null
                        : builder.like(root.get("nom"), "%"+ nom + "%");
    }

    public static Specification<Club> withStudentId(String etudiantId) {
        return (root, query, builder) -> {
            if (etudiantId == null || etudiantId.isEmpty()) {
                return null;
            }

            Join<Club, Integration> integrationJoin = root.join("integrations", JoinType.INNER);

            Join<Integration, Etudiant> etudiantJoin = integrationJoin.join("etudiant", JoinType.INNER);

            return builder.equal(etudiantJoin.get("id"), etudiantId);
        };
    }
    public static Specification<Club> withStudentIdForMyClub(String etudiantId) {
        return (root, query, builder) -> {
            if (etudiantId == null || etudiantId.isEmpty()) {
                return null;
            }

            Join<Club, Integration> integrationJoin = root.join("integrations", JoinType.INNER);

            Join<Integration, Etudiant> etudiantJoin = integrationJoin.join("etudiant", JoinType.INNER);

            return builder.and(
                    builder.equal(etudiantJoin.get("id"), etudiantId),
                    builder.isTrue(integrationJoin.get("isValid"))
            );
        };
    }
    public static Specification<Club> withIsValid(Boolean isValid) {
        return (root, query, builder) -> {
            if (isValid == null) {
                return null;
            }
            return builder.equal(root.get("isValid"), isValid);
        };
    }
    public static Specification<Club> withAdminOrModeratorRole(String etudiantId) {
        return (root, query, builder) -> {
            if (etudiantId == null || etudiantId.isEmpty()) {
                return null;
            }
            Join<Club, Integration> integrationJoin = root.join("integrations", JoinType.INNER);
            Join<Integration, Etudiant> etudiantJoin = integrationJoin.join("etudiant", JoinType.INNER);
            return builder.and(
                    builder.equal(etudiantJoin.get("id"), etudiantId),
                    integrationJoin.get("memberRole").in(MemberRole.ADMIN, MemberRole.MODERATEUR)
            );
        };
    }


}
