package club.management.club.features.services.publications.impl;

import club.management.club.features.Specifications.PublicationSpecifications;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Publication;
import club.management.club.features.mappers.PublicationMapper;
import club.management.club.features.repositories.PublicationRepository;
import club.management.club.features.services.publications.PublicationsService;
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
public class PublicationServiceImpl implements PublicationsService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;


    @Override
    public Page<PublicationDTO> getAllPublications(int page, int size, boolean isDesc, boolean isPublic, String keyword, String formDate, String toDate) {
        Specification<Publication> publicationSpecifications = PublicationSpecifications.withTitle(keyword);

        LocalDateTime parsedFromDate = formDate == null || formDate.isEmpty() ?  null : LocalDateTime.parse(formDate);
        LocalDateTime parsedToDate = toDate == null || toDate.isEmpty() ?  null : LocalDateTime.parse(toDate);


        if (parsedFromDate != null || parsedToDate != null) {
            publicationSpecifications = Specification.where(publicationSpecifications)
                    .and(PublicationSpecifications.withDateRange(parsedFromDate, parsedToDate));
        }

        Pageable pageable =  PageRequest.of(page, size, Sort.by("date").descending());
        return publicationRepository.findAll(publicationSpecifications,pageable).map(publicationMapper::convertToDTO);
    }

}
