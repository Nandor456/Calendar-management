package edu.idde.mnim2377.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@EqualsAndHashCode
public class BaseEntity {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final int ID;

    public BaseEntity() {
        this.ID = COUNTER.incrementAndGet();
    }

    public BaseEntity(int id) {
        this.ID = id;
    }
}
