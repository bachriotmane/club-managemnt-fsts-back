package club.management.club.features.mappers;

import club.management.club.features.dto.responses.ClubDetailsResponse;
import club.management.club.features.dto.responses.ProfilsDetailsDto;
import club.management.club.features.entities.Club;
import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.Integration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClubMapper {

    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "nbrMembres", expression = "java(club.getIntegrations().size())")
    @Mapping(target = "profilsDetailsDto", expression = "java(mapProfilsDetails(club))")
    @Mapping(target = "logo",expression = "java(club.getLogo()!= null ? club.getLogo().getId() : null)")
    ClubDetailsResponse toClubDetailsResponse(Club club);

    @Mapping(source = "id", target = "uuid")
    @Mapping(source = "firstName", target = "nom")
    @Mapping(target = "imgProfile",expression = "java(etudiant.getImgProfile()!= null ? etudiant.getImgProfile().getId() : null)")
    ProfilsDetailsDto toProfilsDetailsDto(Etudiant etudiant);

    List<ProfilsDetailsDto> toProfilsDetailsDtoList(List<Etudiant> etudiants);

    default List<ProfilsDetailsDto> mapProfilsDetails(Club club) {
        return club.getIntegrations().stream()
                .map(Integration::getEtudiant)
                .limit(6)
                .map(this::toProfilsDetailsDto)
                .collect(Collectors.toList());
    }
}
