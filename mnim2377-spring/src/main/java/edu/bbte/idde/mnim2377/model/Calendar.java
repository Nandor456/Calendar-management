package edu.bbte.idde.mnim2377.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Calendar extends BaseEntity {
    private final String address;
    private final String location;
    private final LocalDate date;
    private final Boolean online;

    public Calendar(String address, String location, LocalDate date, Boolean online) {
        super();
        this.address = address;
        this.location = location;
        this.date = date;
        this.online = online;
    }

    public Calendar(UUID id, String address, String location, LocalDate date, Boolean online) {
        super(id);
        this.address = address;
        this.location = location;
        this.date = date;
        this.online = online;
    }
}
