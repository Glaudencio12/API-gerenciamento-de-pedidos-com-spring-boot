package br.com.gerencimentodepedidos.exception;

import java.util.Date;

public record ExceptionResponse(Date time, String message, String details) {
}
