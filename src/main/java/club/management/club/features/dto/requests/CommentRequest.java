package club.management.club.features.dto.requests;

import java.io.Serializable;

public record CommentRequest(
         String publicationId,
         String content
)
implements Serializable {
}
