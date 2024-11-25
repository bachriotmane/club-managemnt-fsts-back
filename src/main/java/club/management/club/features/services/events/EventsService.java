package club.management.club.features.services.events;


import club.management.club.features.dto.requests.EventRequest;
import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.dto.responses.PublicationDTO;

import java.util.List;

import club.management.club.features.entities.Evenement;
import org.springframework.data.domain.Page;

public interface EventsService {
    Page<EventResponseDTO> getAllEvents(EventRequest eventResponseDTO);
    EventResponseDTO get(String id);
    List<EventResponseDTO> getHomeEvents(int limit);
    public Evenement save(Evenement event);

    void deleteEvent(String id);

    EventResponseDTO update(String id, EventResponseDTO eventResponseDTO);

    EventResponseDTO addImageToEvent(String eventId, String imageId);
}
