package club.management.club.features.services.reaction;

import club.management.club.features.dto.requests.ReactionRequest;
import club.management.club.features.dto.responses.ReactionStatusResponse;
import club.management.club.shared.dtos.SuccessResponse;
import org.springframework.security.core.Authentication;

public interface ReactionService {
    SuccessResponse<Integer> addReaction(Authentication authentication, ReactionRequest reactionRequest) ;

    SuccessResponse<String> deleteReaction(Authentication authentication, String publicationId);

    SuccessResponse<ReactionStatusResponse> getReactionsStatus(Authentication authentication, String publicationId);
}
