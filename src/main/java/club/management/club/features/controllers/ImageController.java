package club.management.club.features.controllers;

import club.management.club.features.dto.requests.ImageEditRequest;
import club.management.club.features.services.images.ImageService;
import club.management.club.shared.Constants.ExtensionsConstants;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Tag(name = "Images API", description = "Endpoints for managing images")
@SecurityRequirement(name = "Bearer Authentication")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Save image and get UUID this image ")
    @PostMapping(path = "/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public SuccessResponse<String> uploadImage(
            @RequestParam("file") MultipartFile file) throws IOException {
        return imageService.storeImage(file);
    }

    @Operation(summary = "Retrieve an image by UUID")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        var image = imageService.getImageById(id);
        var headers = new HttpHeaders();
        String extension = image.getFileName().substring(image.getFileName().lastIndexOf(".") + 1).toLowerCase();
        String mimeType = ExtensionsConstants.EXTENSION_TO_MIME_TYPE.get(extension);        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", image.getFileName());

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }
    @Operation(summary = "Edit image by UUID.")
    @PutMapping(path = "/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<byte[]> editImage( @ModelAttribute ImageEditRequest dto, @RequestParam("file") MultipartFile file) throws IOException {
        var image = imageService.editImage(dto,file);
        var headers = new HttpHeaders();
        String extension = image.getFileName().substring(image.getFileName().lastIndexOf(".") + 1).toLowerCase();
        String mimeType = ExtensionsConstants.EXTENSION_TO_MIME_TYPE.get(extension);
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", image.getFileName());

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }
    @Operation(summary = "Delete image by UUID.")
    @DeleteMapping("/image/delete/{uuid}")
    public  SuccessResponse<Boolean>  deleteImage(@PathVariable String  uuid){
        return imageService.delete(uuid);
    }
}
