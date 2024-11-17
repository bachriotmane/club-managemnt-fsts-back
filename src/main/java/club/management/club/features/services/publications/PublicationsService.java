package club.management.club.features.services.publications;


import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.PublicationDTO;
import org.springframework.data.domain.Page;

public interface PublicationsService {
    Page<PublicationDTO> getAllPublications(PublicationsRequest publicationsRequest);
    PublicationDTO get(String id);

}
