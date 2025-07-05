package br.com.gerencimentodepedidos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestWithValueIsNullExeception extends RuntimeException {
    public RequestWithValueIsNullExeception(String message) {
        super(message);
    }
}
