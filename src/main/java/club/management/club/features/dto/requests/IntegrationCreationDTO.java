package club.management.club.features.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IntegrationCreationDTO(
        @NotEmpty(message = "une motivation est necessaire")
        @NotNull(message = "une motivation est necessaire")
        String motivation
) {
}
