package club.management.club.features.services.comment;

import club.management.club.features.dto.requests.CommentRequest;
import club.management.club.features.dto.responses.CommentResponse;
import club.management.club.shared.dtos.SuccessResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {
    SuccessResponse<CommentResponse> addComment(Authentication authentication, CommentRequest commentRequest);
    SuccessResponse<String> deleteComment(Authentication authentication, String commentId);
    List<CommentResponse> getCommentsByPublication(Authentication authentication,String publicationId);
}
