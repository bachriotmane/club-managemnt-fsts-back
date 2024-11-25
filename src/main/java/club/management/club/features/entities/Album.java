package club.management.club.features.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private String albumId;
    private String location;
    private LocalDate date;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;
    @OneToMany(fetch = FetchType.EAGER)
    List<Image> albumImages;
}
