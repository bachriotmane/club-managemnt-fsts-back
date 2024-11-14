package club.management.club.features.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;


    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private Set<User> users;
}
