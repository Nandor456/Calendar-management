package edu.mnim2377.gyak_spring.controller;

import edu.mnim2377.gyak_spring.data.User;
import edu.mnim2377.gyak_spring.repository.JpaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    JpaInterface jpa;

//    @GetMapping
//    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String email) {
//        if (email != null && !email.isEmpty()) {
//            User user = jpa.getUserByEmail(email);
//            return ResponseEntity.ok(Collections.singletonList(user));
//        } else {
//            List<User> users = jpa.findAll();
//            return ResponseEntity.ok(users);
//        }
//    }

    @GetMapping
    public ResponseEntity<List<User>> getUserByCreate() {
        return ResponseEntity.status(HttpStatus.OK).body(jpa.getUsersOrderedByCreation());
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user) {
        User newUser = jpa.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
