package main;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import AP.AutomataPila;
import AP.TransicionIn;
import AP.TransicionOut;
import excepciones.AlfabetoNoValidoException;

@Service
public class ProcesadorPalabras {
	static Logger log = Logger.getLogger(ProcesadorPalabras.class.getName());
	public ProcesadorPalabras(){}
	
	/**
	 * Genera una nueva pila con la siguiente transici�n, 
	 * permitiendo mantener el valor de la pila actual en caso de que transici�n no sea v�lida
	 * @param pila
	 * @param transOut
	 * @return
	 */
	private Deque<Character> pilaAuxiliar(Deque<Character> pila, TransicionOut transOut) {
		
		Deque<Character> pilaAux = new ArrayDeque<>();
		pilaAux.addAll(pila);

		for (Character c : transOut.getNuevaCabezaPila()){
			if (c != Utils.LAMBDA){
				pilaAux.push(c);
			}
		}
		return pilaAux;
	}
	
	/**
	 * Metodo que permite verificar si una palabra pertenece al lenguaje del aut�mata.
	 * @param palabraEntrada no puede ser null, se valida desde el front.
	 * @param automata
	 * @return
	 * @throws Exception
	 */
	public boolean compruebaPalabraBT(String palabraEntrada, AutomataPila automata) throws AlfabetoNoValidoException{
		boolean pertenece = true; 
		int i = 0;
		char letraRechazada = 0;
		palabraEntrada = palabraEntrada.trim(); //suprimimos los espacios en blanco en caso de que los haya
		
		//1� Comprobamos que los caracteres de entrada pertenecen al alfabeto
		while (i<palabraEntrada.length() && pertenece){
			pertenece = automata.getAlfabetoLenguaje().contains(palabraEntrada.charAt(i));
			if (!pertenece){
				letraRechazada = palabraEntrada.charAt(i);
				pertenece = false;
			}
			i++;
		}
		
		if (i!=palabraEntrada.length() || (i==palabraEntrada.length() && !pertenece)){
			throw new AlfabetoNoValidoException("El caracter: "+letraRechazada+ " no pertenece al alfabeto");
		}
		
		//2� Cargamos la primera transicion para llamar al metodo recursivo de BT
		Deque<Character> pila = new ArrayDeque<>();
		pila.push(automata.getInicialPila());
		String estadoActual = automata.getEstadoInicial();			
		//posicion inicial = 0
		boolean resultado = compruebaBT(estadoActual, 0, palabraEntrada, pila,automata);
		String palabra = palabraEntrada.isEmpty() ? "vacia" : palabraEntrada;
		if (resultado){
			log.info("La palabra: "+palabra+" esta aceptada por el automata: "+automata.getIdAutomata());
		} else {
			log.info("La palabra: "+palabra+" NO esta aceptada por el automata: "+automata.getIdAutomata());
		}
		return resultado;
	}
	
	/**
	 * Metodo auxiliar que procesa las palabras mediante la t�cnica de Backtraking
	 * @param estadoActual
	 * @param posicionCadena
	 * @param palabraEntrada
	 * @param pila
	 * @return
	 */
	private boolean compruebaBT(String estadoActual, int posicionCadena, String palabraEntrada, Deque<Character> pila,AutomataPila automata) {
//		registrarEntradaBackTraking(estadoActual, posicionCadena, palabraEntrada, pila);
		if (pila.isEmpty()) {
			return (palabraEntrada.length() == posicionCadena);
		}
		Character caracterEntrada = null;
		if (posicionCadena > 0 && posicionCadena >= palabraEntrada.length()) {
			TransicionIn tranEntrada = new TransicionIn(estadoActual, Utils.LAMBDA, pila.peek());
			if (automata.getFuncionesTransicion().get(tranEntrada) == null) {
				return false;
			}
			caracterEntrada = Utils.LAMBDA;
		} else {		
			caracterEntrada = palabraEntrada.isEmpty() ? Utils.LAMBDA : palabraEntrada.charAt(posicionCadena);
		}
		
		//Recuperamos las posibles transiciones en el estado actual
		Character cabezaConsumida = pila.pop();
		//Guardamos la pila actual, de modo que cuando se haga backtracking todo esta guardado correctamente
		Deque<Character> pilaAnterior = new ArrayDeque<>();
		pila.push(cabezaConsumida);
		pilaAnterior.addAll(pila);
		pila.pop();
		String estadoAnterior = estadoActual;	
		
		TransicionIn tranEntrada = new TransicionIn(estadoActual, caracterEntrada, cabezaConsumida);
		TransicionIn tranLambda = null;
		List<TransicionOut> transicionesSalida = null;
		
		if (automata.getFuncionesTransicion().get(tranEntrada) !=null) {
			transicionesSalida = new ArrayList<>(automata.getFuncionesTransicion().get(tranEntrada));
		} 
		
		if (Utils.esCollecionVaciaONull(transicionesSalida)) {
			pila.push(tranEntrada.getSimbCabezaPila());		
			tranEntrada = new TransicionIn(estadoActual, Utils.LAMBDA, pila.pop());
			if (automata.getFuncionesTransicion().get(tranEntrada) != null){
				transicionesSalida = new ArrayList<>(automata.getFuncionesTransicion().get(tranEntrada));			
			}
		} else {
			tranLambda = new TransicionIn(estadoActual, Utils.LAMBDA, tranEntrada.getSimbCabezaPila());
			if (automata.getFuncionesTransicion().get(tranLambda) != null) {
				List<TransicionOut> transSalidaLambda = new ArrayList<>(automata.getFuncionesTransicion().get(tranLambda));
				for (TransicionOut transicion :transSalidaLambda) {
					if (!transicionesSalida.contains(transicion))
						transicionesSalida.add(transicion);
				}
			}
		}
		
		//Utilizamos la variable exito para salir del BT
		boolean exito = false;
		if (Utils.esCollecionVaciaONull(transicionesSalida)) {
			return false;
		}
		for(int i=0; i<transicionesSalida.size() && !exito ; i++) {
			TransicionOut transOut = transicionesSalida.get(i);

			if (i!=0 && tranEntrada.getSimbEntrada()!=Utils.LAMBDA 
					&& automata.getFuncionesTransicion().get(tranEntrada)!=null 
					&&automata.getFuncionesTransicion().get(tranEntrada).contains(transOut)) {
				if (!pila.isEmpty()) {
					pila.pop();
				}
			}

			Deque<Character> pilaAux = pilaAuxiliar(pila, transOut);
			if (Utils.esFactible(transOut,pilaAux,palabraEntrada,posicionCadena)) {
				pila = pilaAux;
				estadoActual = transOut.getEstadoSalida();

				if (Utils.esSolucion(tranEntrada,tranLambda, pila, posicionCadena, palabraEntrada, estadoActual, automata)) {
					exito = true;
					break;
				} else {
					if (Utils.esTransicionEntradaSoloLambda(automata, tranEntrada, tranLambda)) {
						exito = compruebaBT(estadoActual, posicionCadena, palabraEntrada, pila, automata);							
					} else {
						exito = compruebaBT(estadoActual, posicionCadena+1, palabraEntrada, pila,automata);
					}
					if (!exito){
						pila = pilaAnterior;
						estadoActual = estadoAnterior;
					}
				}
			} else{
				pila = pilaAnterior;
			}
		}
		return exito;
	}

	private void registrarEntradaBackTraking(String estadoActual, int posicionCadena, String palabraEntrada, Deque<Character> pila) {
		Iterator<Character> pilaTraza = pila.descendingIterator();
		StringBuilder sb = new StringBuilder("[");
		while (pilaTraza.hasNext())
			sb.append(pilaTraza.next()+",");
		sb.append("]");
		log.info("Estado actual:"+estadoActual + 
				" posicionPalabra: "+posicionCadena+
				", palabraEntrada: "+palabraEntrada+
				" PILA: " + sb.toString());
	}
}
