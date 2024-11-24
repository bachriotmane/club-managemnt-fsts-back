package club.management.club.features.entities;

import club.management.club.features.enums.StatutDemande;
import club.management.club.features.enums.TypeDemande;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Date date;
    private String description ;
    @Enumerated(EnumType.STRING)
    private StatutDemande statutDemande;
    @Enumerated(EnumType.STRING)
    private TypeDemande type;
    private String motivation;
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


}

