package service.interfaces;

public interface Validatable<T> {

    void validate();

    // default method
    default void validateOrThrow(RuntimeException ex) {
        try {
            validate();
        } catch (IllegalArgumentException e) {
            throw ex;
        }
    }

    // static method
    static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
