package br.com.samuckqadev.farmproject.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.exception.customer.CustomerAlreadyRegistredException;
import br.com.samuckqadev.farmproject.exception.customer.CustomerNotFoundException; // Nova
import br.com.samuckqadev.farmproject.exception.duck.DuckAlreadyExistsException;
import br.com.samuckqadev.farmproject.exception.duck.DuckAlreadySaledException; // Nova
import br.com.samuckqadev.farmproject.exception.duck.DuckMotherNotFoundException;
import br.com.samuckqadev.farmproject.exception.duck.DuckNotFoundException; // Nova
import br.com.samuckqadev.farmproject.exception.seller.SellerCpfAlreadyExists;
import br.com.samuckqadev.farmproject.exception.seller.SellerNotFoundException; // Nova

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler({ RuntimeException.class, IllegalArgumentException.class, IllegalStateException.class })
        public ResponseEntity<BaseResponse<String>> handleBusinessExceptions(RuntimeException ex) {
                var baseResponse = BaseResponse.<String>error(ex.getMessage(), HttpStatus.BAD_REQUEST);
                return ResponseEntity
                                .status(baseResponse.getStatusCode())
                                .body(baseResponse);
        }

        @ExceptionHandler({
                        NoSuchElementException.class,
                        DuckMotherNotFoundException.class,
                        CustomerNotFoundException.class,
                        SellerNotFoundException.class,
                        DuckNotFoundException.class
        })
        public ResponseEntity<BaseResponse<String>> handleNotFoundException(RuntimeException ex) {
                var baseResponse = BaseResponse.<String>error(ex.getMessage(), HttpStatus.NOT_FOUND);
                return ResponseEntity
                                .status(baseResponse.getStatusCode())
                                .body(baseResponse);
        }

        @ExceptionHandler({
                        SellerCpfAlreadyExists.class,
                        CustomerAlreadyRegistredException.class,
                        DuckAlreadyExistsException.class,
                        DuckAlreadySaledException.class
        })
        public ResponseEntity<BaseResponse<String>> handleConflictException(RuntimeException ex) {
                var baseResponse = BaseResponse.<String>error(ex.getMessage(), HttpStatus.CONFLICT);
                return ResponseEntity
                                .status(baseResponse.getStatusCode())
                                .body(baseResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<BaseResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
                String errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                var baseResponse = BaseResponse.<String>error("Validation error: " + errors, HttpStatus.BAD_REQUEST);
                return ResponseEntity
                                .status(baseResponse.getStatusCode())
                                .body(baseResponse);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
                var baseResponse = BaseResponse.<String>error("Erro interno: " + ex.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
                return ResponseEntity
                                .status(baseResponse.getStatusCode())
                                .body(baseResponse);
        }
}