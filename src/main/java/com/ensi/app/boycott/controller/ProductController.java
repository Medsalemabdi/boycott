package com.ensi.app.boycott.controller;

import com.ensi.app.boycott.ai.AiResult;
import com.ensi.app.boycott.ai.HuggingFaceChatService;
import com.ensi.app.boycott.model.Product;
import com.ensi.app.boycott.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductRepository repo;
    private final HuggingFaceChatService aiService;

    public ProductController(ProductRepository repo,
                             HuggingFaceChatService aiService) {
        this.repo = repo;
        this.aiService = aiService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam String name) {

        // 1️⃣ Explicit DB lookup
        Optional<Product> productOpt =
                repo.findFirstByNameIgnoreCaseContaining(name);

        // 2️⃣ If found → return Product
        if (productOpt.isPresent()) {
            return ResponseEntity.ok(productOpt.get());
        }

        // 3️⃣ Else → fallback to AI
        AiResult aiResult = aiService.analyzeProduct(name);
        return ResponseEntity.ok(aiResult);
    }

    // Alias endpoint
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String name) {
        return check(name);
    }
}
