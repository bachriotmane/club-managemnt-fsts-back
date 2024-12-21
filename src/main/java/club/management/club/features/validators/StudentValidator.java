package club.management.club.features.validators;

import club.management.club.features.entities.Etudiant;
import club.management.club.features.repositories.EtudiantRepository;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.exceptionHandler.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StudentValidator {

    @Value("${email.domain:.fst@uhp.ac.ma}")
    private String emailDomain;

    @Value("${application.security.password-regex}")
    private String passwordRegex;

    private final EtudiantRepository etudiantRepository;

    public StudentValidator(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public boolean validateStudent(Etudiant etudiant, int lineNumber) {
        if (!isEmailValid(etudiant.getEmail())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.EMAIL_FORMAT_INVALID);
        }

        if (etudiantRepository.existsByEmail(etudiant.getEmail())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.ACCOUNT_ALREADY_EXISTS);
        }

        // Vérification de l'unicité du CIN
        validateCINUnique(etudiant.getCin(), lineNumber);

        // Vérification de l'unicité du CNE
        validateCNEUnique(etudiant.getCne(), lineNumber);

        if (isMandatory(etudiant.getFirstName())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.FIRST_NAME_IS_MANDATORY);
        }

        if (isMandatory(etudiant.getLastName())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.LAST_NAME_IS_MANDATORY);
        }

        if (isMandatory(etudiant.getFiliere())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.FILIERE_IS_MANDATORY);
        }

        if (isMandatory(etudiant.getPassword())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.PASSWORD_IS_MANDATORY);
        }

        if (!isPasswordValid(etudiant.getPassword())) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.PASSWORD_FORMAT_INVALID);
        }

        return true;
    }

    private boolean isMandatory(String txt) {
        return txt == null || txt.trim().isEmpty();
    }

    private boolean isEmailValid(String email) {
        if (email == null || emailDomain == null) {
            return false;
        }

        if (!email.endsWith(emailDomain)) {
            return false;
        }

        String localPart = email.substring(0, email.length() - emailDomain.length());
        int firstDotIndex = localPart.indexOf('.');

        if (firstDotIndex == -1 || firstDotIndex == 0 || firstDotIndex == localPart.length() - 1) {
            return false;
        }

        String subPart = localPart.substring(firstDotIndex + 1);
        int secondDotIndex = subPart.indexOf('.');
        if (secondDotIndex != -1) {
            subPart = subPart.substring(0, secondDotIndex);
        }

        return subPart.length() == 3;
    }

    private boolean isPasswordValid(String password) {

        if (password == null || password.length() < 8 || passwordRegex == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private void validateCINUnique(String cin, int lineNumber) {
        if (cin == null || etudiantRepository.existsByCin(cin)) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.CIN_ALREADY_EXISTS);
        }
    }

    private void validateCNEUnique(String cne, int lineNumber) {
        if (cne == null || etudiantRepository.existsByCne(cne)) {
            throw new BadRequestException("Ligne N° " + lineNumber + ": " + ValidationConstants.CNE_ALREADY_EXISTS);
        }
    }
}
