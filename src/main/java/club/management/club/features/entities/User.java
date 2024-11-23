package club.management.club.features.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;
import static jakarta.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String cin;

    @Column(unique = true)
    private String facebook;

    @Column(unique = true)
    private String instagram;

    @Column(unique = true)
    private String whatsapp;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean accountLocked;
    private boolean accountLEnabled;
    private boolean accountCompleted;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Image imgProfile;
    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    Set<Authority> authorities;

}