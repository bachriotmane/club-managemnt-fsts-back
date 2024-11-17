package club.management.club.features.dto.responses;

public record UserDto(
    String id,
    String firstName,
    String lastName,
    String email,
    String cin,
    String facebook,
    String instagram,
    String whatsapp
) {
}
