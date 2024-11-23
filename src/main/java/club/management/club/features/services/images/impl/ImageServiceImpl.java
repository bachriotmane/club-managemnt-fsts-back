package club.management.club.features.services.images.impl;

import club.management.club.features.entities.Image;
import club.management.club.features.repositories.ImageRepository;
import club.management.club.features.services.images.ImageService;
import club.management.club.features.validators.ImageValidator;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;

    @Override
    public Image saveImage(MultipartFile file) throws IOException {
        imageValidator.validate(file);

        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .data(file.getBytes())
                .build();

        return imageRepository.save(image);
    }


    @Override
    public Image editImage(MultipartFile file, String uuid) throws IOException {
        imageValidator.validate(file);

        var image = imageRepository.findById(uuid)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.IMAGE_NOT_FOUND));

        image.setData(file.getBytes());
        image.setFileName(file.getOriginalFilename());

        return imageRepository.save(image);
    }

    @Override
    public SuccessResponse<String> storeImage(MultipartFile file) throws IOException {
        imageValidator.validate(file);

        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .data(file.getBytes())
                .build();

        var newImage = imageRepository.save(image);
        return new SuccessResponse<>(newImage.getId());
    }

    @Override
    public Image getImageById(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ValidationConstants.IMAGE_NOT_FOUND));
    }

    @Override
    public SuccessResponse<Boolean> delete(String uuid) {
        boolean exists = imageRepository.existsById(uuid);
        if (!exists) {
            throw new BadRequestException(ValidationConstants.IMAGE_NOT_FOUND);
        }
        imageRepository.deleteById(uuid);

        return new SuccessResponse<>(true);
    }
}

