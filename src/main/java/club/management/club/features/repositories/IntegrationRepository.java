package club.management.club.features.repositories;

import club.management.club.features.entities.Integration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegrationRepository extends JpaRepository<Integration, String> {
}
