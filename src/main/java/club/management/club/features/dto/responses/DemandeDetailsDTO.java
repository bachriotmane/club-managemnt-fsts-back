package club.management.club.features.dto.responses;

import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;

import java.time.LocalDateTime;
import java.util.List;

public record DemandeDetailsDTO(
        String demandeId,
        TypeDemande typeDemande,
        StatutDemande statutDemande,
        String demandeurUserName,
        String demandeurId,
        // Integration
        String clubName,
        String clubId,
        String motivation,
        // Creation
        String instagrammeLink,
        String creationDesc,
        List<String> activities,
        // For event
        String eventName,
        String location,
        double budget,
        LocalDateTime eventDate,
        String eventDesc
) {
}
