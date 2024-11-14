package club.management.club.services.publications.impl;

import club.management.club.dto.PublicationDTO;
import club.management.club.mappers.PublicationMapper;
import club.management.club.repositories.PublicationRepository;
import club.management.club.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class PublicationServiceImpl implements PublicationsService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;


    @Override
    public Page<PublicationDTO> getAllPublications(Pageable pageable) {
        return publicationRepository.findAll(pageable).map(publicationMapper::convertToDTO);
    }
}
