package club.management.club.features.mappers;

import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Publication;
import org.springframework.stereotype.Component;


@Component
public class PublicationMapper {
    public PublicationDTO convertToDTO(Publication publication) {
        return new PublicationDTO(publication.getId(), publication.getTitle(), publication.getPubDesc(),publication.getDate(), publication.isPublic(),publication.getClub().getNom(), publication.getClub().getIntegrations().size(),publication.getClub().getId() );
    }

    public Publication convertToPublication(PublicationDTO dto) {
        Publication publication = new Publication();
        publication.setId(dto.id());
        return publication;
    }

}
