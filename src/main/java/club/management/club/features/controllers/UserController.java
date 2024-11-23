package club.management.club.features.controllers;

import club.management.club.features.dto.requests.UpdateUserRequest;
import club.management.club.features.dto.responses.EtudiantDto;
import club.management.club.features.dto.responses.UserRolesInsideClubResponse;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Image;
import club.management.club.features.entities.User;
import club.management.club.features.mappers.EtudiantMapper;
import club.management.club.features.mappers.UserMapper;
import club.management.club.features.services.images.impl.ImageServiceImpl;
import club.management.club.features.services.users.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User API")
public class UserController {
    private final EtudiantMapper etudiantMapper;
    private final UserMapper userMapper;
    private final ImageServiceImpl imageServiceImpl;
    private PasswordEncoder passwordEncoder;
    private UserService userService ;
    @GetMapping("/test")
    public String test( ){
        return "hello fom protected resources ";
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);

        if (user instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) user;
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(etudiantMapper.toEtudiantDto(etudiant));
        } else if ( user != null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userMapper.toUserDto(user));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }


    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable String id) {
        User user = userService.getUserById(id);

        if (user != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.getImgProfile());
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

    @PatchMapping(value = "/profile/{id}" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> updateProfileImage(@PathVariable String id , @RequestPart("profile") MultipartFile imageProfile , Principal principal) throws IOException {

        User user = userService.getUserById(id);
        if(!principal.getName().equals(user.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to update this user profile");
        }
        if (user != null) {
            Image profileImage = imageServiceImpl.saveImage(imageProfile);
            user.setImgProfile(profileImage);
            userService.saveUser(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Profile image updated successfully");
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

    }


    @PatchMapping(value = "/cover/{id}" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> updateCoverImage(@PathVariable String id , @RequestPart("cover") MultipartFile imageCover , Principal principal) throws IOException {

        User user = userService.getUserById(id);
        if(!principal.getName().equals(user.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to update this user profile");
        }

        if (user != null) {
            Image coverImage = imageServiceImpl.saveImage(imageCover);
            user.setImgCover(coverImage);
            userService.saveUser(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Profile image updated successfully");
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id , @RequestBody UpdateUserRequest userRequest , Principal principal) {


        User userToUpdate = userService.getUserById(id);

        if(!principal.getName().equals(userToUpdate.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to update this user profile");
        }

        if (userToUpdate != null) {
            userToUpdate.setFirstName(userRequest.firstName());
            userToUpdate.setLastName(userRequest.lastName());
            userToUpdate.setCne(userRequest.cne());
            userToUpdate.setFacebook(userRequest.facebook());
            userToUpdate.setInstagram(userRequest.instagram());
            userToUpdate.setWhatsapp(userRequest.whatsapp());

            userService.saveUser(userToUpdate);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User updated successfully");
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

}
