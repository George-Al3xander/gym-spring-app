package io.github.George_Al3xander.exception;


public class EntityNotFoundException extends RuntimeException {

    private final String entityName;
    private final String identifier;

    public EntityNotFoundException(String entityName, Object identifier) {
        super(String.format("%s with identifier '%s' was not found", entityName, identifier));
        this.entityName = entityName;
        this.identifier = String.valueOf(identifier);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getIdentifier() {
        return identifier;
    }
}

