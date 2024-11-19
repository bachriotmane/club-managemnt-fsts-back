package club.management.club.features.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record ClubCreationDTO(
        @NotEmpty(message = "Le nom du Club necessaire")
        @NotNull(message = "le nom du Club necessaire")
        String nomClub,
        @NotEmpty(message = "svp ajouter une description")
        @NotNull(message = "svp ajouter une description")
        String description,
        String instagram,
        @NotEmpty(message = "svp ajouter au moins une activite")
        @NotNull(message = "svp ajouter au moins une activite")
        List<String> activities
) implements Serializable {
}