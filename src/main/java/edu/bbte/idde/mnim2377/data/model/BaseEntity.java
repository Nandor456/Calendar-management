package edu.bbte.idde.mnim2377.data.model;

public abstract class BaseEntity {
    protected final String id;

    public BaseEntity() {
        this.id = java.util.UUID.randomUUID().toString();
    }
    public String getId() {
        return id;
    }
}
