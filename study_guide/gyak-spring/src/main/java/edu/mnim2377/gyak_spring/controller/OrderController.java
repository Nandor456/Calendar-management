package edu.mnim2377.gyak_spring.controller;

import edu.mnim2377.gyak_spring.data.Product;
import edu.mnim2377.gyak_spring.excpetion.InvalidOrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private List<String> list = List.of("alma", "sajt", "tej");

    @PostMapping
    public ResponseEntity<Product> postOrders(@RequestBody Product product) {
        if (!list.contains(product.getName())) {
            log.info("Name doesnt exist");
            throw new InvalidOrderException("name dosnt exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
