package com.pushdown.automaton.exceptions;

public class DatosEntradaErroneosException extends Exception {

	private static final long serialVersionUID = -6490403974892412013L;

	public DatosEntradaErroneosException(String mensaje, Throwable e){
		super(mensaje, e);
	}
	
	public DatosEntradaErroneosException(String mensaje){
		super(mensaje);
	}
}
