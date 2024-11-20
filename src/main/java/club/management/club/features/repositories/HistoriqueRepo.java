package club.management.club.features.repositories;

import club.management.club.features.entities.Historique;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriqueRepo extends JpaRepository<Historique,String> {
}
