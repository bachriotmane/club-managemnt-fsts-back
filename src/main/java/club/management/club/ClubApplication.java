package club.management.club;


import club.management.club.features.entities.*;
import club.management.club.features.enums.MemberRole;
import club.management.club.features.repositories.*;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@OpenAPIDefinition(
        info = @Info(
                title = "FSTs Clubs Management",
                description = "FSTs Clubs management application REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "FST Settat",
                        email = "karimid2002@gmail.com",
                        url = "fsts.support.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.fsts.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "FSTs Clubs management application REST API Documentation",
                url = "https://www.fsts.com/swagger-ui.html"
        )
)
@SpringBootApplication
@AllArgsConstructor
public class ClubApplication {
    private PublicationRepository publicationRepository;
    private ClubRepository clubRepository;
    private final UserRepo userRepo;
    private final IntegrationRepository integrationRepository;

    private final EtudiantRepository etudiantRepository;
    private final AuthorityRepo authorityRepo;
    private final PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(ClubApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            Etudiant etudiant = etudiantRepository.findByEmail("bourich.sou.fst@uhp.ac.ma")
                    .orElseGet(() -> createEtudiant());

            createClubsAndIntegrations(etudiant);

            if (authorityRepo.findAuthorityByName("ROLE_USER").isEmpty()) {
                authorityRepo.save(Authority.builder().name("ROLE_USER").build());
            }
            // Creating a new Etudiant
            Etudiant student = new Etudiant();
            student.setFirstName("SARAH");
            student.setCne("K987654321");
            student = userRepo.save(student);

// Creating another Etudiant
            Etudiant anotherStudent = new Etudiant();
            anotherStudent.setFirstName("AHMED");
            anotherStudent.setCne("K123987654");
            anotherStudent = userRepo.save(anotherStudent);

// Creating a new club
            Club newClub = clubRepository.save(Club.builder()
                    .nom("TECH INNOVATORS")
                    .createdAt(new Date())
                    .description("A club dedicated to exploring and innovating technology.")
                    .build());

// Adding first student to the new club
            Integration integration1 = Integration.builder()
                    .etudiant(student)
                    .club(newClub)
                    .memberRole(MemberRole.MEMBER)
                    .roleName("President")
                    .build();
            integration1 = integrationRepository.save(integration1);

// Adding second student to the new club
            Integration integration2 = Integration.builder()
                    .etudiant(anotherStudent)
                    .club(newClub)
                    .memberRole(MemberRole.MEMBER)
                    .roleName("Member")
                    .build();
            integration2 = integrationRepository.save(integration2);

// Associating integrations with the new club
            newClub.setIntegrations(new ArrayList<>());
            newClub.getIntegrations().add(integration1);
            newClub.getIntegrations().add(integration2);
            clubRepository.save(newClub);

// Adding publications to the new club
            Publication pub1 = publicationRepository.save(Publication.builder()
                    .isPublic(true)
                    .title("Welcome to Tech Innovators")
                    .pubDesc("We're excited to launch our new tech-focused club!")
                    .date(LocalDateTime.of(2024, Month.JANUARY, 10, 10, 0))
                    .build());
            newClub.setPublications(new ArrayList<>());
            newClub.getPublications().add(pub1);
            newClub = clubRepository.save(newClub);
            pub1.setClub(newClub);
            publicationRepository.save(pub1);

            Publication pub2 = publicationRepository.save(Publication.builder()
                    .isPublic(true)
                    .title("Hackathon Announcement")
                    .pubDesc("Join us for an exciting 48-hour hackathon next month!")
                    .date(LocalDateTime.of(2024, Month.FEBRUARY, 15, 14, 0))
                    .build());
            newClub.getPublications().add(pub2);
            newClub = clubRepository.save(newClub);
            pub2.setClub(newClub);
            publicationRepository.save(pub2);

            Publication pub3 = publicationRepository.save(Publication.builder()
                    .isPublic(false)
                    .title("AI Workshop for Members")
                    .pubDesc("An exclusive workshop on AI advancements for club members.")
                    .date(LocalDateTime.of(2024, Month.MARCH, 20, 16, 0))
                    .build());
            newClub.getPublications().add(pub3);
            newClub = clubRepository.save(newClub);
            pub3.setClub(newClub);
            publicationRepository.save(pub3);

            Publication pub4 = publicationRepository.save(Publication.builder()
                    .isPublic(true)
                    .title("Tech Fair 2024")
                    .pubDesc("Showcase your projects at our annual tech fair.")
                    .date(LocalDateTime.of(2024, Month.MAY, 5, 11, 30))
                    .build());
            newClub.getPublications().add(pub4);
            newClub = clubRepository.save(newClub);
            pub4.setClub(newClub);
            publicationRepository.save(pub4);

            Publication pub5 = publicationRepository.save(Publication.builder()
                    .isPublic(true)
                    .title("End of Year Celebration")
                    .pubDesc("Join us for our annual celebration to wrap up the year!")
                    .date(LocalDateTime.of(2024, Month.DECEMBER, 22, 18, 0))
                    .build());
            newClub.getPublications().add(pub5);
            newClub = clubRepository.save(newClub);
            pub5.setClub(newClub);
            publicationRepository.save(pub5);


            addPublications();

        };
    }

    private Etudiant createEtudiant() {
        // Créer un nouvel Etudiant si l'email n'existe pas
        Etudiant etudiant = new Etudiant();
        etudiant.setFirstName("Soufian");
        etudiant.setLastName("Bourich");
        etudiant.setEmail("bourich.sou.fst@uhp.ac.ma");
        etudiant.setCin("CIN123456");
        etudiant.setCne("CNE123456");
        etudiant.setFiliere("Informatique");
        etudiant.setPassword(passwordEncoder.encode("Soufian@20"));
        etudiant.setAccountLocked(false);
        etudiant.setAccountLEnabled(true);
        etudiant.setAccountCompleted(true);
        etudiantRepository.save(etudiant);
        return etudiant;
    }

    private void createClubsAndIntegrations(Etudiant etudiant) {
        // Créer 20 clubs
        for (int i = 1; i <= 20; i++) {
            Club club = new Club();
            club.setNom("Club " + i);
            club.setDescription("Le Club Bac N"+i+" est un club dédié aux passionnés de développement backend et aux technologies qui façonnent l'architecture des applications web et mobiles");
            club.setCreatedAt(new Date());
            club.setLogo(null);
            club.setInstagramme("https://instagram.com/club" + i);
            clubRepository.save(club);
        }

        // Créer 5 intégrations pour l'étudiant Bourich
        List<Club> clubs = clubRepository.findAll();
        for (int i = 0; i < 5; i++) {
            Club club = clubs.get(i);
            Integration integration = new Integration();
            integration.setRoleName("Membre");
            integration.setMemberRole(MemberRole.MEMBER);
            integration.setEtudiant(etudiant);
            integration.setClub(club);
            integrationRepository.save(integration);
        }
    }

    private void addPublications() {
        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("New Member Joins Us")
                .pubDesc("We are thrilled to welcome a new team member!")
                .date(LocalDateTime.of(2024, Month.JANUARY, 5, 10, 30))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Quarterly Sales Report Released")
                .pubDesc("Our Q3 sales report shows promising growth.")
                .date(LocalDateTime.of(2024, Month.MARCH, 1, 14, 0))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Tech Conference 2024")
                .pubDesc("Excited to participate in the upcoming tech conference!")
                .date(LocalDateTime.of(2024, Month.MAY, 18, 9, 45))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Product Launch")
                .pubDesc("Announcing our latest product in the market.")
                .date(LocalDateTime.of(2024, Month.JULY, 12, 11, 15))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(false)
                .title("Internal Strategy Meeting")
                .pubDesc("A meeting to discuss our future strategies.")
                .date(LocalDateTime.of(2024, Month.AUGUST, 20, 16, 0))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Office Renovation Complete")
                .pubDesc("Our office renovation is now complete! Check it out!")
                .date(LocalDateTime.of(2024, Month.SEPTEMBER, 25, 13, 30))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Charity Drive Success")
                .pubDesc("Thanks to all who participated in our recent charity event.")
                .date(LocalDateTime.of(2024, Month.OCTOBER, 15, 15, 45))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Employee of the Month")
                .pubDesc("Congratulations to Jane Doe, our employee of the month!")
                .date(LocalDateTime.of(2024, Month.NOVEMBER, 3, 12, 0))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Health and Wellness Workshop")
                .pubDesc("Join our upcoming wellness workshop for all employees.")
                .date(LocalDateTime.of(2024, Month.DECEMBER, 10, 9, 0))
                .build());

        publicationRepository.save(Publication.builder()
                .isPublic(true)
                .title("Holiday Party Announced")
                .pubDesc("Our annual holiday party is coming up soon, stay tuned!")
                .date(LocalDateTime.of(2024, Month.DECEMBER, 20, 18, 30))
                .build());
    }
}
