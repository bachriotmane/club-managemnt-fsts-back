package club.management.club.features.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Etudiant extends User {
    private String cne;
    private String filiere;

    @OneToMany
    private List<Demande> demandes;
    
}
