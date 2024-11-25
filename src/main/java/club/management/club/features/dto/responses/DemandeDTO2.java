package club.management.club.features.dto.responses;

import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandeDTO2 {
    private String id ;
    private StatutDemande statutDemande;
    private TypeDemande type;
    private String idClub ;
    private String idEvent ;
    private String idIntegration ;

}
