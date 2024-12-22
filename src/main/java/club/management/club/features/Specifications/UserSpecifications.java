package club.management.club.features.Specifications;

import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Authority;
import club.management.club.features.entities.Integration;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecifications {

    public static Specification<Etudiant> withUserName(String userName) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(userName)) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("firstName")),
                                "%" + userName.toLowerCase() + "%"
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("lastName")),
                                "%" + userName.toLowerCase() + "%"
                        )
                );
            }
            return criteriaBuilder.conjunction();
        };
    }


    public static Specification<Etudiant> withCne(String cne) {
        return (root, query, builder) ->
                (cne == null || cne.isEmpty())
                        ? null
                        : builder.like(root.get("cne"), "%"+ cne + "%");
    }


    public static Specification<Etudiant> withCin(String cin) {
        return (root, query, builder) ->
                (cin == null || cin.isEmpty())
                        ? null
                        : builder.like(root.get("cne"), "%"+ cin + "%");
    }



 
    public static Specification<Etudiant> withAuthority(Authority authority) {
        return (root, query, criteriaBuilder) -> {
            if (authority != null) {
                return criteriaBuilder.isMember(authority, root.get("authorities"));
            }
            return criteriaBuilder.conjunction();
        };
    }
    public static Specification<Etudiant> withClubId(String uuidClub) {
        return (root, query, builder) -> {
            if (uuidClub == null || uuidClub.isEmpty()) {
                return null;
            }

            Join<Etudiant, Integration> integrationJoin = root.join("integrations", JoinType.LEFT);

            Predicate clubPredicate = builder.equal(integrationJoin.get("club").get("id"), uuidClub);

            Predicate validPredicate = builder.isTrue(integrationJoin.get("isValid"));

            return builder.and(clubPredicate, validPredicate);
        };
    }


}
