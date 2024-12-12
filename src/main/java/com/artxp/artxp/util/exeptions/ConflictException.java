package com.artxp.artxp.util.exeptions;

//conflicto al realizar una operación
public class ConflictException extends RuntimeException{
    private static final String ERROR_MESSAGE = "Ha ocurrido un error: %s";

    public ConflictException(String mensaje) {
        super(String.format(ERROR_MESSAGE,mensaje));
    }
}