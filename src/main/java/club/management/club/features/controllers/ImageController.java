package club.management.club.features.controllers;

import club.management.club.features.entities.Image;
import club.management.club.features.services.images.ImageService;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/images")
@Tag(name = "Images API", description = "Endpoints for managing images")
@SecurityRequirement(name = "Bearer Authentication")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(path = "/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public SuccessResponse<Boolean> uploadImage(
            @RequestParam("file") MultipartFile file) throws IOException {
        return imageService.storeImage(file);
    }

    @Operation(
            summary = "Retrieve an image by ID",
            description = "Fetches an image stored in the database using its unique ID",
            tags = { "Images" }
    )


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        var image = imageService.getImageById(id);
        var headers = new HttpHeaders();
        String extension = image.getFileName().substring(image.getFileName().lastIndexOf(".") + 1).toLowerCase();
        String mimeType = switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> throw new IllegalArgumentException("Type d'image non pris en charge : " + extension);
        };
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", image.getFileName());

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }
}
