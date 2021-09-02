package business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({DataBaseException.class})
    public ResponseEntity<String> dataBaseExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String payload = "path: " + request.getRequestURI() + "; Database problem detected: " + ex.getMessage();
        return new ResponseEntity<String>(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<String> validationExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String payload = "path: " + request.getRequestURI() + "; Data not valid: " + ex.getMessage();
        return new ResponseEntity<String>(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<String> securityExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String payload = "path: " + request.getRequestURI() + "; Security problem detected: " + ex.getMessage();
        return new ResponseEntity<String>(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> illegalArgumentExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String payload = "path: " + request.getRequestURI() + "; Database problem operation not finished: " + ex.getMessage();
        return new ResponseEntity<String>(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<String> dateTimepParseExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String payload = "path: " + request.getRequestURI() + "; Date not valid: " + ex.getMessage();
        return new ResponseEntity<String>(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MailException.class})
    public ResponseEntity<String> mailExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String payload = "path: " + request.getRequestURI() + "; Something goes wrong mail not sent: " + ex.getMessage();
        return new ResponseEntity<String>(payload, HttpStatus.BAD_REQUEST);
    }
}