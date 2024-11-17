package club.management.club.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TokenNotMatchException extends RuntimeException {
    public TokenNotMatchException(String message) {
        super(message);
    }
}
