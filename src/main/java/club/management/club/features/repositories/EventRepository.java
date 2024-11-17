package club.management.club.features.repositories;


import club.management.club.features.entities.Evenement;
import club.management.club.features.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Evenement, String> {
    Page<Evenement> findAll(Specification<Evenement> spec , Pageable pageable);
}
