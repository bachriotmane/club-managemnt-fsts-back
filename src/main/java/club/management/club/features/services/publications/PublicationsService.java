package club.management.club.features.services.publications;


import club.management.club.features.dto.responses.PublicationDTO;
import org.springframework.data.domain.Page;

public interface PublicationsService {
    Page<PublicationDTO> getAllPublications(int page, int size, boolean isDesc, boolean isPublic , String keyword, String formDate, String toDate);
}
