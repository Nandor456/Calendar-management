package edu.mnim2377.gyak_spring.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Product {

    private final String name;
    private final int price;

    private final String id = UUID.randomUUID().toString();
}
