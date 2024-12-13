package club.management.club.features.services.comment.impl;

import club.management.club.features.dto.requests.CommentRequest;
import club.management.club.features.dto.responses.CommentResponse;
import club.management.club.features.entities.Comment;
import club.management.club.features.repositories.CommentRepository;
import club.management.club.features.repositories.PublicationRepository;
import club.management.club.features.repositories.UserRepo;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.comment.CommentService;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final JwtTokenService jwtTokenService;
    private final CommentRepository commentRepository;
    private final UserRepo userRepository;
    private final PublicationRepository publicationRepository;

    @Override
    public SuccessResponse<CommentResponse> addComment(Authentication authentication, CommentRequest commentRequest) {
        var userId = jwtTokenService.getUserId(authentication);

        var publication = publicationRepository.findById(commentRequest.publicationId())
                .orElseThrow(() -> new BadRequestException(ValidationConstants.PUBLICATION_NOT_FOUND));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.USER_NOT_FOUND));

        var comment = Comment.builder()
                .publication(publication)
                .user(user)
                .content(commentRequest.content())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        CommentResponse response = new CommentResponse(
                comment.getId(),
                comment.getContent(),
                user.getFirstName(),
                user.getLastName(),
                comment.getCreatedAt().toString(),
                comment.getUser().getId().equals(userId)
        );

        return new SuccessResponse<>(response);
    }


    @Override
    public SuccessResponse<String> deleteComment(Authentication authentication, String commentId) {
        var userId = jwtTokenService.getUserId(authentication);

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.COMMENT_NOT_EXIST));

        if (!comment.getUser().getId().equals(userId)) {
            throw new BadRequestException(ValidationConstants.UNAUTHORIZED_ACTION);
        }

        commentRepository.delete(comment);

        return new SuccessResponse<>("Comment removed successfully");
    }


    @Override
    public List<CommentResponse> getCommentsByPublication(Authentication authentication,String publicationId) {
        var userId = jwtTokenService.getUserId(authentication);

        return commentRepository.findByPublicationId(publicationId).stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getFirstName(),
                        comment.getUser().getLastName(),
                        comment.getCreatedAt().toString(),
                        comment.getUser().getId().equals(userId)
                ))
                .collect(Collectors.toList());
    }
}
