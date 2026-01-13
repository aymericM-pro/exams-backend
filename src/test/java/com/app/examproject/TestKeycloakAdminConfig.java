package com.app.examproject;

import org.keycloak.admin.client.Keycloak;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestKeycloakAdminConfig {

    @Bean
    public Keycloak keycloak() {
        return Mockito.mock(Keycloak.class);
    }
}
