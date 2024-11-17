package club.management.club.features.repositories;

import club.management.club.features.entities.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DemandeRepository extends JpaRepository<Demande, String>, JpaSpecificationExecutor<Demande> {
    // Pas besoin de méthodes supplémentaires, JpaSpecificationExecutor gère les Specifications.
}

