package cl.bci.test.samus.bcitest.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(final String email) {
        super(String.format("El correo %s ya está registrado", email));
    }
}
