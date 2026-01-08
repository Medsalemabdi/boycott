package com.ensi.app.boycott.repository;

import com.ensi.app.boycott.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findFirstByNameIgnoreCaseContaining(String name);
}