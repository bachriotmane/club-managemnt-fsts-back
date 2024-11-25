package club.management.club.features.services.publications.impl;

import club.management.club.features.Specifications.PublicationSpecifications;
import club.management.club.features.dto.requests.PublicationsRequest;
import club.management.club.features.dto.responses.PublicationDTO;
import club.management.club.features.entities.*;
import club.management.club.features.mappers.PublicationMapper;
import club.management.club.features.repositories.ClubRepository;
import club.management.club.features.repositories.ImageRepository;
import club.management.club.features.repositories.PublicationRepository;
import club.management.club.features.services.publications.PublicationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PublicationServiceImpl implements PublicationsService {
    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;
    private final ClubRepository clubRepository;
    private final ImageRepository imageRepository;


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

    @Override
    public PublicationDTO create(PublicationDTO publicationDTO) {
        if(publicationDTO.clubId() == null || publicationDTO.clubId().isEmpty()){
            throw new RuntimeException("Club id is required!");
        }

        if(publicationDTO.title() == null || publicationDTO.title().isEmpty()){
            throw new RuntimeException("Title is required!");
        }

        if(publicationDTO.description() == null || publicationDTO.description().isEmpty()){
            throw new RuntimeException("Content is required!");
        }
        Club club = clubRepository.findById(publicationDTO.clubId()).orElseThrow(()-> new RuntimeException("Club not found!"));
        Publication publication = publicationMapper.convertToPublication(publicationDTO);
        publication.setClub(club);
        publication.setDate(LocalDateTime.now());
        return publicationMapper.convertToDTO(publicationRepository.save(publication));
    }

    @Override
    @Transactional
    public PublicationDTO addImageToPublication(String publicationId, String imageId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
        if(publication.getImage() != null){
            publication.setImage(null);
            publicationRepository.save(publication);
            imageRepository.deleteById(image.getId());
        }
        publication.setImage(image);
        return publicationMapper.convertToDTO(publicationRepository.save(publication));
    }


    @Override
    @Transactional
    public void deletePublicationById(String id) {
        Publication publication = publicationRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Publication with ID " + id + " not found."));
        String clubId = publication.getClub().getId();
        Club club = clubRepository.findById(clubId).orElseThrow(()->new IllegalArgumentException("Club with ID " + clubId + " not found."));
        club.getPublications().removeIf((c)-> Objects.equals(c.getId(), id));
        clubRepository.save(club);
        publication.setClub(null);
        publicationRepository.save(publication);
        publicationRepository.deleteById(id);
    }

    @Override
    public PublicationDTO update(String id, PublicationDTO publicationDTO) {
        Publication publication = publicationRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Publication with ID " + id + " not found."));
        publication.setTitle(publicationDTO.title());
        publication.setPubDesc(publicationDTO.description());
        publication.setPublic(publicationDTO.isPublic());
        if(publicationDTO.imageId() == null){
            publication.setImage(null);
        }
        return publicationMapper.convertToDTO(publicationRepository.save(publication));
    }
}
