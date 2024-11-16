package club.management.club.features.services.publications.impl;

import club.management.club.features.Specifications.PublicationSpecifications;
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
    public Page<PublicationDTO> getAllPublications(int page, int size, boolean isDesc, boolean isPublic, String keyword, String formDate, String toDate, Long userId) {
        Specification<Publication> publicationSpecifications = PublicationSpecifications.withTitle(keyword);

        LocalDateTime parsedFromDate = formDate == null || formDate.isEmpty() ?  null : LocalDateTime.parse(formDate);
        LocalDateTime parsedToDate = toDate == null || toDate.isEmpty() ?  null : LocalDateTime.parse(toDate);


        if (parsedFromDate != null || parsedToDate != null) {
            publicationSpecifications = Specification.where(publicationSpecifications)
                    .and(PublicationSpecifications.withDateRange(parsedFromDate, parsedToDate));
        }

        Pageable pageable =  PageRequest.of(page, size, Sort.by("date").descending());
        Page<Publication> publicationList = publicationRepository.findAll(publicationSpecifications,pageable);
        if (!isPublic && userId != null) {
            Etudiant etudiant = (Etudiant) userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            List<Publication> filteredPublications = publicationList.stream()
                    .filter(publication -> {
                        if (publication.isPublic()) {
                            return false;
                        }
                        return publication.getClub().getIntegrations().stream()
                                .anyMatch(integration -> integration.getEtudiant().equals(etudiant));
                    })
                    .toList();
            int start = page * size;
            int end = Math.min((start + size), filteredPublications.size());
            List<PublicationDTO> publicationDTOList = filteredPublications.subList(start, end)
                    .stream()
                    .map(publicationMapper::convertToDTO)
                    .collect(Collectors.toList());
            return new PageImpl<>(publicationDTOList, pageable, filteredPublications.size());
        }
        return publicationList.map(publicationMapper::convertToDTO);
    }

}
