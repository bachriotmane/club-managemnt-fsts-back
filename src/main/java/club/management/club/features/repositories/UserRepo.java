package club.management.club.features.repositories;

import club.management.club.features.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long id);
}
