package br.com.gerencimentodepedidos.exception.exceptionresponse;

import java.util.List;

public record ExceptionResponseValidate(String time, String message, List<String> details) {
}
