package edu.mnim2377.gyak_spring.data;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
@Entity
@Data
@Table(name = "comments")
public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        private String text;

        private Timestamp createdAt;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @PrePersist
        protected void onSave() {
            this.createdAt = Timestamp.from(Instant.now());
        }
}
