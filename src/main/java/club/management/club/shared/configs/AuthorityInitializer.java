package club.management.club.shared.configs;

import club.management.club.features.entities.Authority;
import club.management.club.features.repositories.AuthorityRepo;
import club.management.club.shared.Constants.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorityInitializer implements CommandLineRunner {

    private final AuthorityRepo authorityRepo;

    @Override
    public void run(String... args)  {
        List<String> roles = List.of(
                Roles.ROLE_ADMIN,
                Roles.ROLE_USER,
                Roles.ROLE_SUPERADMIN
        );
        for (String role : roles) {
            if (!authorityRepo.existsByName(role)) {
                Authority authority = Authority.builder()
                        .name(role)
                        .build();
                authorityRepo.save(authority);
            }
        }
    }
}