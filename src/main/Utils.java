package main;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import AP.AutomataPila;
import AP.TransicionIn;
import AP.TransicionOut;

public class Utils<E> {

	public static final int ARGUMENTOS_LINEA0 = 5;
	public static final String SEPARADOR_CAMPOS = ";";
	public static final String SEPARADOR_ELEMENTOS_CAMPO = ",";
	public static final String INICIO_CAMPO = "{";
	public static final String FIN_CAMPO = "}";
	public static final String SEPARADOR_TRANSICIONES = "=";
	public static final Character LAMBDA = '@';
	public static final String SALTO_LINEA = "\n";
	/**
	 * Permite comprobar si un string es nulo o est� formado por espacios en blanco
	 * @param palabra
	 * @return true en caso de que se cumpla alguna de las condiciones
	 */
	public static boolean isBlankOrNull(String palabra){
		if (palabra == null){
			return true;
		} else if (palabra.trim().compareTo("") == 0){
			return true;
		}else
			return false;		
	}	
	
	public static boolean esSolucion(TransicionIn keyTransicion,TransicionIn tranLambda, Stack<Character> pila, int posPalabra, String palabraEntrada, String estadoActual, AutomataPila automata){

		return (pila.isEmpty() && posPalabra == palabraEntrada.length());

	}	
	
	public static boolean esFactible(TransicionOut transicionesSalida, Stack<Character> pila, String palabraEntrada, int posicionCadena){
		
		if (transicionesSalida==null){ //pila.isEmpty() && posicionCadena < palabraEntrada.length() ||
			return false; 
		} else {
			return true;
		}
				
	}
	/**
	 * Devuelve una lista recibiendo un array del objeto
	 * @param array
	 * @return
	 */
	public List<E> AdaptaALista(E array[]){
		return Arrays.asList(array);
	}
	/**
	 * Transform list to array
	 * @param list
	 * @return
	 */
	public E[] AdaptaAArray(List<E> list){
		return (E[]) list.toArray();
	}
}
