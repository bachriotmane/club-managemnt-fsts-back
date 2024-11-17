package club.management.club.features.dto.requests;

public record EventRequest(
        Integer page,
        Integer size,
        String keyword,
        String fromDate,
        String toDate,
        String userId
) {
    public EventRequest {
        if(page == null || page < 0) page = 0;
        if(size == null || size <= 0) size = 10;
    }
}
