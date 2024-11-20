package club.management.club.features.Specifications;

import club.management.club.features.entities.Demande;
import club.management.club.features.enums.TypeDemande;
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
}
