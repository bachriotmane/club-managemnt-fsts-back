package club.management.club.features.dto.responses;

import club.management.club.features.enums.ReactionType;

import java.io.Serializable;

public record ReactionStatusResponse(
        int totalReactions,
        ReactionType userReaction
)
implements Serializable {
}
