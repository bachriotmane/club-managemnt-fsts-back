package club.management.club.features.services.events.impl;

import club.management.club.features.Specifications.EventsSpecifications;
import club.management.club.features.dto.requests.EventRequest;
import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.entities.Evenement;
import club.management.club.features.mappers.EventsMapper;
import club.management.club.features.repositories.EventRepository;
import club.management.club.features.services.events.EventsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final EventRepository eventRepository;
    private final EventsMapper eventsMapper;


    @Override
    public Page<EventResponseDTO> getAllEvents(EventRequest eventRequest){
            Specification<Evenement> eventsSpecifications = EventsSpecifications.withTitle(eventRequest.keyword());

            LocalDateTime parsedFromDate = eventRequest.fromDate() == null || eventRequest.fromDate().isEmpty() ?  null : LocalDateTime.parse(eventRequest.fromDate());
            LocalDateTime parsedToDate = eventRequest.toDate() == null || eventRequest.toDate().isEmpty() ?  null : LocalDateTime.parse(eventRequest.toDate());


            if (parsedFromDate != null || parsedToDate != null) {
                eventsSpecifications = Specification.where(eventsSpecifications)
                        .and(EventsSpecifications.withDateRange(parsedFromDate, parsedToDate));
            }

        Pageable pageable =  PageRequest.of(eventRequest.page(), eventRequest.size(), Sort.by("date").descending());
        Page<Evenement> events = eventRepository.findAll(eventsSpecifications,pageable);
            return events.map(eventsMapper::convertToDTO);
    }

    @Override
    public EventResponseDTO get(String id) {
        Evenement publication = eventRepository.findById(id).orElseThrow(()-> new RuntimeException("Events not found!"));
        return eventsMapper.convertToDTO(publication);
    }

}
