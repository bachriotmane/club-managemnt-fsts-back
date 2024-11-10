package club.management.club.repositories;

import club.management.club.entities.MailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MailTokenRepo extends JpaRepository<MailToken,Integer> {
    Optional<MailToken> findMailTokenByToken(String token);
}
