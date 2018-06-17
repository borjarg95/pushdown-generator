package com.pushdown.automaton.utils;

public enum CodigosError {
	
	ALFABETO_LENGUAJE("El alfabeto del lenguaje no está correctamente informado."),
	ALFABETO_PILA("El alfabeto de la pila no está correctamente informado."),
	MAL_FORMADA(", no esta bien formada"),
	TRANSICION_DE_LA_LINEA("La transición de la linea ");
	private String value;
	
	CodigosError(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
