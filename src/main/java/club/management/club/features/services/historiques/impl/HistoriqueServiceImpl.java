package club.management.club.features.services.historiques.impl;

import club.management.club.features.entities.Historique;
import club.management.club.features.repositories.HistoriqueRepo;
import club.management.club.features.services.historiques.HistoriqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoriqueServiceImpl implements HistoriqueService {
    private final HistoriqueRepo historiqueRepo;
    @Override
    public Historique save(Historique historique) {
        return historiqueRepo.save(historique);
    }
}
