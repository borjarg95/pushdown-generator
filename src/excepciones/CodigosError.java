package excepciones;

public enum CodigosError {
	
	ALFABETO_LENGUAJE("El alfabeto del lenguaje no está correctamente informado."),
	ALFABETO_PILA("El alfabeto de la pila no está correctamente informado.");
	
	private String value;
	
	CodigosError(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
