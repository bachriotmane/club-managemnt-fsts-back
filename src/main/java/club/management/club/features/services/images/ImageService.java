package club.management.club.features.services.images;

import club.management.club.features.entities.Image;
import club.management.club.shared.dtos.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {
    Image saveImage(MultipartFile file)  throws IOException;
    SuccessResponse<Boolean> storeImage(MultipartFile file) throws IOException;
    Image getImageById(String id);
}