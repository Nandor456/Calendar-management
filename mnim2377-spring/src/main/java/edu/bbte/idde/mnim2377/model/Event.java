package edu.bbte.idde.mnim2377.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "event")
public class Event extends BaseEntity {

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id", nullable = false)
    @JsonIgnore
    private Calendar calendar;

    public Event(UUID id, String title, LocalTime startTime, LocalTime endTime, Calendar calendar) {
        super(id);
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendar = calendar;
    }
}

