package club.management.club.mappers;

import club.management.club.dto.PublicationDTO;
import club.management.club.entities.Publication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PublicationMapper {
    public PublicationDTO convertToDTO(Publication publication) {
        return new PublicationDTO(publication.getId(), publication.getTitle(), publication.getPubDesc(),publication.getDate(), publication.isPublic(),"");
    }

    public Publication convertToPublication(PublicationDTO dto) {
        Publication publication = new Publication();
        publication.setId(dto.id());
        return publication;
    }

}
