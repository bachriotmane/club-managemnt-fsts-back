package club.management.club.services.publications;

import club.management.club.dto.PublicationDTO;
import club.management.club.entities.Publication;

import java.util.Set;

public interface PublicationsService {
    Set<PublicationDTO> getAllPublications();
}
