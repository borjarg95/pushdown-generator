package com.pushdown.automaton.exceptions;

public class AlfabetoNoValidoException extends Exception {

	private static final long serialVersionUID = -7979803280598637482L;

	public AlfabetoNoValidoException(String mensaje, Throwable e){
		super(mensaje, e);
	}
	
	public AlfabetoNoValidoException(String mensaje){
		super(mensaje);
	}
}
