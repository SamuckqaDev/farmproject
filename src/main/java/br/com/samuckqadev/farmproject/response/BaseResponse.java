package br.com.samuckqadev.farmproject.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Classe genérica para padronização de respostas da API.
 * Permite encapsular qualquer objeto, mensagem e status code.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private String message;
    private int statusCode;
    private T data;
    private boolean success;


    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .data(data)
                .message("Operação realizada com sucesso")
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }
    
     public static <T> BaseResponse<T> success(T data, int statusCode, String message) {
        return BaseResponse.<T>builder()
                .data(data)
                .message(message)
                .statusCode(statusCode)
                .success(true)
                .build();
    }


 
    public static <T> BaseResponse<T> success(T data, String message) {
        return BaseResponse.<T>builder()
                .data(data)
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }


    public static <T> BaseResponse<T> created(T data, String message) {
        return BaseResponse.<T>builder()
                .data(data)
                .message(message)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }


    public static <T> BaseResponse<T> error(String message) {
        return BaseResponse.<T>builder()
                .message(message)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .build();
    }

 
    public static <T> BaseResponse<T> error(String message, HttpStatus status) {
        return BaseResponse.<T>builder()
                .message(message)
                .statusCode(status.value())
                .success(false)
                .build();
    }
}