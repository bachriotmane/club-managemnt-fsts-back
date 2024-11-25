package club.management.club.features.Specifications;

import club.management.club.features.entities.Integration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class IntegrationSpecifications {

    public static Specification<Integration>    withStudentNameAndClubUuid(String studentName, String clubUuid) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (StringUtils.hasText(clubUuid)) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(root.get("club").get("id"), clubUuid)
                );
            }

            if (StringUtils.hasText(studentName)) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("etudiant").get("firstName")),
                                        "%" + studentName.toLowerCase() + "%"
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("etudiant").get("lastName")),
                                        "%" + studentName.toLowerCase() + "%"
                                )
                        )
                );
            }

            return predicates;
        };
    }
}
