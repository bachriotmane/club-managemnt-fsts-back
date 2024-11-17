package club.management.club.features.mappers;

import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Evenement;
import club.management.club.features.entities.Publication;
import org.springframework.stereotype.Component;


@Component
public class EventsMapper {
    public EventResponseDTO convertToDTO(Evenement evenement) {
        return new EventResponseDTO(evenement.getId(), evenement.getNom(), evenement.getDescription(),evenement.getDate(), evenement.getClub().getNom(),evenement.getClub().getIntegrations().size(),evenement.getClub().getId(),evenement.getLocation(),evenement.getClub().getInstagramme());
    }

    public Evenement convertToEvent(EventResponseDTO eventResponseDTO) {
        return null;
    }

}
