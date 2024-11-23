package club.management.club.features.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Etudiant extends User {
    private String filiere;

    @OneToMany(mappedBy = "etudiant")
    private List<Integration> integrations;

    @OneToMany
    private List<Demande> demandes;

}
