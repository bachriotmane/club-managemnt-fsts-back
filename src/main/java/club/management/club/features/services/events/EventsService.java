package club.management.club.features.services.events;


import club.management.club.features.dto.requests.EventRequest;
import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.dto.responses.PublicationDTO;
import org.springframework.data.domain.Page;

public interface EventsService {
    Page<EventResponseDTO> getAllEvents(EventRequest eventResponseDTO);
    EventResponseDTO get(String id);

}
