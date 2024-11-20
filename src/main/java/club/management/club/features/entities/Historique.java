package club.management.club.features.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String titre;
    private String description;
    private LocalDateTime date;
}