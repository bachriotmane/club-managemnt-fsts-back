package club.management.club.features.mappers;

import club.management.club.features.dto.responses.UserDto;
import club.management.club.features.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getCin(),
            user.getFacebook(),
            user.getInstagram(),
            user.getWhatsapp()
        );
    }
}
