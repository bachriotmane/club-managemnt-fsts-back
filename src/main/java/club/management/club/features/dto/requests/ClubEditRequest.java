package club.management.club.features.dto.requests;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public record ClubEditRequest(
        @NotEmpty(message = "Le nom du club est requis.")
        String nom,
        @NotEmpty(message = "Veuillez ajouter une description.")
        String description,
        String instagramme,
        @NotEmpty(message = "Veuillez ajouter au moins une activité.")
        List<String> activites
) implements Serializable {
}
