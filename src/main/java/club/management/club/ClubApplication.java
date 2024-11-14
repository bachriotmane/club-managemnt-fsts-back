package club.management.club;

import club.management.club.features.entities.Authority;
import club.management.club.features.repositories.AuthorityRepo;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
                description =  "FSTs Clubs management application REST API Documentation",
                url = "https://www.fsts.com/swagger-ui.html"
        )
)
@SpringBootApplication
public class ClubApplication {
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
        };
    }
}
