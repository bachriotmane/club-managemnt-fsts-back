package club.management.club.features.services.publications;


import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Publication;
import org.springframework.data.domain.Page;

public interface PublicationsService {
    Page<PublicationDTO> getAllPublications(PublicationsRequest publicationsRequest);
    PublicationDTO get(String id);
    PublicationDTO create(PublicationDTO publicationDTO);

    PublicationDTO addImageToPublication(String publicationId, String imageId);

    void deletePublicationById(String id);

    PublicationDTO update(String id, PublicationDTO publicationDTO);
}
