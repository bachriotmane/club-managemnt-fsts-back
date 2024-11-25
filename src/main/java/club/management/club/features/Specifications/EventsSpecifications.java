package club.management.club.features.Specifications;


import club.management.club.features.entities.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventsSpecifications {
    public static Specification<Evenement> withTitle(String title) {
        return (root, query, builder) ->
                (title == null || title.isEmpty())
                        ? null
                        : builder.like(root.get("nom"), "%" + title + "%");
    }

    public static Specification<Evenement> isValid() {
        return (root, query, builder) ->
                builder.isTrue(root.get("isValid"));
    }


    public static Specification<Evenement> withDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
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