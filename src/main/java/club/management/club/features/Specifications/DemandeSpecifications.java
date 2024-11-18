package club.management.club.features.Specifications;

import club.management.club.features.entities.Demande;
import club.management.club.features.enums.TypeDemande;
import org.springframework.data.jpa.domain.Specification;

public class DemandeSpecifications {

    // Spécification pour filtrer les demandes par type
    public static Specification<Demande> withType(TypeDemande type) {
        return (root, query, builder) -> {
            if (type == null) {
                return builder.conjunction();  // Si aucun type n'est spécifié, on ne filtre pas
            }
            return builder.equal(root.get("type"), type);  // Filtre par le champ 'type' de la demande
        };
    }
}
