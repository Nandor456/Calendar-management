package edu.bbte.idde.mnim2377.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString(includeFieldNames = false)
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {

    private final UUID id;

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    protected BaseEntity(UUID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }
}
