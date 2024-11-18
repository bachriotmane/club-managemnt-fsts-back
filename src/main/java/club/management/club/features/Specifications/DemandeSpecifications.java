package club.management.club.features.Specifications;

import club.management.club.features.entities.Demande;
import club.management.club.features.enums.TypeDemande;
import org.springframework.data.jpa.domain.Specification;

public class DemandeSpecifications {
    public static Specification<Demande> withType(TypeDemande typeDemande) {
        return (root, query, builder) ->
                typeDemande == null ? null : builder.equal(root.get("type"), typeDemande);
    }
}
