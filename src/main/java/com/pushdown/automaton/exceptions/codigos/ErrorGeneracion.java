package com.pushdown.automaton.exceptions.codigos;

public enum ErrorGeneracion implements CodigoError{
	AUTOMATA_NO_INFORMADO(1, "Para generar el automata se debe incluir una descripción con su definición. "),
	PRIMERA_LINEA_MAL_FORMADA(2,"La primera linea no está bien formada. Consulta la guia de mensajes de entrada. "),
	ALFABETO_LENGUAJE(3,"El alfabeto del lenguaje no está correctamente informado. "),
	ALFABETO_PILA(4,"El alfabeto de la pila no está correctamente informado. "),
	ESTADOS(5,"Los estados del automata no están correctamente informados. "),
	SIMBOLO_PILA(6, "Los simbolos de pila deben ser un carácter. "),
	ESTADO_INICIAL(7, "El estado inicial no esta correctamente informado. ");

	private int codigo;
	private String descripcion;

	private ErrorGeneracion(int code, String desc) {
		codigo = code;
		descripcion = desc;
	}
	
	@Override
	public int getCode() {
		return codigo;
	}

	@Override
	public String getDescripcion() {
		return descripcion;
	}

}
