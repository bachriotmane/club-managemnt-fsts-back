package club.management.club.features.repositories;

import club.management.club.features.entities.Etudiant;
import club.management.club.features.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant,String> {
    Optional<Etudiant> findByEmail(String mail);

    boolean existsByEmail(String email);

    Page<Etudiant> findAll(Specification<Etudiant> spec, Pageable pageable);

    boolean existsByCin(String cin);

    boolean existsByCne(String cne);
    @Query("SELECT e FROM Etudiant e JOIN e.integrations i " +
            "WHERE i.club.id = :uuidClub AND i.isValid = true")
    List<Etudiant> findByClubIdAndValid(String uuidClub);
}
