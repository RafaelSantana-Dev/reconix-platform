package com.reconix.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReconixAuthApplicationTest {

    @Test
    @DisplayName("Deve carregar o contexto do Auth Service com sucesso")
    void contextLoads() {
    }
}