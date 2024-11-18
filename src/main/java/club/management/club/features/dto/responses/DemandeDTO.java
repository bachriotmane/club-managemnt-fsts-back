package club.management.club.features.dto.responses;

import club.management.club.features.enums.StatutDemande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandeDTO {
    private String id;
    private String etudiantDemandeurNom;
    private Date date;
    private StatutDemande statutDemande;
}