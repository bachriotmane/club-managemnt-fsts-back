package club.management.club.features.controllers;

import club.management.club.features.dto.requests.ReactionRequest;
import club.management.club.features.dto.responses.ReactionStatusResponse;
import club.management.club.features.services.reaction.ReactionService;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Reactions")
@AllArgsConstructor
@Tag(name = "Reactions API")
@SecurityRequirement(name = "Bearer Authentication")
public class ReactionController {
    private  final ReactionService reactionService;

    @PostMapping
    @Operation(summary = "Ajouter une réaction à une publication")
    public SuccessResponse<Integer> addReaction(Authentication authentication,
                                                @RequestBody ReactionRequest reactionRequest)
    {
        return reactionService.addReaction(authentication,reactionRequest);
    }
    @DeleteMapping("/{publicationId}")
    @Operation(summary = "Retirer une réaction d'une publication")
    public SuccessResponse<String> deleteReaction(Authentication authentication,
                                                  @PathVariable String publicationId) {
        return reactionService.deleteReaction(authentication, publicationId);
    }
    @GetMapping("/count/{publicationId}")
    @Operation(summary = "Afficher le nombre total de réactions et l'état de la réaction de l'utilisateur connecté")
    public SuccessResponse<ReactionStatusResponse> getReactionsStatus(Authentication authentication,
                                                                      @PathVariable String publicationId) {
        return reactionService.getReactionsStatus(authentication, publicationId);
    }
}
