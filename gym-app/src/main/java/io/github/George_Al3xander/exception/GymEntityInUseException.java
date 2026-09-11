package io.github.George_Al3xander.exception;

public class GymEntityInUseException extends RuntimeException {

    public GymEntityInUseException(String entityName, String entityIdentifier,
                                   String referencedEntityName, String referencedEntityIdentifier) {
        super(String.format(
                "Cannot delete %s [%s] because it is referenced by %s [%s].",
                entityName,
                entityIdentifier,
                referencedEntityName,
                referencedEntityIdentifier
        ));
    }
}