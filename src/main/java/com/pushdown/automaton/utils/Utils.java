package com.pushdown.automaton.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import com.pushdown.automaton.model.AutomataPila;
import com.pushdown.automaton.model.TransicionIn;
import com.pushdown.automaton.model.TransicionOut;

public class Utils <E>{

	public static final int ARGUMENTOS_LINEA0 = 5;
	public static final String SEPARADOR_CAMPOS = ";";
	public static final String SEPARADOR_ELEMENTOS_CAMPO = ",";
	public static final String INICIO_CAMPO = "{";
	public static final String FIN_CAMPO = "}";
	public static final String SEPARADOR_TRANSICIONES = "=";
	public static final Character LAMBDA = '@';
	public static final String SALTO_LINEA = "\n";
	public static final int TAMANIO_MAPA_AUTOMATAS_GENERADOS = 350;
	public static final int ALTURA_MAXIMA_ARBOL_BACKTRAKING = 500;

	public static boolean esSolucion(Deque<Character> pila, int posPalabra, String palabraEntrada){

		return (pila.isEmpty() && posPalabra == palabraEntrada.length());
	}	
	
	/**
	 * Dado 
	 * @param automata
	 * @param tranEntrada
	 * @param tranLambda
	 * comprueba en la situción del automata únicamente se pueda realizar la transición lambda. 
	 * Esto implica que BT se realiza sobre la misma posición de la cadena de entrada, es decir, se itera sin leer un nuevo caracter de la palabra
	 * @return
	 */
	public static boolean esTransicionEntradaSoloLambda(AutomataPila automata, TransicionIn tranEntrada, TransicionIn tranLambda) {
		return (tranLambda !=null && (automata.getFuncionesTransicion().get(tranEntrada) == null)) 
				|| ((tranLambda == null) && (tranEntrada.getSimbEntrada() == Utils.LAMBDA))
				|| tranEntrada.equals(tranLambda);
	}
	
	public static boolean esTransicionLambda(AutomataPila automata, TransicionIn tranEntrada, TransicionIn tranLambda, TransicionOut tranSalida) {
		List<TransicionOut> transicionesLambdaAux = automata.getFuncionesTransicion().get(new TransicionIn(tranEntrada.getEstado(), LAMBDA, tranEntrada.getSimbCabezaPila()));
		return (tranLambda != null && (automata.getFuncionesTransicion().get(tranEntrada) == null) 
				|| ((tranLambda == null) && tranEntrada.getSimbEntrada() == Utils.LAMBDA))
				|| tranEntrada.equals(tranLambda)
				|| (!esCollecionVaciaONull(transicionesLambdaAux) && transicionesLambdaAux.contains(tranSalida));
	}
	
	
	public static <E> boolean esCollecionVaciaONull(Collection<E> coleccion){
		return coleccion == null 
				|| coleccion.isEmpty() 
				|| (coleccion.size() == 1 && coleccion.iterator().next() == null);
	}
	
	public static Character recuperaCaracter(int posicion, String palabra) {
		return palabra.isEmpty() || posicion >= palabra.length() ? LAMBDA : palabra.charAt(posicion);
	}
	
	/**
	 * El siguiente método corrige los posibles
	 * carácteres erróneos que podemos encontrar 
	 * en la transformación mediante json.
	 * @param hexaString
	 * @return
	 */
	public static String correctorCharEspeciales(String hexaString){
		
		String correccion = hexaString.replace('+', ' ');
		
		correccion = correccion.replace("\r", "");	
		
		if (hexaString.contains("%")){ /*ahorramos computo si no existe ningún %, 
										ya que forma parte de las cadenas de caracteres especiales*/
			correccion = correccion.replace("%C3%B1", "ñ"); 
			correccion = correccion.replace("C3%91", "Ñ"); 
			correccion = correccion.replace("%C3%A1", "á"); 
			correccion = correccion.replace("%C3%81", "Á"); 
			correccion = correccion.replace("%C3%A9", "é"); 
			correccion = correccion.replace("%C3%89", "É");
			correccion = correccion.replace("%C3%AD", "í"); 
			correccion = correccion.replace("%C3%8D", "Í");		
			correccion = correccion.replace("%C3%B3", "ó");
			correccion = correccion.replace("%C3%93", "Ó"); 
			correccion = correccion.replace("%C3%BA", "ú"); 
			correccion = correccion.replace("%C3%9A", "Ú");
			correccion = correccion.replace("%3A", ":");
			correccion = correccion.replace("%2C", ",");
			correccion = correccion.replace("%3B", ";");
			correccion = correccion.replace("%3F", "?");
			correccion = correccion.replace("%C2%BF", "¿");
			correccion = correccion.replace("%C2%A1", "¡");	
			correccion = correccion.replace("%7B", INICIO_CAMPO);
			correccion = correccion.replace("%7D", FIN_CAMPO);
			correccion = correccion.replace("%2C", SEPARADOR_ELEMENTOS_CAMPO);
			correccion = correccion.replace("%3B", SEPARADOR_CAMPOS);
			correccion = correccion.replace("%0A", SALTO_LINEA);
			correccion = correccion.replace("%28", "(");
			correccion = correccion.replace("%29", ")");	
			correccion = correccion.replace("%3D", SEPARADOR_TRANSICIONES);

		}
		return correccion;
	}
	
	/**
	 * Genera una nueva pila con la siguiente transici�n, 
	 * permitiendo mantener el valor de la pila actual en caso de que transici�n no sea v�lida
	 * @param pila
	 * @param transOut
	 * @return
	 */
	public static void adaptaPilaConTransicionSalida(Deque<Character> pila, TransicionOut transOut) {
		
		for (Character c : transOut.getNuevaCabezaPila()){
			if (c != Utils.LAMBDA){
				pila.push(c);
			}
		}
	}
	
	/**
	 * Devuelve una lista recibiendo un array del objeto
	 * @param array
	 * @return
	 */
	public List<E> adaptaALista(E[] array){
		return Arrays.asList(array);
	}
	/**
	 * Transform list to array
	 * @param list
	 * @return
	 */
	public E[] adaptaAArray(List<E> list){
		return (E[]) list.toArray();
	}
}
