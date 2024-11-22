package club.management.club.features.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")

    private byte[] data;

    @Column(nullable = false)
    private String fileName;

}
