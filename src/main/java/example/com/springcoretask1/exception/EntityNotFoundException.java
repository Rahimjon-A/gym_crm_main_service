package example.com.springcoretask1.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String msg) { super(msg); }
}