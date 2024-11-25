package club.management.club.features.services.integration;

import club.management.club.features.entities.Integration;

public interface IntegrationService {
    Integration save(Integration integration);

    void deleteById(String id);
    void accepterIntegration(String id);
}
