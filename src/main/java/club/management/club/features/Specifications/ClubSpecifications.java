package club.management.club.features.Specifications;

import club.management.club.features.entities.Club;
import org.springframework.data.jpa.domain.Specification;

public class ClubSpecifications {
    public static Specification<Club> withNom(String nom) {
        return (root, query, builder) ->
                (nom == null || nom.isEmpty())
                        ? null
                        : builder.like(root.get("nom"), nom + "%");
    }
}
