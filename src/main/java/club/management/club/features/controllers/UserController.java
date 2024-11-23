package club.management.club.features.controllers;

import club.management.club.features.dto.responses.EtudiantDto;
import club.management.club.features.dto.responses.UserRolesInsideClubResponse;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.User;
import club.management.club.features.mappers.EtudiantMapper;
import club.management.club.features.mappers.UserMapper;
import club.management.club.features.services.users.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User API")
public class UserController {
    private final EtudiantMapper etudiantMapper;
    private final UserMapper userMapper;
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
}
