package club.management.club.features.dto.requests;

public record PublicationsRequest(
        Integer page,
        Integer size,
        String keyword,
        Boolean isPublic,
        String fromDate,
        String toDate,
        Long userId
) {
    public PublicationsRequest {
        if(page == null || page < 0) page = 0;
        if(size == null || size <= 0) size = 10;
        if(isPublic == null) isPublic = false;
    }
}
