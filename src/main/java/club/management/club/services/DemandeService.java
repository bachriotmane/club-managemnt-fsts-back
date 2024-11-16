package club.management.club.services;

import club.management.club.entities.Demande;
import club.management.club.repositories.DemandeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepo demandeRepo;


    public List<Demande> getAllDemandes() {
        return demandeRepo.findAll();
    }


    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepo.findById(id);
    }


    public Demande saveOrUpdateDemande(Demande demande) {
        return demandeRepo.save(demande);
    }


    public void deleteDemande(Long id) {
        demandeRepo.deleteById(id);
    }
}
