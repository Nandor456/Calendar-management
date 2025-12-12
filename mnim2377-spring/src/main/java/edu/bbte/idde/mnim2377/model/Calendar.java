package edu.bbte.idde.mnim2377.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calendar extends BaseEntity {
    private String address;
    private String location;
    private LocalDate date;
    private Boolean online;

    public Calendar(UUID id, String address, String location, LocalDate date, Boolean online) {
        super(id);
        this.address = address;
        this.location = location;
        this.date = date;
        this.online = online;
    }
}
