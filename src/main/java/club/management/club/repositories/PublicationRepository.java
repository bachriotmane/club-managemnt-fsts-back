package club.management.club.repositories;

import club.management.club.entities.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, String> {
    
}
