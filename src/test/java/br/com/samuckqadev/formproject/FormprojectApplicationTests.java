package br.com.samuckqadev.formproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Adicionamos a referência explícita à sua classe principal
@SpringBootTest(classes = FormprojectApplicationTests.class)
class FormprojectApplicationTests {

    @Test
    void contextLoads() {
        // Se este teste passar, significa que seu banco, liquibase 
        // e todas as dependências estão configuradas corretamente.
    }

}