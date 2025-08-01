package br.com.gerencimentodepedidos.exception.handler;

import br.com.gerencimentodepedidos.exception.exceptionresponse.ExceptionResponse;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.exception.exceptionresponse.ExceptionResponseValidate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomEntityResponseHandler {

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String dateFormated = now.format(dateTimeFormatter);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handlerAllExceptions(Exception e, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(dateFormated, e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handlerNotFoundException(Exception e, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(dateFormated, e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponseValidate> handlerValidationException(MethodArgumentNotValidException ex){
        List<String> erros = new ArrayList<>();

        for (FieldError erro : ex.getBindingResult().getFieldErrors()){
            String details = erro.getField() + " : " + erro.getDefaultMessage();
            erros.add(details);
        }

        ExceptionResponseValidate response = new ExceptionResponseValidate(dateFormated, "Erro de validação", erros);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
