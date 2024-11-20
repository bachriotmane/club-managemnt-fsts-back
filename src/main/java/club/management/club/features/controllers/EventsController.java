package club.management.club.features.controllers;

import club.management.club.features.dto.requests.EventRequest;
import club.management.club.features.dto.responses.EventResponseDTO;
import club.management.club.features.services.events.EventsService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventsController {
    private final EventsService eventsService;

    @GetMapping
    public Page<EventResponseDTO> getPublications(@ModelAttribute EventRequest eventRequest) {
        return eventsService.getAllEvents(eventRequest);
    }

    @GetMapping("/{id}")
    public EventResponseDTO getPublicationById(@PathVariable String id){
        return eventsService.get(id);
    }
    
    @GetMapping("/home-events")
    public List<EventResponseDTO> getHomeEvents(@RequestParam(defaultValue = "7") int limit) {
        return eventsService.getHomeEvents(limit);
    }

    
    
}
