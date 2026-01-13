package com.app.examproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "keycloak.admin.server-url=http://localhost:9999",
        "keycloak.admin.realm=test",
        "keycloak.admin.client-id=test-client",
        "keycloak.admin.client-secret=test-secret"
})
@ActiveProfiles("test")
@Import({TestJwtConfig.class, TestKeycloakAdminConfig.class})

class ExamProjectApplicationTests {

    @Test
    void contextLoads() {
    }

}

