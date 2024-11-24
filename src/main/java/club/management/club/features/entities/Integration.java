package club.management.club.features.entities;

import club.management.club.features.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

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
    private Date integrationDate;
    private boolean isValid;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Etudiant etudiant;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Club club;
}
