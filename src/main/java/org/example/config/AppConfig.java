package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Indica ao Spring que esta é uma classe de configuração
public class AppConfig {

    @Bean // Indica que este método cria um "Bean" (objeto gerenciado pelo Spring)
    public RestTemplate restTemplate() {
        // Retorna uma nova instância do RestTemplate,
        // que o Spring agora pode injetar em outros Services
        return new RestTemplate();
    }
}