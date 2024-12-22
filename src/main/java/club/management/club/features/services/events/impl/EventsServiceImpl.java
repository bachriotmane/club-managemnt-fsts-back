package club.management.club.features.services.events.impl;

import club.management.club.features.Specifications.EventsSpecifications;
import club.management.club.features.dto.requests.EventRequest;
import club.management.club.features.dto.responses.EventClubChartDTO;
import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Evenement;
import club.management.club.features.entities.Image;
import club.management.club.features.entities.Publication;
import club.management.club.features.mappers.EventsMapper;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.repositories.EventRepository;
import club.management.club.features.repositories.ImageRepository;
import club.management.club.features.services.events.EventsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final EventRepository eventRepository;
    private final EventsMapper eventsMapper;
    private final ClubRepository clubRepository;
    private final ImageRepository imageRepository;
    
    @Override
    public void accepterEvenement(String id) {
        // Récupérer l'événement par ID
        Evenement evenement = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        // Mettre à jour l'état de l'événement (validé)
        evenement.setValid(true);

        // Sauvegarder l'événement mis à jour
        eventRepository.save(evenement);
    }

    @Override
    public List<EventClubChartDTO> getEventsCountForClubs(int year) {
        return clubRepository.findAll().stream()
                .map(club -> {
                    long clubCount = club.getEvenements().stream()
                            .filter(evenement -> evenement.getDate().getYear() == year)
                            .count();
                    return new EventClubChartDTO(club.getId(), club.getNom(), (int) clubCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<EventResponseDTO> getAllEvents(EventRequest eventRequest){
            Specification<Evenement> eventsSpecifications = EventsSpecifications.withTitle(eventRequest.keyword());

            LocalDateTime parsedFromDate = eventRequest.fromDate() == null || eventRequest.fromDate().isEmpty() ?  null : LocalDateTime.parse(eventRequest.fromDate());
            LocalDateTime parsedToDate = eventRequest.toDate() == null || eventRequest.toDate().isEmpty() ?  null : LocalDateTime.parse(eventRequest.toDate());


            if (parsedFromDate != null || parsedToDate != null) {
                eventsSpecifications = Specification.where(eventsSpecifications)
                        .and(EventsSpecifications.withDateRange(parsedFromDate, parsedToDate));
            }

        eventsSpecifications = Specification.where(eventsSpecifications)
                .and(EventsSpecifications.isValid());

        Pageable pageable =  PageRequest.of(eventRequest.page(), eventRequest.size(), Sort.by("date").descending());
        Page<Evenement> events = eventRepository.findAll(eventsSpecifications,pageable);
            return events.map(eventsMapper::convertToDTO);
    }

    @Override
    public EventResponseDTO get(String id) {
        Evenement evenement = eventRepository.findById(id).orElseThrow(()-> new RuntimeException("Events not found!"));
        if(!evenement.isValid()){
            throw new RuntimeException("The event is not valide");
        }
        return eventsMapper.convertToDTO(evenement);
    }
    
    @Override
    public List<EventResponseDTO> getHomeEvents(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("date").descending());
        Page<Evenement> events = eventRepository.findAll(pageable);
        return events.map(eventsMapper::convertToDTO).getContent();
    }


    @Override
    public Evenement save(Evenement event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(String id) {
        Evenement evenement = eventRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Event with ID " + id + " not found."));
        String clubId = evenement.getClub().getId();
        Club club = clubRepository.findById(clubId).orElseThrow(()->new IllegalArgumentException("Club with ID " + clubId + " not found."));
        club.getEvenements().removeIf((c)-> Objects.equals(c.getId(), id));
        clubRepository.save(club);
        evenement.setClub(null);
        eventRepository.save(evenement);
        eventRepository.deleteById(id);
    }

    @Override
    public EventResponseDTO update(String id, EventResponseDTO eventResponseDTO) {
        Evenement evenement = eventRepository.findById(id).orElseThrow(()->new IllegalArgumentException("event with ID " + id + " not found."));
        evenement.setNom(eventResponseDTO.nom());
        evenement.setDescription(eventResponseDTO.description());
        evenement.setDate(eventResponseDTO.date());
        evenement.setLocation(eventResponseDTO.location());

        if(eventResponseDTO.imageId() == null){
            evenement.setImage(null);
        }
        else {
            Image image = imageRepository.findById(eventResponseDTO.imageId())
                    .orElseThrow(() -> new IllegalArgumentException("Image not found"));
            evenement.setImage(image);
        }
        return eventsMapper.convertToDTO(eventRepository.save(evenement));
    }

    @Override
    @Transactional
    public EventResponseDTO addImageToEvent(String eventId, String imageId) {
        Evenement evenement = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image  not found"));
        if(evenement.getImage() != null){
            evenement.setImage(null);
            eventRepository.save(evenement);
            imageRepository.deleteById(image.getId());
        }
        evenement.setImage(image);
        return eventsMapper.convertToDTO(eventRepository.save(evenement));
    }

    @Override
    public void deleteById(String id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Événement avec ID " + id + " non trouvé");
        }
        eventRepository.deleteById(id);
    }
}
