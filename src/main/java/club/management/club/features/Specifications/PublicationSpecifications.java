package club.management.club.features.Specifications;


import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Integration;
import club.management.club.features.entities.Publication;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PublicationSpecifications {
    public static Specification<Publication> withTitle(String title) {
        return (root, query, builder) ->
                (title == null || title.isEmpty())
                        ? null
                        : builder.like(root.get("title"), "%" + title + "%");
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
}