package club.management.club.features.dto.requests;

import club.management.club.features.enums.TypeDemande;

public record DemandeCreationDTO(
        TypeDemande demandeType,
        IntegrationCreationDTO integration,
        ClubCreationDTO club,
        EventCreationDTO event
) {
}
