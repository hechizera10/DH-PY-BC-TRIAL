package com.artxp.artxp.util.exeptions;

public class UnauthorizedException extends RuntimeException {
    private static final String ERROR_MESSAGE = "El usuario no cuenta con los permisos para la acción %s";
    public UnauthorizedException(String mensaje) {
        super(String.format(ERROR_MESSAGE,mensaje));
    }
}