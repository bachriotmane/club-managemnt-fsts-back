package club.management.club.features.repositories;

import club.management.club.features.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant,String> {
    Optional<Etudiant> findByEmail(String mail);

}
