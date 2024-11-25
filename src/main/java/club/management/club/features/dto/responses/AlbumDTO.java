package club.management.club.features.dto.responses;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AlbumDTO(
        String albumId,
        String location,
        LocalDate date,
        String title,
        List<String> imagesUrl
) {
}
