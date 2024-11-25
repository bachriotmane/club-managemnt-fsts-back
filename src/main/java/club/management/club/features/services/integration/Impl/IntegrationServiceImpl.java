package club.management.club.features.services.integration.Impl;

import club.management.club.features.entities.Integration;
import club.management.club.features.repositories.DemandeRepository;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.features.services.integration.IntegrationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {
    private final IntegrationRepository integrationRepo ;
    private final DemandeRepository demandeRepo;
    public void deleteById(String id) {

        integrationRepo.deleteById(id);
    }
    @Override
    public void accepterIntegration(String id) {
        // Récupérer l'intégration par ID
        Integration integration = integrationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration non trouvée"));

        // Mettre à jour l'état de l'intégration (valide)
        integration.setValid(true);

        // Sauvegarder l'intégration mise à jour
        integrationRepo.save(integration);
    }
    @Override
    public Integration save(Integration integration) {
        return integrationRepo.save(integration);
    }
}
