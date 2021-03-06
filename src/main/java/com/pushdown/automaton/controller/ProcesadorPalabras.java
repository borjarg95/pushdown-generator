package com.pushdown.automaton.controller;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pushdown.automaton.exceptions.DatosNoValidosException;
import com.pushdown.automaton.exceptions.NodosInfinitosException;
import com.pushdown.automaton.exceptions.ValidacionPalabraException;
import com.pushdown.automaton.model.AutomataPila;
import com.pushdown.automaton.model.TransicionIn;
import com.pushdown.automaton.model.TransicionOut;
import com.pushdown.automaton.utils.Utils;

@Service
public class ProcesadorPalabras {
	private static final String LA_PALABRA = "La palabra: ";
	private static Logger logger = LogManager.getLogger(ProcesadorPalabras.class.getName());
	
	/**
	 * Metodo que permite verificar si una palabra pertenece al lenguaje del aut�mata.
	 * @param palabraEntrada no puede ser null, se valida desde el front.
	 * @param automata
	 * @return
	 * @throws ValidacionPalabraException 
	 * @throws Exception
	 */
	public boolean compruebaPalabraBT(String palabraEntrada, AutomataPila automata) throws ValidacionPalabraException{
		palabraEntrada = palabraEntrada.trim(); //suprimimos los espacios en blanco en caso de que los haya
		validaCaracteresPalabra(palabraEntrada, automata);
		
		//2� Cargamos la primera transicion para llamar al metodo recursivo de BT
		Deque<Character> pila = new ArrayDeque<>();
		pila.push(automata.getInicialPila());
		String estadoActual = automata.getEstadoInicial();			
		//posicion inicial = 0
		boolean resultado;
		try {
			resultado = compruebaBT(estadoActual, 0, palabraEntrada, pila,automata, 0);
		} catch (NodosInfinitosException e) {
			logger.info(LA_PALABRA+ palabraEntrada + " esta generando un bucle infinito: "+automata.toString());
			resultado = false;
		}
		String palabra = palabraEntrada.isEmpty() ? "vacia" : palabraEntrada;
		if (resultado){
			logger.info(LA_PALABRA+palabra+" esta aceptada por el automata: "+automata.getIdAutomata());
		} else {
			logger.info(LA_PALABRA+palabra+" NO esta aceptada por el automata: "+automata.getIdAutomata());
		}
		return resultado;
	}

	/**
	 * Dada una cadena de entrada y un autómata construido, 
	 * se valida que la todos los caracteres de la palabra 
	 * pertenecen al alfabeto del automata.
	 * 
	 * @param palabraEntrada
	 * @param automata
	 * @throws DatosNoValidosException
	 * @throws ValidacionPalabraException 
	 */
	private void validaCaracteresPalabra(String palabraEntrada, AutomataPila automata) throws ValidacionPalabraException {
		boolean pertenece = true; 
		int i = 0;
		char letraRechazada =' ';
		while (i<palabraEntrada.length() && pertenece){
			pertenece = automata.getAlfabetoLenguaje().contains(palabraEntrada.charAt(i));
			if (!pertenece){
				letraRechazada = palabraEntrada.charAt(i);
				pertenece = false;
			}
			i++;
		}
		
		if (i!=palabraEntrada.length() || (i==palabraEntrada.length() && !pertenece)){
			throw new ValidacionPalabraException("El caracter: "+letraRechazada+ " no pertenece al alfabeto");
		}
	}
	
	/**
	 * Metodo auxiliar que procesa las palabras mediante la t�cnica de Backtraking
	 * @param estadoActual
	 * @param posicionCadena
	 * @param palabraEntrada
	 * @param pila
	 * @return
	 * @throws NodosInfinitosException 
	 */
	private boolean compruebaBT(String estadoActual, int posicionCadena, String palabraEntrada, Deque<Character> pila,AutomataPila automata, int numLlamadasRecursivas) throws NodosInfinitosException {
		if (numLlamadasRecursivas > Utils.ALTURA_MAXIMA_ARBOL_BACKTRAKING) {
			throw new NodosInfinitosException();
		}
		if (pila.isEmpty()) {
			return Utils.esSolucion(pila, posicionCadena, palabraEntrada);
		}
		Character caracterEntrada = Utils.recuperaCaracter(posicionCadena, palabraEntrada);
		if (posicionCadena > 0 && posicionCadena >= palabraEntrada.length()) {
			TransicionIn tranEntrada = new TransicionIn(estadoActual, caracterEntrada, pila.peek());
			if (automata.getFuncionesTransicion().get(tranEntrada) == null) {
				return false;
			}
		}
		
		//Guardamos la pila actual, de modo que cuando se haga backtracking no se pierde la informacion anterior
		Deque<Character> pilaAnterior = new ArrayDeque<>(pila);
		String estadoAnterior = estadoActual;	
		
		//Recuperamos las posibles transiciones en el estado actual
		TransicionIn tranEntrada = new TransicionIn(estadoActual, caracterEntrada, pila.pop());
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
		int i = 0;
		while (i < transicionesSalida.size() && !exito) {
			TransicionOut transOut = transicionesSalida.get(i);

			if (i!=0 && tranEntrada.getSimbEntrada()!=Utils.LAMBDA 
					 && automata.getFuncionesTransicion().get(tranEntrada)!=null 
					 && automata.getFuncionesTransicion().get(tranEntrada).contains(transOut)
					 && !pila.isEmpty()) {
				pila.pop();
			}
			Utils.adaptaPilaConTransicionSalida(pila, transOut);
			estadoActual = transOut.getEstadoSalida();

			if (Utils.esSolucion(pila, posicionCadena, palabraEntrada)) {
				exito = true;
			} else {
				if (Utils.esTransicionLambda(automata, tranEntrada, tranLambda, transOut)) {
					exito = compruebaBT(estadoActual, posicionCadena, palabraEntrada, pila, automata, numLlamadasRecursivas++);							
				} else {
					exito = compruebaBT(estadoActual, posicionCadena+1, palabraEntrada, pila,automata, numLlamadasRecursivas++);
				}
				if (!exito){
					pila = pilaAnterior;
					estadoActual = estadoAnterior;
				} 
			}
			i++;
		}
		return exito;
	}
}
