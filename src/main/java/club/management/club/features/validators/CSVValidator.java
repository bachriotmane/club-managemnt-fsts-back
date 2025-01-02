package club.management.club.features.validators;

import club.management.club.shared.Constants.ExtensionsConstants;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.exceptionHandler.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

public class CSVValidator {

    public static void validate(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            throw new BadRequestException(ValidationConstants.INVALID_CSV_FILE);
        }

        String filename = file.getOriginalFilename().toLowerCase();
        if (!filename.endsWith(".csv")) {
            throw new BadRequestException(ValidationConstants.INVALID_CSV_EXTENSION);
        }

        String contentType = file.getContentType();
        if (!ExtensionsConstants.CSV_EXTENSIONS.contains(contentType)) {
            throw new BadRequestException(ValidationConstants.INVALID_CSV_MIME_TYPE);
        }
    }

}
