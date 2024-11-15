package club.management.club.features.repositories;


import club.management.club.features.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, String> {
    Page<Publication> findAll(Specification<Publication> spec , Pageable pageable);
}
