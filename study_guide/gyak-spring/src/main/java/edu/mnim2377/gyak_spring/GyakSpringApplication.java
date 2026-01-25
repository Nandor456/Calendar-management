package edu.mnim2377.gyak_spring;

import edu.mnim2377.gyak_spring.data.Comment;
import edu.mnim2377.gyak_spring.data.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GyakSpringApplication {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mypersistance");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // --- Create users ---
            User u1 = new User();
            u1.setEmail("user1@example.com");

            User u2 = new User();
            u2.setEmail("user2@example.com");

            em.persist(u1);
            em.persist(u2);

            // --- Create comments ---
            Comment c1 = new Comment();
            c1.setText("Hello from user1");
            c1.setUser(u1);
            u1.getComments().add(c1); // optional, for bidirectional consistency

            Comment c2 = new Comment();
            c2.setText("Another comment by user1");
            c2.setUser(u1);
            u1.getComments().add(c2);

            Comment c3 = new Comment();
            c3.setText("Comment by user2");
            c3.setUser(u2);
            u2.getComments().add(c3);

            // Persist comments explicitly
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);

            em.getTransaction().commit();

            // --- Query users with comments ---
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u LEFT JOIN FETCH u.comments ORDER BY u.createdAt ASC", User.class);
            List<User> users = query.getResultList();

            for (User u : users) {
                System.out.println("User: " + u.getEmail() + " (createdAt=" + u.getCreatedAt() + ")");
                for (Comment c : u.getComments()) {
                    System.out.println("  Comment: " + c.getText() + " (createdAt=" + c.getCreatedAt() + ")");
                }
            }

        } finally {
            em.close();
            emf.close();
        }
    }
}
