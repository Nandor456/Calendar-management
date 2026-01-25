package edu.mnim2377.gyak_spring.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserData {
    private String name;
    private int age;
    private UUID id;

    public UserData(String name, int age) {
        id = UUID.randomUUID();
        this.name = name;
        this.age = age;
    }

}