package club.management.club.features.Specifications;


import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Integration;
import club.management.club.features.entities.Publication;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PublicationSpecifications {
    public static Specification<Publication> withTitle(String title) {
        return (root, query, builder) ->
                (title == null || title.isEmpty())
                        ? null
                        : builder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Publication> withPublicStatus(Boolean isPublic) {
        return (root, query, builder) ->
                isPublic == null
                        ? null
                        : builder.equal(root.get("isPublic"), isPublic);
    }


    public static Specification<Publication> withDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, builder) -> {
            if (fromDate == null && toDate == null) {
                return null;
            } else if (fromDate != null && toDate != null) {
                return builder.between(root.get("date"), fromDate, toDate);
            } else if (fromDate != null) {
                return builder.greaterThanOrEqualTo(root.get("date"), fromDate);
            } else {
                return builder.lessThanOrEqualTo(root.get("date"), toDate);
            }
        };
    }

    public static Specification<Publication> privatePublicationsForUser(String etudiantId) {
        return (root, query, builder) -> {
            if (etudiantId == null || etudiantId.isEmpty()) {
                return null;
            }

            // Join Publication -> Club
            Join<Publication, Club> clubJoin = root.join("club", JoinType.INNER);

            // Join Club -> Integration
            Join<Club, Integration> integrationJoin = clubJoin.join("integrations", JoinType.INNER);

            // Join Integration -> Etudiant
            Join<Integration, Etudiant> etudiantJoin = integrationJoin.join("etudiant", JoinType.INNER);

            // Filter for private publications
            Predicate isPrivate = builder.isFalse(root.get("isPublic"));

            // Filter for publications where the student is a member of the club
            Predicate isMember = builder.equal(etudiantJoin.get("id"), etudiantId);

            return builder.and(isPrivate, isMember);
        };
    }

}