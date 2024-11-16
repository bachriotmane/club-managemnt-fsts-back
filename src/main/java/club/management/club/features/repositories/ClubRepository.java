package club.management.club.features.repositories;

import club.management.club.features.entities.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, String> {
    Page<Club> findAll(Specification<Club> spec , Pageable pageable);
}
