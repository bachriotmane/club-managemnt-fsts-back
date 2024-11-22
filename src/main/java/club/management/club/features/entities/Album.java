package club.management.club.features.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String location;
    private LocalDateTime dte;
    private String description;
}
