package br.com.samuckqadev.farmproject.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.exception.seller.SellerCpfAlreadyExists;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Manipulador global de exceções seguindo o padrão de projeto Farm API.
 * Centraliza o tratamento de erros e padroniza o retorno via BaseResponse.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura exceções de negócio e erros comuns de runtime.
     */
    @ExceptionHandler({ RuntimeException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<BaseResponse<String>> handleBusinessExceptions(RuntimeException ex) {
        var baseResponse = BaseResponse.<String>error(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity
                .status(baseResponse.getStatusCode())
                .body(baseResponse);
    }

    /**
     * Captura exceções de recursos não encontrados (404 Not Found).
     */
    @ExceptionHandler({ NoSuchElementException.class })
    public ResponseEntity<BaseResponse<String>> handleNotFoundException(RuntimeException ex) {
        var baseResponse = BaseResponse.<String>error(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity
                .status(baseResponse.getStatusCode())
                .body(baseResponse);
    }

    /**
     * Captura exceções específicas de conflito (ex: CPF duplicado).
     */
    @ExceptionHandler({ SellerCpfAlreadyExists.class })
    public ResponseEntity<BaseResponse<String>> handleConflictException(RuntimeException ex) {
        var baseResponse = BaseResponse.<String>error(ex.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity
                .status(baseResponse.getStatusCode())
                .body(baseResponse);
    }

    /**
     * Captura erros de validação do Bean Validation (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var baseResponse = BaseResponse.<String>error("Erro de validação: " + errors, HttpStatus.BAD_REQUEST);
        return ResponseEntity
                .status(baseResponse.getStatusCode())
                .body(baseResponse);
    }

    /**
     * Fallback para qualquer outro erro inesperado no servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
        var baseResponse = BaseResponse.<String>error("Erro interno: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(baseResponse.getStatusCode())
                .body(baseResponse);
    }
}