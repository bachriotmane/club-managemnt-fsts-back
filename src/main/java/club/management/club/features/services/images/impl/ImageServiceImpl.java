package club.management.club.features.services.images.impl;

import club.management.club.features.dto.requests.ImageEditRequest;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Image;
import club.management.club.features.repositories.*;
import club.management.club.features.services.images.ImageService;
import club.management.club.features.validators.ImageValidator;
import club.management.club.shared.Constants.TypeObjetConstants;
import club.management.club.shared.Constants.ValidationConstants;
import club.management.club.shared.dtos.SuccessResponse;
import club.management.club.shared.exceptionHandler.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;
    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;
    private final PublicationRepository publicationRepository;
    private final EtudiantRepository etudiantRepository;

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
    @Transactional
    public Image editImage(ImageEditRequest dto, MultipartFile file) throws IOException {
        imageValidator.validate(file);

        var imageOptional = imageRepository.findById(dto.uuidImage());

        var image = imageOptional.orElseGet(() -> {
            try {
                return Image.builder()
                        .fileName(file.getOriginalFilename())
                        .data(file.getBytes())
                        .build();
            } catch (IOException e) {
                throw new BadRequestException(ValidationConstants.YOUR_IMAGE_CONTIENT_PROBLEME);
            }
        });

        switch (dto.typeObjet()) {
            case TypeObjetConstants.CLUB -> updateClubImage(dto.uuidObjet(), image);
            case TypeObjetConstants.EVENT -> {
                // add your logic
            }
            case TypeObjetConstants.STUDENT -> updateStudentImage(dto.uuidObjet(), image);
            default -> throw new BadRequestException(ValidationConstants.YOUR_IMAGE_CONTIENT_PROBLEME);
        }

        image.setFileName(file.getOriginalFilename());
        image.setData(file.getBytes());

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
    @Transactional
    public SuccessResponse<Boolean> delete(String uuid) {
        boolean exists = imageRepository.existsById(uuid);
        if (!exists) {
            throw new BadRequestException(ValidationConstants.IMAGE_NOT_FOUND);
        }
        imageRepository.deleteById(uuid);

        return new SuccessResponse<>(true);
    }
    private void updateClubImage(String clubUuid, Image image) {
        var clubOptional = clubRepository.findById(clubUuid);

        if (clubOptional.isPresent()) {
            Club club = clubOptional.get();
            club.setLogo(image);
            clubRepository.save(club);
        } else {
            throw new BadRequestException(ValidationConstants.CLUB_NOT_FOUND);
        }
    }
    private void updateStudentImage(String studentUuid, Image image) {
        var studentOptional = etudiantRepository.findById(studentUuid);

        if (studentOptional.isPresent()) {
            Etudiant student = studentOptional.get();
            student.setImgProfile(image);
            etudiantRepository.save(student);
        } else {
            throw new BadRequestException(ValidationConstants.STUDENT_NOT_FOUND);
        }
    }



    @Override
    public Image saveAlbumImage(MultipartFile image) throws IOException {
        imageValidator.validate(image);

        Image builtedImage = Image.builder()
                .fileName(image.getOriginalFilename())
                .data(image.getBytes())
                .build();
        return imageRepository.save(builtedImage);
    }

}

