package club.management.club.features.validators;

import club.management.club.shared.Constants.ExtensionsConstants;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor
public class ImageValidator {


    public void validate(MultipartFile file) throws BadRequestException {
        if (!isImage(file)) {
            throw new BadRequestException(ValidationConstants.IMAGE_NOT_VALID);
        }
        validateSize(file);
        validateExtension(file);
    }
    private boolean isImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        var contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    private void validateSize(MultipartFile file) throws BadRequestException {
//        if (file.getSize() >  1048576) { //default max in Mysql
        if (file.getSize() >  2097152) {
            throw new BadRequestException(ValidationConstants.IMAGE_SIZE_SHOULD_BE_UNDER_TWO_MB);
        }
    }

    private void validateExtension(MultipartFile file) throws BadRequestException {
        if (!ExtensionsConstants.IMAGE_EXTENSIONS.contains(file.getContentType())) {
            throw new BadRequestException(ValidationConstants.IMAGE_EXTENSION_IS_NOT_SUPPORTED);
        }
    }
}
