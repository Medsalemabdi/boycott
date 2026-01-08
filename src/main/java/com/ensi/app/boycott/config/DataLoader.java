package com.ensi.app.boycott.config;

import com.ensi.app.boycott.model.Product;
import com.ensi.app.boycott.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(ProductRepository repo) {
        return args -> {

            if (repo.count() == 0) {

                repo.save(new Product(null, "Coca-Cola", true, "Boga Cola"));
                repo.save(new Product(null, "Pepsi", true, "Selecto"));
                repo.save(new Product(null, "Danone", true, "Natilait"));
                repo.save(new Product(null, "Nestlé", true, "Lella"));
                repo.save(new Product(null, "Safia", false, null)); // tunisien, non boycotté

                System.out.println("✔ Initial products loaded");
            }
        };
    }
}
