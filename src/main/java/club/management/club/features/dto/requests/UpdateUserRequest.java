package club.management.club.features.dto.requests;

public record UpdateUserRequest(
    String id,
    String firstName,
    String lastName,
    String cne,
    String filiere,
    String facebook,
    String instagram,
    String whatsapp
) {
}
