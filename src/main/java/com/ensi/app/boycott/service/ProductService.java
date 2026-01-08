package com.ensi.app.boycott.service;

import com.ensi.app.boycott.model.Product;
import com.ensi.app.boycott.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public Product checkProduct(String name) {
        return repo.findFirstByNameIgnoreCaseContaining(name)
                .orElse(null);
    }
}
