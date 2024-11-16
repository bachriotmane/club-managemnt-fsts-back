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

    @OneToMany(fetch = FetchType.EAGER)
    private List<Integration> integrations;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Publication> publications;
}