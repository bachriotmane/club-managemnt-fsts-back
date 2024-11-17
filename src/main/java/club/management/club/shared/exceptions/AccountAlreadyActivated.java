package club.management.club.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AccountAlreadyActivated extends RuntimeException {
    public AccountAlreadyActivated(String message) {
        super(message);
    }

}

