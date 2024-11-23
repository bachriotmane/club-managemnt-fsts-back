package club.management.club.features.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstName;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastName;
    @Pattern(regexp = ".+@uhp\\.ac\\.ma",message = "Email should end with : @uhp.ac.ma")
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

    @NotEmpty(message = "CIN is mandatory")
    @NotNull(message = "CIN is mandatory")
    @Pattern(regexp = "[A-Z]{1,2}[0-9]{4,15}", message = "CNE should be 2 Or 1 uppercase letters followed by 4 - 15 digits")
    private String cne;
    private String facebook;
    private String instagram;
    private String whatsapp;
}
