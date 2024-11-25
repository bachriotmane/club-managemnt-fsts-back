package club.management.club.features.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandeDTO3 {
    private String id;
    private String description;
    private String idEtudiant;
}