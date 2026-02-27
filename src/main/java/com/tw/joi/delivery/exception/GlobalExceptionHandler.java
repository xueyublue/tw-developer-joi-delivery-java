package com.tw.joi.delivery.exception;

import com.tw.joi.delivery.dto.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ApiError> handleStoreNotFound(StoreNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = buildApiError(HttpStatus.NOT_FOUND, ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    private ApiError buildApiError(HttpStatus status, Exception ex, HttpServletRequest request) {
        return new ApiError(
                Instant.now(), status.value(), status.getReasonPhrase(), ex.getMessage(), request.getRequestURI()
        );
    }

}
