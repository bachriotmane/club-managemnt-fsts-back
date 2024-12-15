package club.management.club.features.dto.responses;

import club.management.club.features.enums.ReactionType;

import java.io.Serializable;

public record ReactionStatusResponse(
        ReactionCounts reactionCounts,
        ReactionType userReaction
)
implements Serializable {
}
