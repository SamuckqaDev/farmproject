package br.com.samuckqadev.farmproject.config;

import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.model.Duck;
import br.com.samuckqadev.farmproject.repository.DuckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DuckRepository duckRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Limpa o banco para evitar duplicidade ao reiniciar (opcional em dev)
        duckRepository.deleteAll();

        // 2. Criando a Pata Mãe (Matriarca)
        Duck motherDuck = Duck.builder()
                .name("Mama Duck")
                .status(DuckStatusEnum.AVAILABLE)
                .mother(null) // Ela é a primeira da linhagem
                .build();

        // Precisamos salvar a mãe primeiro para o Postgres gerar o ID
        motherDuck = duckRepository.save(motherDuck);
        System.out.println("Mother Duck saved with ID: " + motherDuck.getIdDuck());

        // 3. Criando os Filhotes vinculados à mãe
        Duck sonDuck = Duck.builder()
                .name("Donald")
                .status(DuckStatusEnum.AVAILABLE)
                .mother(motherDuck) // Vincula o objeto salvo
                .build();

        Duck daughterDuck = Duck.builder()
                .name("Daisy")
                .status(DuckStatusEnum.AVAILABLE)
                .mother(motherDuck)
                .build();

        duckRepository.saveAll(List.of(sonDuck, daughterDuck));

        System.out.println("Family of ducks initialized successfully!");
    }
}