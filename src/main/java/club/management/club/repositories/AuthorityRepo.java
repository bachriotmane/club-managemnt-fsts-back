package club.management.club.repositories;

import club.management.club.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority,Integer> {

    Optional<Authority> findAuthorityByName(String name);
}
