package edu.bbte.idde.mnim2377.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "calendar")
public class Calendar extends BaseEntity {

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "is_online")
    private Boolean online;

    public Calendar(UUID id, String address, String location, LocalDate date, Boolean online) {
        super(id);
        this.address = address;
        this.location = location;
        this.date = date;
        this.online = online;
    }
}
