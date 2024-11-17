package club.management.club.features.services.publications.impl;

import club.management.club.features.Specifications.PublicationSpecifications;
import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Integration;
import club.management.club.features.entities.Publication;
import club.management.club.features.mappers.PublicationMapper;
import club.management.club.features.repositories.PublicationRepository;
import club.management.club.features.repositories.UserRepo;
import club.management.club.features.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicationServiceImpl implements PublicationsService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;
    private UserRepo userRepo;


    @Override
    public Page<PublicationDTO> getAllPublications(PublicationsRequest publicationsRequest) {
            Specification<Publication> publicationSpecifications = PublicationSpecifications.withTitle(publicationsRequest.keyword());

            LocalDateTime parsedFromDate = publicationsRequest.fromDate() == null || publicationsRequest.fromDate().isEmpty() ?  null : LocalDateTime.parse(publicationsRequest.fromDate());
            LocalDateTime parsedToDate = publicationsRequest.toDate() == null || publicationsRequest.toDate().isEmpty() ?  null : LocalDateTime.parse(publicationsRequest.toDate());

            publicationSpecifications = Specification.where(publicationSpecifications)
                    .and(PublicationSpecifications.withPublicStatus(publicationsRequest.isPublic()));
            if (parsedFromDate != null || parsedToDate != null) {
                publicationSpecifications = Specification.where(publicationSpecifications)
                        .and(PublicationSpecifications.withDateRange(parsedFromDate, parsedToDate));
            }


            if (!publicationsRequest.isPublic() && publicationsRequest.userId() != null) {
                publicationSpecifications = Specification.where(publicationSpecifications)
                        .and(PublicationSpecifications.privatePublicationsForUser(publicationsRequest.userId()));
            }

        Pageable pageable =  PageRequest.of(publicationsRequest.page(), publicationsRequest.size(), Sort.by("date").descending());
        Page<Publication> publicationList = publicationRepository.findAll(publicationSpecifications,pageable);
            return publicationList.map(publicationMapper::convertToDTO);
    }

    @Override
    public PublicationDTO get(String id) {
        Publication publication = publicationRepository.findById(id).orElseThrow(()-> new RuntimeException("Publication not found!"));
        return publicationMapper.convertToDTO(publication);
    }

}
