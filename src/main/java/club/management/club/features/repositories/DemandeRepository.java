package club.management.club.features.repositories;

import club.management.club.features.entities.Demande;
import club.management.club.features.entities.Historique;
import club.management.club.features.enums.TypeDemande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, String>, JpaSpecificationExecutor<Demande> {
    int countByEtudiantDemandeur_Id(String etudiantId);
    int countByEtudiantDemandeurIdAndType(String etudiantId, TypeDemande type);
}

