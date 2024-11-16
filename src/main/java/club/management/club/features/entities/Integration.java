package club.management.club.features.entities;

import club.management.club.features.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Integration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String roleName;
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;
}
