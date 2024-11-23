package club.management.club.features.dto.requests;


import java.io.Serializable;

public record ImageEditRequest(
         String uuidImage,
         String typeObjet,
         String uuidObjet

)  implements Serializable {
}
