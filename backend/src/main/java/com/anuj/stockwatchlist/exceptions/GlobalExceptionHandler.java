package com.anuj.stockwatchlist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.anuj.stockwatchlist.dto.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(StockNotFoundException exc) {
        logger.error("StockNotFoundException occured: {}", exc);
        StockErrorResponse err = new StockErrorResponse();
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(MethodArgumentNotValidException exc) {
        logger.error("MethodArgumentNotValidException occured: {}", exc);
        StockErrorResponse err = new StockErrorResponse();
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getBindingResult().getFieldError().getDefaultMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(ConstraintViolationException exc) {
        logger.error("ConstraintViolationException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getConstraintViolations()
                .iterator()
                .next()
                .getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(InvalidSortFieldException exc) {
        logger.error("InvalidSortFieldException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(InvalidDirectionException exc) {
        logger.error("InvalidDirectionException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.BAD_REQUEST);
    }

    // generic exception handler
    // imp for production systems -
    // Without it: Unhandled exceptions leak default Spring responses
    // With it:All errors stay standardized

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<StockErrorResponse>> handleGenericException(Exception exc) {

        logger.error("Unexpected exception occurred: {}", exc.getMessage());

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setMessage("An unexpected error occurred");
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(
                new ApiResponse<>(false, err.getMessage(), err),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(UnauthorizedException exc) {
        logger.error("UnauthorizedException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.FORBIDDEN.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(UserAlreadyExistsException exc) {
        logger.error("UserAlreadyExistsException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.CONFLICT.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(EmailAlreadyExistsException exc) {
        logger.error("EmailAlreadyExistsException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.CONFLICT.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<StockErrorResponse>> handlerException(InvalidCredentialsException exc) {
        logger.error("InvalidCredentialsException occured: {}", exc);

        StockErrorResponse err = new StockErrorResponse();

        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(new ApiResponse<>(false, err.getMessage(), err), HttpStatus.UNAUTHORIZED);
    }

}
