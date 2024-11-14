package club.management.club.services.publications;

import club.management.club.dto.PublicationDTO;
import club.management.club.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface PublicationsService {
    Page<PublicationDTO> getAllPublications(Pageable pageable);
}
