package club.management.club.shared.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MailDontValidateException extends RuntimeException {
    public MailDontValidateException() {
        super("Please validate your email first.");
    }
}
