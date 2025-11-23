package edu.bbte.idde.mnim2377.backend.data.model;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseEntity {
    protected final UUID id;

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    protected BaseEntity(UUID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public UUID getId() {
        return id;
    }

}
