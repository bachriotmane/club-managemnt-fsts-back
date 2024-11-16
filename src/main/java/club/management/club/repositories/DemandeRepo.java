package club.management.club.repositories;

import club.management.club.entities.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeRepo extends JpaRepository<Demande, Long> {

}

