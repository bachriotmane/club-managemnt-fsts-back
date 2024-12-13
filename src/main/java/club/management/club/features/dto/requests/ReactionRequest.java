package club.management.club.features.dto.requests;

import club.management.club.features.enums.ReactionType;

import java.io.Serializable;

public record ReactionRequest(
         String publicationId,
         ReactionType type
)  implements Serializable {

}
