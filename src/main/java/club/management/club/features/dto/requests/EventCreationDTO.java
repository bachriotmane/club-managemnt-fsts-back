package club.management.club.features.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventCreationDTO(
        @NotEmpty(message = "Le nom d'evenment est  necessaire")
        @NotNull(message = "Le nom d'evenment est  necessaire")
        String eventName,
        @NotEmpty(message = "une description est  necessaire")
        @NotNull(message = "une description est  necessaire")
        String description,
        @NotEmpty(message = "Localisation est  necessaire")
        @NotNull(message = "Localisation est  necessaire")
        String location,

        LocalDateTime eventDate,
        @NotNull(message = "un montant est  necessaire")
        Double budget
) implements Serializable {
}
