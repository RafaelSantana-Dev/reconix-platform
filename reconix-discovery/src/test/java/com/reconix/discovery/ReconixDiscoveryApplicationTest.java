package com.reconix.discovery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReconixDiscoveryApplicationTest {

    @Test
    @DisplayName("Deve carregar o contexto do Eureka Server com sucesso")
    void contextLoads() {
        // Se o contexto do Spring subir sem erro, o teste passa.
    }
}