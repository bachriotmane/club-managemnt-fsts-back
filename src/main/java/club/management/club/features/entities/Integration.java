package club.management.club.features.entities;

import club.management.club.features.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Integration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String roleName;
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.EAGER)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.EAGER)
    private Club club;
}
