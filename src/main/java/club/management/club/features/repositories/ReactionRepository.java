package club.management.club.features.repositories;

import club.management.club.features.entities.Reaction;
import club.management.club.features.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, String> {

    long countByPublicationId(String publicationId);

    void deleteByPublicationIdAndUserId(String publicationId, String userId);

    @Query("SELECT r.type FROM Reaction r WHERE r.publication.id = :publicationId AND r.user.id = :userId")
    Optional<ReactionType> findReactionTypeByPublicationIdAndUserId(@Param("publicationId") String publicationId, @Param("userId") String userId);

    @Query("SELECT r FROM Reaction r WHERE r.publication.id = :publicationId AND r.user.id = :userId")
    Optional<Reaction> findByPublicationIdAndUserId(@Param("publicationId") String publicationId, @Param("userId") String userId);

    long countByPublicationIdAndType(String publicationId, ReactionType type);
}
