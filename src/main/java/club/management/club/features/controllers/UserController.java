package club.management.club.features.controllers;

import club.management.club.features.dto.responses.EtudiantDto;
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
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        User savedUser = null;
        ResponseEntity<?> response =null;
        try {
            String hashPwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPwd);
            savedUser = userService.saveUser(user);
            if (savedUser.getId() != null) {//please Abdelkrim  check your logic here Long to UUID (savedUser.getId()>0 ==>savedUser.getId() != null)
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(savedUser);
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to " + ex.getMessage());
        }
        return response;
    }
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
