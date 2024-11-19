package club.management.club.features.services.integration.Impl;

import club.management.club.features.entities.Integration;
import club.management.club.features.repositories.IntegrationRepository;
import club.management.club.features.services.integration.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {
    private final IntegrationRepository integrationRepo ;

    @Override
    public Integration save(Integration integration) {
        return integrationRepo.save(integration);
    }
}
