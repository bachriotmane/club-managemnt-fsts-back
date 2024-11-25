package club.management.club.features.entities;

import club.management.club.features.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Etudiant etudiant;
    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;


}
