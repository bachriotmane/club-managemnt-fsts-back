package club.management.club.features.repositories;

import club.management.club.features.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByPublicationId(String publicationId);

    boolean existsByIdAndUserId(String commentId, String userId);
}
