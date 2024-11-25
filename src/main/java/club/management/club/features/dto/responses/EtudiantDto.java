package club.management.club.features.dto.responses;

import java.util.List;

public record EtudiantDto(

    String id,
    String firstName,
    String lastName,
    String email,
    String cne,
    String filiere,
    String facebook,
    String instagram,
    String whatsapp,
    String imageProfile,
    String imageCover,
    List<String> memberClubs,
    List<String> adminClubs
) {
}
