package club.management.club.features.repositories;

import club.management.club.features.entities.Integration;
import club.management.club.features.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationRepository extends JpaRepository<Integration,String> , JpaSpecificationExecutor<Integration> {
    List<Integration> findAllByEtudiantId(String id);
    Optional<Integration> findByEtudiantIdAndClubId(String etudiantId, String clubUuid);
    @Query("SELECT COUNT(i) FROM Integration i WHERE i.memberRole = :role AND i.club.id = :clubId")
    long countAdminsInClub(@Param("clubId") String clubId, @Param("role") MemberRole role);
}
