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
	public static final int TAMANIO_MAPA_AUTOMATAS_GENERADOS = 350;

	public static boolean esSolucion(TransicionIn keyTransicion,TransicionIn tranLambda, Stack<Character> pila, int posPalabra, String palabraEntrada, String estadoActual, AutomataPila automata){

		return (pila.isEmpty() && posPalabra == palabraEntrada.length());

	}	
	
	public static boolean esFactible(TransicionOut transicionesSalida, Stack<Character> pila, String palabraEntrada, int posicionCadena){
		return (transicionesSalida!=null); //pila.isEmpty() && posicionCadena < palabraEntrada.length() ||
				
	}
	/**
	 * Devuelve una lista recibiendo un array del objeto
	 * @param array
	 * @return
	 */
	public List<E> adaptaALista(E array[]){
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
}
