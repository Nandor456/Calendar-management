package edu.mnim2377.gyak_spring.repository;

import edu.mnim2377.gyak_spring.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaInterface extends JpaRepository<User, Integer> {
    User getUserByEmail(String email);

    @Query("SELECT u from User u ORDER BY u.createdAt DESC")
    List<User> getUsersOrderedByCreation();
}
