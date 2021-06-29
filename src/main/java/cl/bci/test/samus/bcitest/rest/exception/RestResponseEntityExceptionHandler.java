package cl.bci.test.samus.bcitest.rest.exception;

import cl.bci.test.samus.bcitest.exception.EmailExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Map<String, String> handleValidationExceptions(
            WebExchangeBindException ex) {
        log.info("MethodArgumentNotValidException");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EmailExistsException.class})
    public Map<String, String> validEmail(
            EmailExistsException ex) {
        log.info("EmailExistsException");
        log.error(ex.getMessage());
        return mapErrorFromException(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ServerWebInputException.class})
    public Map<String, String> notBody(
            ServerWebInputException ex) {
        log.error(ex.getReason());
        return mapErrorFromException(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleExcepption(
            Exception ex) {
        log.info("Exception");
        log.info(ex.getClass().getCanonicalName());
        log.error(ex.getMessage(), ex);
        return mapErrorFromException(ex);
    }

    private Map<String, String> mapErrorFromException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return errors;
    }

    private Map<String, String> mapErrorFromException(ResponseStatusException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getReason());
        return errors;
    }
}