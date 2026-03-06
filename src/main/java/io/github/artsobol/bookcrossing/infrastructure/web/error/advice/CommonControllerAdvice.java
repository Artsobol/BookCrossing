package io.github.artsobol.bookcrossing.infrastructure.web.error.advice;

import io.github.artsobol.bookcrossing.exception.base.BaseException;
import io.github.artsobol.bookcrossing.infrastructure.localization.MessageService;
import io.github.artsobol.bookcrossing.infrastructure.web.error.dto.ErrorResponse;
import io.github.artsobol.bookcrossing.infrastructure.web.error.dto.ValidationErrorResponse;
import io.github.artsobol.bookcrossing.infrastructure.web.error.dto.ValidationFieldError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final MessageService messageService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<ValidationFieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationFieldError(err.getField(), messageService.resolveValidationMessage(err)))
                .toList();

        String message = messageService.createMessage("validation.error", null);

        ValidationErrorResponse response = new ValidationErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();
        String message = messageService.createMessage(ex.getMessageKey(), ex.getMessageArgs());

        ErrorResponse response = getErrorResponse(request, status, ex.getErrorCode(), message);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = messageService.createMessage("unexpected.error", null);

        ErrorResponse response = getErrorResponse(request, status, "INTERNAL_SERVER_ERROR", message);

        return ResponseEntity.status(status).body(response);
    }

    private static @NonNull ErrorResponse getErrorResponse(
            HttpServletRequest request,
            HttpStatus status,
            String errorCode,
            String message
    ) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                errorCode,
                message,
                request.getRequestURI()
        );
    }
}
