package club.management.club;


import club.management.club.features.entities.Authority;
import club.management.club.features.entities.Publication;
import club.management.club.features.repositories.AuthorityRepo;
import club.management.club.features.repositories.PublicationRepository;
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

import java.time.LocalDateTime;
import java.time.Month;

@OpenAPIDefinition(
        info = @Info(
                title = "FSTs Clubs management",
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

    // test
    public static void main(String[] args) {
        SpringApplication.run(ClubApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(AuthorityRepo authorityRepo) {
        return args -> {
            if (authorityRepo.findAuthorityByName("ROLE_USER").isEmpty()) {
                authorityRepo.save(Authority.builder().name("ROLE_USER").build());
            }

            // Add Some Pubs to the database
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

        };
    }
}
