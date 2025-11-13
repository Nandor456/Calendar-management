package edu.bbte.idde.mnim2377.backend.data.model;

public abstract class BaseEntity {
    protected String id;

    public BaseEntity() {
        this.id = java.util.UUID.randomUUID().toString();
    }

    protected BaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
