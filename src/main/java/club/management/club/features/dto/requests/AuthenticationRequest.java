package club.management.club.features.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @Pattern(regexp = ".+@uhp\\.ac\\.ma",message = "Email should end with : @uhp.ac.ma")
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    private String password;
}