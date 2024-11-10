package club.management.club.services;

import club.management.club.entities.MailToken;
import club.management.club.repositories.MailTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailTokenService {
    private final MailTokenRepo mailTokenRepo;
    public Optional<MailToken> findMailTokenByToken(String token){
        return  mailTokenRepo.findMailTokenByToken(token);
    }
    public void deleteTokenById(Integer id){
        mailTokenRepo.deleteById(id);
    }
    public void saveToken(MailToken mailToken) {
        Optional<MailToken> existingToken = mailTokenRepo.findMailTokenByToken(mailToken.getToken());
        if (existingToken.isPresent()) {
            throw new RuntimeException("Token already exists");
        }
        mailTokenRepo.save(mailToken);
    }

}
