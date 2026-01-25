package edu.mnim2377.gyak_spring.controller;

import edu.mnim2377.gyak_spring.data.Product;
import edu.mnim2377.gyak_spring.excpetion.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final Map<String, Product> productMap = new ConcurrentHashMap<>();

    public ProductController() {
        productMap.put("1", new Product("Tej", 300));
        productMap.put("2", new Product("Kenyér", 450));
        productMap.put("3", new Product("Sajt", 1200));
    }

    @GetMapping("/{id}")
    public Product getProducts(@PathVariable String id) {
        if (!productMap.containsKey(id)) {
            throw new NotFoundException("Cannot find id:" + id);
        }
        return productMap.get(id);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handler(NotFoundException ex) {
        // 1. Beállítjuk a státuszkódot 404-re (NOT_FOUND)
        // 2. A body-ba beletesszük a hibaüzenetet
        return ResponseEntity.ok(ex.getMessage());
    }
}
