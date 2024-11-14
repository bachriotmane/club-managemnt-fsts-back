package club.management.club.features.entities;

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
@Getter
@Setter
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nom;
    private String description;
    private Date createdAt;
    private String logo; 
    private String instagramme;

    @ManyToMany
    @JoinTable(
        name = "club_etudiant",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    private List<Etudiant> members;

    @OneToMany
    private List<Publication> publications;

    @OneToMany
    private List<Demande> demandes;

    
    @ManyToMany
    @JoinTable(
        name = "club_admins",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id")
    )
    private List<Etudiant> admins;
}