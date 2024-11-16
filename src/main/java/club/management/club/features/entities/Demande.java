package club.management.club.features.entities;

import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
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
    @Enumerated(EnumType.STRING)
    private StatutDemande statutDemande;
    @Enumerated(EnumType.STRING)
    private TypeDemande type;

    @ManyToOne
    private Etudiant etudiantDemandeur;
    @ManyToOne
    private Club club;
    @ManyToOne
    private Integration integration;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Historique> historiques;
    @OneToOne
    private Evenement organisationEvenement;
    //aymen
}

