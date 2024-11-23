package club.management.club.features.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public record ClubEditRequest(
        @NotEmpty(message = "Le nom du club est requis.")
        String nom,
        @NotEmpty(message = "Veuillez ajouter une description.")
        String description,
        String instagramme,
        @NotEmpty(message = "Veuillez ajouter au moins une activité.")
        @Size(min = 1, max = 7, message = "La liste des activités doit contenir entre 1 et 7 éléments.")
        List<String> activites
) implements Serializable {
}
