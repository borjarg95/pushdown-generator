package excepciones;

public class AlfabetoNoValidoException extends Exception {

	private static final String ALFABETO_LENGUAJE = "El alfabeto del lenguaje no está correctamente informado.";
	private static final String ALFABETO_PILA = "El alfabeto de la pila no está correctamente informado.";
	private static final long serialVersionUID = -7979803280598637482L;

	public AlfabetoNoValidoException(String mensaje, Throwable e){
		super(mensaje, e);
	}
	
	public AlfabetoNoValidoException(String mensaje){
		super(mensaje);
	}
}
