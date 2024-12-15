package club.management.club.features.dto.responses;


import java.io.Serializable;

public record ReactionCounts(
        int totalReactions,
        int love,
        int like,
        int wow,
        int sad,
        int angry
) implements Serializable {
}
