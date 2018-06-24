package com.pushdown.automaton.exceptions;

import com.pushdown.automaton.exceptions.codigos.CodigoError;

public class DatosNoValidosException extends Exception {
	public static final String MAL_FORMADA = ", no esta bien formada";

	private static final long serialVersionUID = -7979803280598637482L;
	private CodigoError tipoError;
	
	public DatosNoValidosException(String mensaje, Throwable e){
		super(mensaje, e);
	}
	
	public DatosNoValidosException(CodigoError tipoError){
		super(tipoError.getDescripcion());
		this.tipoError = tipoError;
	}
	
	public DatosNoValidosException(String mensaje, CodigoError tipoError){
		super(mensaje);
		this.tipoError = tipoError;
	}
	
	public CodigoError getCodigoError(){
		return tipoError;
	}
}
