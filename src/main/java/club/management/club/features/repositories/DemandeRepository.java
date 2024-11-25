package club.management.club.features.repositories;

import club.management.club.features.entities.Club;
import club.management.club.features.entities.Demande;
import club.management.club.features.entities.Etudiant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface DemandeRepository extends JpaRepository<Demande, String>, JpaSpecificationExecutor<Demande> {
    @Query("SELECT d.etudiantDemandeur FROM Demande d " +
            "WHERE d.club = :club AND d.type = 'CREATION_CLUB'")
    Etudiant findEtudiantDemandeurbyClub(Club club);
    void deleteByClubId(String uuid);
    @Query("SELECT d FROM Demande d WHERE d.etudiantDemandeur.id = :etudiantId AND d.club.id = :clubId AND d.type = 'INTEGRATION_CLUB'")
    Optional<Demande> findIntegrationDemandeByEtudiantIdAndClubId(@Param("etudiantId") String etudiantId, @Param("clubId") String clubId);
}

