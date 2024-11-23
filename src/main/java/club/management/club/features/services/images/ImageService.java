package club.management.club.features.services.images;

import club.management.club.features.entities.Image;
import club.management.club.shared.dtos.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {
    //use this methode in all services  for save your image
    Image saveImage(MultipartFile file)  throws IOException;
    Image editImage(MultipartFile file,String uuid) throws IOException;

    SuccessResponse<String> storeImage(MultipartFile file) throws IOException;
    Image getImageById(String id);

    SuccessResponse<Boolean> delete(String uuid);
}