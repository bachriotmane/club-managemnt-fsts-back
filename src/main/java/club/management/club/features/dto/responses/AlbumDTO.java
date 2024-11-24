package club.management.club.features.dto.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AlbumDTO(
        String albumId,
        String location,
        LocalDateTime date,
        String title,
        List<String> imagesUrl
) {
}
