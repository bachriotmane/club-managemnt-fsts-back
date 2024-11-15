package club.management.club.services.publications.impl;

import club.management.club.dto.PublicationDTO;
import club.management.club.mappers.PublicationMapper;
import club.management.club.repositories.PublicationRepository;
import club.management.club.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublicationServiceImpl implements PublicationsService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;


    @Override
    public Page<PublicationDTO> getAllPublications(int page, int size, boolean isDesc, boolean isPublic, String keyword, String formDate, String toDate) {
        Pageable pageable =  PageRequest.of(page, size, Sort.by("date").descending());
        return publicationRepository.findAll(pageable).map(publicationMapper::convertToDTO);
    }

}
