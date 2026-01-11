package edu.bbte.idde.mnim2377.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(
            mappedBy = "calendar",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Event> events = new ArrayList<>();

    public Calendar(UUID id, String address, String location, LocalDate date, Boolean online) {
        super(id);
        this.address = address;
        this.location = location;
        this.date = date;
        this.online = online;
    }

    public void addEvent(Event event) {
        if (event == null) {
            return;
        }
        events.add(event);
        event.setCalendar(this);
    }

    public boolean removeEventById(UUID eventId) {
        if (eventId == null) {
            return false;
        }

        for (var iterator = events.iterator(); iterator.hasNext(); ) {
            Event e = iterator.next();
            if (eventId.equals(e.getId())) {
                e.setCalendar(null);
                iterator.remove();
                return true;
            }
        }

        return false;
    }
}
