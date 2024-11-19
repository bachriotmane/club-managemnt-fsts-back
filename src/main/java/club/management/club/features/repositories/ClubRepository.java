package club.management.club.features.repositories;

import club.management.club.features.entities.Club;
import club.management.club.features.enums.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, String> {
    Page<Club> findAll(Specification<Club> spec , Pageable pageable);

    @Query("select c from Club c join c.integrations i where i.memberRole = ?1 and i.etudiant.id = ?2")
    List<Club> findByIntegrationsMemberRoleAndIntegrationsEtudiantId(MemberRole memberRole, String id);

    @Query(
        """
        select c 
        from Club c 
        where c.id not in (
            select i.club.id 
            from Integration i 
            where i.etudiant.email = ?1
        )
        """
    )
    List<Club> findAllClubsWhereUserNotJoined(String email);

}