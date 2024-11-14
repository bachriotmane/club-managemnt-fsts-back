package club.management.club.services.publications.impl;

import club.management.club.dto.PublicationDTO;
import club.management.club.repositories.PublicationRepository;
import club.management.club.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class PublicationServiceImpl implements PublicationsService {
    private final PublicationRepository publicationRepository;
    @Override
    public Set<PublicationDTO> getAllPublications() {
        return Set.of();
    }
}
