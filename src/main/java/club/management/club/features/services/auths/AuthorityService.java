package club.management.club.features.services.auths;

import club.management.club.features.entities.Authority;
import club.management.club.features.repositories.AuthorityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepo authorityRepo;
    public Optional<Authority> findByName(String name){
        return authorityRepo.findAuthorityByName(name);
    }
}
