package club.management.club.shared.exceptionHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadRequestException extends RuntimeException {
    private final String errorMessage;
    public BadRequestException(String message) {
        super(message);
        this.errorMessage = message;
    }
}
