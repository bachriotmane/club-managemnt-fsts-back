package club.management.club.features.dto.requests;

import java.io.Serializable;

public record UserEditRequest(
         String firstName,
         String lastName,
         String email,
         String cne,
         String cin,
         String filiere,
         String role,
         Boolean accountLocked,
         String password
)  implements Serializable {
}
