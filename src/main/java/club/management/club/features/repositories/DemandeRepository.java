package club.management.club.features.repositories;

import club.management.club.features.entities.Club;
import club.management.club.features.dto.responses.DemandeDTO;
import club.management.club.features.entities.Demande;
import club.management.club.features.entities.Etudiant;

import club.management.club.features.entities.User;
import club.management.club.features.entities.Historique;
import club.management.club.features.enums.TypeDemande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface DemandeRepository extends JpaRepository<Demande, String>, JpaSpecificationExecutor<Demande> {
    int countByEtudiantDemandeur_Id(String etudiantId);
    int countByEtudiantDemandeurIdAndType(String etudiantId, TypeDemande type);
    @Query("SELECT d.etudiantDemandeur FROM Demande d " +
            "WHERE d.club = :club AND d.type = 'CREATION_CLUB'")
    Etudiant findEtudiantDemandeurbyClub(Club club);
    void deleteByClubId(String uuid);
    int countByTypeAndClubIdIn(TypeDemande type, List<String> clubIds);
    @Query("SELECT d FROM Demande d WHERE d.etudiantDemandeur.id = :etudiantId AND d.club.id = :clubId AND (d.type = 'INTEGRATION_CLUB' OR d.type = 'CREATION_CLUB')")
    Optional<Demande> findIntegrationDemandeByEtudiantIdAndClubId(@Param("etudiantId") String etudiantId, @Param("clubId") String clubId);

    List<Demande> findByEtudiantDemandeurId(String demandeurId);

    Collection<Demande> findAllByEtudiantDemandeur(User student);
    List<Demande> findByIntegrationId(String integration_id);
}

