package club.management.club.features.controllers;

import club.management.club.features.dto.requests.CommentRequest;
import club.management.club.features.dto.responses.CommentResponse;
import club.management.club.features.services.comment.CommentService;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Comments")
@AllArgsConstructor
@Tag(name = "Comments API")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {
    private  final CommentService commentService;

    @PostMapping
    public SuccessResponse<CommentResponse> addComment(
            Authentication authentication,
            @RequestBody CommentRequest commentRequest
    ) {
        return commentService.addComment(authentication, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public SuccessResponse<String> deleteComment(
            Authentication authentication,
            @PathVariable String commentId
    ) {
        return commentService.deleteComment(authentication, commentId);
    }

    @GetMapping("/publication/{publicationId}")
    public List<CommentResponse> getCommentsByPublication(
            Authentication authentication,
            @PathVariable String publicationId
    ) {
        return commentService.getCommentsByPublication(authentication,publicationId);
    }
}
