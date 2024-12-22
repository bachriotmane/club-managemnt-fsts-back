package club.management.club.features.dto.responses;


public record EventClubChartDTO(
        String clubId,
        String clubName,
        int eventsCount
) {
}
