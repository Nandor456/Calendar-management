package edu.idde.mnim2377.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {
    private String email;
    private int age;
    private String name;

    public User(int id, String email, int age, String name) {
        super(id);
        this.email = email;
        this.age = age;
        this.name = name;
    }

    public User(String email, int age, String name) {
        this.email = email;
        this.age = age;
        this.name = name;
    }
}
