package club.management.club.features.mappers;

import club.management.club.features.dto.responses.ClubSimpleDTO;
import club.management.club.features.entities.Club;
import org.springframework.stereotype.Component;

@Component
public class ClubsMapper {
    public ClubSimpleDTO ToClubSimpleDTO(Club club){
        return new ClubSimpleDTO(club.getId(),club.getNom());
    }
}
