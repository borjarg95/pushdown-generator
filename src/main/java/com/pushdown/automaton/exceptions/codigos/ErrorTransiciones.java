package com.pushdown.automaton.exceptions.codigos;

public enum ErrorTransiciones implements CodigoError{
	TRANSICION_DE_LA_LINEA(10, "La transición de la linea "),
	TRANSICION_DUPLICADA(11, "No puedes introducir transiciones repetidas. Linea: "),
	TRANSICION_IN_MAL_DEFINIDA_EN_LINEA(12, "TransicionIn mal definida en linea: "),
	TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA(13, "TransicionOut mal definida en linea: ");
	private int codigo;
	private String descripcion;
	private ErrorTransiciones(int code, String desc) {
		this.codigo = code;
		this.descripcion = desc;
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
