package club.management.club.features.services.images;

import club.management.club.features.dto.requests.ImageEditRequest;
import club.management.club.features.entities.Image;
import club.management.club.shared.dtos.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {
    //use this methode in all services  for save your image
    Image saveImage(MultipartFile file)  throws IOException;
    Image editImage(ImageEditRequest dto, MultipartFile file) throws IOException;

    SuccessResponse<String> storeImage(MultipartFile file) throws IOException;
    Image getImageById(String id);

    SuccessResponse<Boolean> delete(String uuid);
    Image saveAlbumImage(MultipartFile image) throws IOException;

}