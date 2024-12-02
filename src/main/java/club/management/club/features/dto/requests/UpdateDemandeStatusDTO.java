package club.management.club.features.dto.requests;

import club.management.club.features.enums.StatutDemande;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;


public record UpdateDemandeStatusDTO(
        StatutDemande statutDemande,
        String agent,
        String comment
) {
}