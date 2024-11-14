package club.management.club.entities;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String pubDesc;
    private Date date;
    private  boolean isPublic;
}
