package club.management.club.features.dto.responses;

import java.time.LocalDateTime;

public record CommentResponse(
        String id,
        String content,
        String firstName,
        String lastName,
        String createdAt,
        boolean isAuthorizedDelete
) {
}
