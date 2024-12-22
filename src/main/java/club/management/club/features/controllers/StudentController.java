package club.management.club.features.controllers;

import club.management.club.features.dto.requests.UserEditRequest;
import club.management.club.features.dto.requests.UserFilterDTO;
import club.management.club.features.dto.responses.ListUsersResponse;
import club.management.club.features.services.users.UserCsvService;
import club.management.club.shared.dtos.ListSuccessResponse;
import club.management.club.shared.dtos.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/Students")
@AllArgsConstructor
@Tag(name = "Students API")
@SecurityRequirement(name = "Bearer Authentication")
public class StudentController {
    private final UserCsvService userCsvService;

    @GetMapping("/csv")
    @Operation(summary = "Download students as a csv file.")
    public ResponseEntity<byte[]> generateCsvFile(Authentication authentication) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "students.csv");

        byte[] csvBytes = userCsvService.export(authentication);
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
    @PostMapping(path = "/csv", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Upload students as a csv file.")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<String> uploadCSV(Authentication authentication, @RequestPart("file") MultipartFile file) {
        return userCsvService.upload(authentication, file);
    }
    @GetMapping("/users/")
    @Operation(summary = "Get all users .")
    public ListSuccessResponse<ListUsersResponse> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            UserFilterDTO filter
    ) {

        var paging = PageRequest.of(page, size);
        return userCsvService.getAllUsers(paging, filter);
    }
    @PutMapping("/users/{userId}")
    @Operation(summary = "Edit user details.")
    public SuccessResponse<ListUsersResponse> editUser(
            @PathVariable("userId") String userId,
            @RequestBody UserEditRequest userEditRequest,
            Authentication authentication
    ) throws MessagingException {
        return userCsvService.editUser(userId, userEditRequest, authentication);
    }

}
