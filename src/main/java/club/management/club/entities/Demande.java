package club.management.club.entities;

import club.management.club.enums.StatutDemande;
import club.management.club.enums.TypeDemande;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;

    @ManyToOne
    private Etudiant etudiantDemandeur;
    @ManyToOne
    private Club club;
    @OneToMany
    private List<Historique> historiques;
    @OneToOne
    private Evenement organisationEvenement;

    @Enumerated(EnumType.STRING)
    private StatutDemande statutDemande;
    
    @Enumerated(EnumType.STRING)
    private TypeDemande type;

}

