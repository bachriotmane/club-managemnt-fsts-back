package club.management.club.features.dto.responses;

public record UserDto(
    String id,
    String firstName,
    String lastName,
    String email,
    String cne,
    String facebook,
    String instagram,
    String whatsapp,
    String imageProfile,
    String imageCover
) {
}
