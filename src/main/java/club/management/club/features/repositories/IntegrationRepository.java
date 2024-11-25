package club.management.club.features.repositories;

import club.management.club.features.entities.Integration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationRepository extends JpaRepository<Integration,String> , JpaSpecificationExecutor<Integration> {
    List<Integration> findAllByEtudiantId(String id);
    Optional<Integration> findByEtudiantIdAndClubId(String etudiantId, String clubUuid);
}
