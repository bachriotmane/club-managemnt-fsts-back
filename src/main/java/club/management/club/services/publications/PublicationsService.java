package club.management.club.services.publications;

import club.management.club.dto.PublicationDTO;
import club.management.club.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface PublicationsService {
    Page<PublicationDTO> getAllPublications(int page, int size, boolean isDesc, boolean isPublic , String keyword, String formDate, String toDate);
}