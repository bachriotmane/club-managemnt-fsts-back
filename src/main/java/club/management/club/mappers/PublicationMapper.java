package club.management.club.mappers;

import club.management.club.dto.PublicationDTO;
import club.management.club.entities.Publication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PublicationMapper {
    public PublicationDTO convertToDTO(Publication publication) {
        return new PublicationDTO("", "", "",new Date(), false,"false");
    }

    public Publication convertToPublication(PublicationDTO dto) {
        return new Publication();
    }

}
