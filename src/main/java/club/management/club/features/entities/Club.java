package club.management.club.features.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nom;
    private String description;
    private Date createdAt;
    private String logo; 
    private String instagramme;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "club")
    private List<Integration> integrations;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Publication> publications;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Evenement> evenements = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "club_activites", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "activite")
    private List<String> activites;
}