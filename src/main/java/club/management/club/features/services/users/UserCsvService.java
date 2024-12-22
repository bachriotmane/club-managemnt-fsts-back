package club.management.club.features.services.users;

import club.management.club.features.Specifications.UserSpecifications;
import club.management.club.features.dto.requests.UserEditRequest;
import club.management.club.features.dto.requests.UserFilterDTO;
import club.management.club.features.dto.responses.ListUsersResponse;
import club.management.club.features.entities.Authority;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.mappers.EtudiantMapper;
import club.management.club.features.repositories.AuthorityRepo;
import club.management.club.features.repositories.EtudiantRepository;
import club.management.club.features.repositories.UserRepo;
import club.management.club.features.services.auths.AuthorityService;
import club.management.club.features.services.auths.JwtTokenService;
import club.management.club.features.services.email.EmailService;
import club.management.club.features.validators.CSVValidator;
import club.management.club.features.validators.StudentValidator;
import club.management.club.shared.Constants.Roles;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import com.nimbusds.jose.util.StandardCharset;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCsvService {
    private final EtudiantRepository etudiantRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;
    private final StudentValidator studentValidator;
    private final JwtTokenService jwtTokenService;
    private final UserRepo userRepo;
    private final EtudiantRepository etudiantRepo;
    private final AuthorityRepo authorityRepository;
    private final EtudiantMapper etudiantMapper;
    private final EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    public byte[] export(Authentication authentication) {
        var students = etudiantRepository.findAll();
        isAuthorized(authentication);
        StringBuilder csvContent = generateCsvContent(students);
        return csvContent.toString().getBytes(StandardCharset.UTF_8);
    }

    private StringBuilder generateCsvContent(List<Etudiant> students) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("FirstName,LastName,Email,CIN,CNE,Filiere,AccountLocked,password\n");

        for (Etudiant etudiant : students) {
            csvContent.append(String.format("%s,%s,%s,%s,%s,%s,%b,%s\n",
                    etudiant.getFirstName(),
                    etudiant.getLastName(),
                    etudiant.getEmail(),
                    etudiant.getCin(),
                    etudiant.getCne(),
                    etudiant.getFiliere(),
                    etudiant.isAccountLocked(),
                    "Not visible"
              ));
        }

        return csvContent;
    }

    private void isAuthorized(Authentication authentication) {
        var userId = jwtTokenService.getUserId(authentication);
        var user = userRepo.findById(userId).orElseThrow(() ->
                new BadRequestException(ValidationConstants.USER_NOT_FOUND));

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> Roles.ROLE_SUPERADMIN.equals(auth.getName()));

        if (!isAdmin) {
            throw new BadRequestException(ValidationConstants.NOT_AUTHORIZED);
        }
    }

    public SuccessResponse<String> upload(Authentication authentication, MultipartFile file) {
        isAuthorized(authentication);
        CSVValidator.validate(file);

        Authority authority = authorityService.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharset.UTF_8))) {
            processCsvFile(reader, authority);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }

        return new SuccessResponse<>("CSV file uploaded and students added successfully");
    }

    private void processCsvFile(BufferedReader reader, Authority authority) throws IOException, MessagingException {
        String line;
        int lineNumber = 1;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("FirstName")) {
                lineNumber++;
                continue;
            }

            String[] data = line.split(",");
            if (data.length == 8) {
                Etudiant etudiant = getEtudiant(data, authority);
                studentValidator.validateStudent(etudiant, lineNumber);
                etudiantRepository.save(etudiant);
                sendAccountCreationEmailWithPassword(etudiant,data[7]);
            }
            lineNumber++;
        }
    }

    private Etudiant getEtudiant(String[] data, Authority authority) {
        Etudiant etudiant = new Etudiant();
        etudiant.setFirstName(data[0]);
        etudiant.setLastName(data[1]);
        etudiant.setEmail(data[2]);
        etudiant.setCin(data[3]);
        etudiant.setCne(data[4]);
        etudiant.setFiliere(data[5]);
        etudiant.setAccountLocked(Boolean.parseBoolean(data[6]));
        etudiant.setAccountLEnabled(true);
        etudiant.setAccountCompleted(true);
        etudiant.setAuthorities(Set.of(authority));
        etudiant.setPassword(passwordEncoder.encode(data[7]));

        return etudiant;
    }
    private void sendAccountCreationEmailWithPassword(Etudiant etudiant, String password) throws MessagingException, UnsupportedEncodingException {

        emailService.sendAccountCreationEmail(
                etudiant.getEmail(),
                etudiant.getFirstName() + " " + etudiant.getLastName(),
                password,
                "Création de compte réussie"
        );
    }

    public ListSuccessResponse<ListUsersResponse> getAllUsers(Pageable paging, UserFilterDTO filter) {
        Authority authority = authorityRepository.findByName(filter.role());

        var spec = Specification
                .where(UserSpecifications.withUserName(filter.userName()))
                .and(UserSpecifications.withCin(filter.cin()))
                .and(UserSpecifications.withCne(filter.cne()))
                .and(UserSpecifications.withAuthority(authority));

        Page<Etudiant> users = etudiantRepo.findAll(spec, paging);

        Set<ListUsersResponse> userResponses = users.getContent().stream()
                .map(user -> new ListUsersResponse(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getCin(),
                        user.getCne(),
                        user.getFiliere(),
                        user.isAccountLocked(),
                        user.getAuthorities().stream()
                                .map(Authority::getName)
                                .findFirst()
                                .orElse(Roles.ROLE_USER)
                ))
                .collect(Collectors.toSet());

        return new ListSuccessResponse<>(
                userResponses,
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext()
        );
    }

    public SuccessResponse<ListUsersResponse> editUser(String userId, UserEditRequest etudiantEditRequest, Authentication authentication) throws MessagingException {
        isAuthorized(authentication);

        var etudiant = etudiantRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.USER_NOT_FOUND));

        Etudiant updatedEtudiant = etudiantMapper.toEtudiant(etudiantEditRequest, etudiant);

       var newUser = etudiantRepository.save(updatedEtudiant);
        if (etudiantEditRequest.isPasswordSend() && (etudiantEditRequest.password() != null && !etudiantEditRequest.password().isEmpty())) {
            emailService.sendPasswordResetEmail(
                    etudiantEditRequest.email(),
                    etudiant.getFirstName() + " " + etudiant.getLastName(),
                    etudiantEditRequest.password(),
                    "Réinitialisation de votre mot de passe"
            );
        }

        return new SuccessResponse<>(new ListUsersResponse(
                newUser.getId(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail(),
                newUser.getCin(),
                newUser.getCne(),
                newUser.getFiliere(),
                newUser.isAccountLocked(),
                newUser.getAuthorities().stream()
                        .map(Authority::getName)
                        .findFirst()
                        .orElse(Roles.ROLE_USER)
        ));
    }

}
