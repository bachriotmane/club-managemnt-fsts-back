package club.management.club.features.services.reaction.impl;

import club.management.club.features.dto.requests.ReactionRequest;
import club.management.club.features.dto.responses.ReactionStatusResponse;
import club.management.club.features.entities.Reaction;
import club.management.club.features.enums.ReactionType;
import club.management.club.features.repositories.UserRepo;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.reaction.ReactionService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.features.repositories.PublicationRepository;
import club.management.club.features.repositories.ReactionRepository;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final JwtTokenService jwtTokenService;
    private final PublicationRepository publicationRepository;
    private final UserRepo userRepository;
    private final ReactionRepository reactionRepository;

    @Override
    public SuccessResponse<Integer> addReaction(Authentication authentication, ReactionRequest reactionRequest) {
        var userId = jwtTokenService.getUserId(authentication);

        var publication = publicationRepository.findById(reactionRequest.publicationId())
                .orElseThrow(() -> new BadRequestException(ValidationConstants.PUBLICATION_NOT_FOUND));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.USER_NOT_FOUND));

        var existingReaction = reactionRepository.findByPublicationIdAndUserId(reactionRequest.publicationId(), userId);

        existingReaction.ifPresent(reactionRepository::delete);

        var reaction = Reaction.builder()
                .publication(publication)
                .user(user)
                .type(reactionRequest.type())
                .createdAt(LocalDateTime.now())
                .build();

        reactionRepository.save(reaction);

        int totalReactions = (int) reactionRepository.countByPublicationId(reactionRequest.publicationId());

        return new SuccessResponse<>(totalReactions);
    }

    @Override
    public SuccessResponse<String> deleteReaction(Authentication authentication, String publicationId) {
        var userId = jwtTokenService.getUserId(authentication);

        reactionRepository.deleteByPublicationIdAndUserId(publicationId, userId);

        return new SuccessResponse<>("Reaction removed successfully");
    }
    @Override
    public SuccessResponse<ReactionStatusResponse> getReactionsStatus(Authentication authentication, String publicationId) {
        int totalReactions = (int) reactionRepository.countByPublicationId(publicationId);

        String userId = jwtTokenService.getUserId(authentication);

        var userReaction = reactionRepository.findReactionTypeByPublicationIdAndUserId(publicationId, userId)
                .orElse(null);

        ReactionStatusResponse response = new ReactionStatusResponse(totalReactions, userReaction);

        return new SuccessResponse<>(response);
    }

}
