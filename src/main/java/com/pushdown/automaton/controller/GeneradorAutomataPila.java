package com.pushdown.automaton.controller;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import com.pushdown.automaton.exceptions.DatosNoValidosException;
import com.pushdown.automaton.exceptions.codigos.ErrorGeneracion;
import com.pushdown.automaton.exceptions.codigos.ErrorTransiciones;
import com.pushdown.automaton.model.AutomataPila;
import com.pushdown.automaton.model.TransicionIn;
import com.pushdown.automaton.model.TransicionOut;
import com.pushdown.automaton.utils.Utils;

/**
 * @author Borjarg95
 *
 */
@Service
public class GeneradorAutomataPila {
	
	private BufferedReader bufferReader;

	public AutomataPila generaAutomata(String definicion) throws DatosNoValidosException {
		if (GenericValidator.isBlankOrNull(definicion)){
			throw new DatosNoValidosException(ErrorGeneracion.AUTOMATA_NO_INFORMADO);
		}
		String[] entrada =  definicion.split("\n");
		String[] primeraLinea = entrada[0].split(Utils.SEPARADOR_CAMPOS);
		AutomataPila automata = new AutomataPila();
		procesaPrimeraLinea(automata, primeraLinea);
		
		//TRANSICIONES Y ESTADOS FINALES
		String linea;
		int line = 1; //Inicializamos el valor a 1 porque ya hemos procesado la primera linea del fichero	
		while (line < entrada.length){
			linea = entrada[line];
			line = incluirTransicionEnAutomata(automata, linea, line);
		}	
		return automata;
	}
	
	/**
	 * Se lee de un fichero de entrada la definicion de un automata a pila, de tal manera que:
	 * 		En la primera linea se definiran:
	 * 			-Alfabeto lenguaje
	 * 			-Simbolos automata
	 * 			-Conjunto de estados
	 * 			-Estado inicial
	 * 			-Simbolo inicial de pila
	 *	Todos ellos, se encuentran separados mediante ";" de este modo se simplifica su procesado.
	 * @throws DatosNoValidosException 
	 **/
	public AutomataPila generaAutomataRuta(String ruta) throws IOException, DatosNoValidosException {
		if (GenericValidator.isBlankOrNull(ruta)){
			throw new FileNotFoundException("La ruta no está informada");
		}
		
		FileReader archivoEntrada = new FileReader(ruta);
		bufferReader = new BufferedReader(archivoEntrada);
		
		String[] camposPrimeraLinea = bufferReader.readLine().split(Utils.SEPARADOR_CAMPOS);
		AutomataPila automata = new AutomataPila();
		procesaPrimeraLinea(automata, camposPrimeraLinea);

		//TRANSICIONES Y ESTADOS FINALES
		String linea;
		int line = 1; //inicializamos el valor a 1 porque ya hemos procesado la primera linea del fichero	
		while ((linea = bufferReader.readLine())!=null){
			incluirTransicionEnAutomata(automata, linea, line);
		}	
		bufferReader.close();
		return automata;
	}

	/**
	 * @param automata
	 * @param linea
	 * @param line
	 * @return
	 * @throws DatosEntradaErroneosException
	 * @throws DatosNoValidosException
	 */
	private int incluirTransicionEnAutomata(AutomataPila automata, String linea, int line) throws  DatosNoValidosException {
		line++;
		validaFormatoLineaTransicion(linea, line);
		linea = linea.substring(2, linea.length()-1);				 
		String[] transiciones = divideLineaTransicion(linea, line);
		//Procesado de transicion
		String[] vectorTransEntrada = recuperaVectorTransicionEntrada(line, transiciones);
		TransicionIn tranIn = generaTransicionEntrada(vectorTransEntrada, automata, line);
		String[] vectorTransSalida = recuperaVectorTransicionSalida(line, transiciones);
		TransicionOut tranOut = generaTransicionSalida(vectorTransSalida,automata,line);
		
		incluyeTransicionSalidaEnAutomata(automata, line, tranIn, tranOut);
		if (tranOut.getNuevaCabezaPila().size()==1 && (tranOut.getNuevaCabezaPila().get(0) == Utils.LAMBDA)){
			if (automata.getTransicionesVaciado() == null)					
				automata.setTransicionesVaciado(new ArrayList<TransicionIn>());						
			automata.getTransicionesVaciado().add(tranIn);
		}
		return line;
	}

	/**
	 * Dada una transicion de entrada, de salida y un automata, 
	 * se intenta la transicioens en el mapa de transiciones del automata
	 * @param automata
	 * @param line
	 * @param tranIn
	 * @param tranOut
	 * @throws DatosEntradaErroneosException
	 */
	private void incluyeTransicionSalidaEnAutomata(AutomataPila automata, int line, TransicionIn tranIn,
			TransicionOut tranOut) throws DatosNoValidosException {
		List<TransicionOut> listaSalida = automata.getFuncionesTransicion().get(tranIn);
		if (listaSalida == null){
			listaSalida = new ArrayList<>();
			listaSalida.add(tranOut);
		}else {
			if (!listaSalida.contains(tranOut)){
				listaSalida.add(tranOut);
			} else {
				throw new DatosNoValidosException(ErrorTransiciones.TRANSICION_DUPLICADA.getDescripcion()+line,
						ErrorTransiciones.TRANSICION_DUPLICADA);
			}
		}
		automata.getFuncionesTransicion().put(tranIn, listaSalida);
	}

	/**
	 * Construye la entrada de la transicion 
	 * @param vectorTransEntrada
	 * @param automata
	 * @param line
	 * @return
	 * @throws IOException
	 * @throws DatosNoValidosException 
	 */	
	private TransicionIn generaTransicionEntrada(String[] vectorTransEntrada, AutomataPila automata, int line) throws DatosNoValidosException {
		TransicionIn tranEntrada = new TransicionIn();
		String cabeceraExcepcion = ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA.getDescripcion();
		if (!automata.getEstadosPila().contains(vectorTransEntrada[0])){
			throw new DatosNoValidosException(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA.getDescripcion()+line
					+", estado: "+vectorTransEntrada[0]
					+" no pertenece a los estados del automata", ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA);
		} else{
			tranEntrada.setEstado(vectorTransEntrada[0]);
		}
		if (!(vectorTransEntrada[1].isEmpty())) {
			String simEntradaAlfabeto = vectorTransEntrada[1];
			seteaSimboloEntradaTranIn(automata, line, tranEntrada, cabeceraExcepcion, simEntradaAlfabeto);
		} else {
			throw new DatosNoValidosException(cabeceraExcepcion+line	+", es vacia",
					ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA);
		}
		String simbPila = vectorTransEntrada[2].substring(0, vectorTransEntrada[2].length()-1);
		if ((simbPila.length() == 1) && automata.getAlfabetoPila().contains(simbPila.charAt(0))){
			tranEntrada.setSimbCabezaPila(simbPila.charAt(0));
		} else {
			throw new DatosNoValidosException(cabeceraExcepcion+ line+", el simbolo de pila: "+ simbPila +
					" esta mal informado o no pertenece al conjunto", ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA);
		}
		return tranEntrada;
	}

	private void seteaSimboloEntradaTranIn(AutomataPila automata, int line, TransicionIn tranEntrada,
			String cabeceraExcepcion, String simEntradaAlfabeto) throws DatosNoValidosException {
		if (simEntradaAlfabeto.trim().isEmpty()){									
			//Es una transicion lambda
			tranEntrada.setSimbEntrada(Utils.LAMBDA); //utilizamos el s�mbolo @ como lambda.
			
		} else if(simEntradaAlfabeto.length() == 1){
			if (automata.getAlfabetoLenguaje().contains(simEntradaAlfabeto.charAt(0))){
				tranEntrada.setSimbEntrada(simEntradaAlfabeto.charAt(0));
			} else {
				throw new DatosNoValidosException(cabeceraExcepcion + line 
						+"el simbolo de entrada no pertenece al alfabeto del lenguaje", ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA);
			}
		}
	}

	/**
	 * Construye la salida de la transici�n
	 * @param vectorTransSalida
	 * @param automata
	 * @param line
	 * @return
	 * @throws IOException 
	 */
	private TransicionOut generaTransicionSalida(String[] vectorTransSalida, AutomataPila automata, int line) throws DatosNoValidosException {
		String cabeceraExcepcion = ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA.getDescripcion();
		TransicionOut tranSalida = new TransicionOut();
		String estadoDestino = vectorTransSalida[0].substring(1);
		if (!automata.getEstadosPila().contains(estadoDestino)) {
			throw new DatosNoValidosException(cabeceraExcepcion+line+", estado "+estadoDestino+" no pertenece a los estados del automata",
					ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA);		
		} else {
			tranSalida.setEstadoSalida(estadoDestino);
		}
		String nuevaCabezaPila = vectorTransSalida[1];
		List<Character> cabezaPila = new ArrayList<>();
		if (nuevaCabezaPila.trim().isEmpty()) {//Es una transicion lambda, se utiliza el char @ para representarlo							
			cabezaPila.add(Utils.LAMBDA);
			tranSalida.setNuevaCabezaPila(cabezaPila);
		} else {
			//procesamos la cadena de caracteres a la inversa, para facilitar la inserción en la pila
			for (int i = (nuevaCabezaPila.length()-1);i>=0;i--) {
				if (automata.getAlfabetoPila().contains(nuevaCabezaPila.charAt(i))) {
					cabezaPila.add(nuevaCabezaPila.charAt(i));
				} else {
					throw new DatosNoValidosException(cabeceraExcepcion+line+", símbolo "+nuevaCabezaPila.charAt(i)
							+" no pertenece a los símbolos del automata", ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA);		
				}
			}
		}
		tranSalida.setNuevaCabezaPila(cabezaPila);
		return tranSalida;
	}
	
	/** 
	 * En la primera linea se definiran:
	 * 			-Alfabeto lenguaje
	 * 			-Simbolos automata
	 * 			-Conjunto de estados
	 * 			-Estado inicial
	 * 			-Simbolo inicial de pila
	 * @throws DatosNoValidosException 
	 * @throws DatosEntradaErroneosException 
  	*/
	private void procesaPrimeraLinea(AutomataPila automata, String[] primeraLinea) throws DatosNoValidosException {
		
		validaTamanioPrimeraLinea(primeraLinea);
		String alfabetoLenguaje = primeraLinea[0];
		String simbAutomata = primeraLinea[1];	
		String estados = primeraLinea[2];
		String estadoIni = primeraLinea[3];		
		String simbInicial = primeraLinea[4];
		
		validaDatosPrimeraLinea(alfabetoLenguaje, simbAutomata, estados);
		rellenaAlfabetoLenguaje(automata, alfabetoLenguaje);
		rellenaSimbolosAutomata(automata, simbAutomata);
		rellenaEstadosAutomata(automata, estados);
		procesaSimboloInicialPila(automata, simbInicial);	
		
		//5 - Estado inicial	
		if (GenericValidator.isBlankOrNull(estadoIni)){
			throw new DatosNoValidosException(ErrorGeneracion.ESTADO_INICIAL);
		} else if (!estados.contains(estadoIni)){
			throw new DatosNoValidosException(ErrorGeneracion.ESTADO_INICIAL.getDescripcion() +
					" Debe pertenecer al conjunto de estados.", ErrorGeneracion.ESTADO_INICIAL);
		} else {
			automata.setEstadoInicial(estadoIni);			
		}
	}

	private void validaDatosPrimeraLinea(String alfabetoLenguaje, String simbAutomata, String estados)
			throws DatosNoValidosException {
		if (alfabetoLenguaje.length()<=2){
			throw new DatosNoValidosException(ErrorGeneracion.ALFABETO_LENGUAJE);
		}

		if (simbAutomata.length()<=2){
			throw new DatosNoValidosException(ErrorGeneracion.ALFABETO_PILA);
		}
		
		if (estados.length()<=2){
			throw new DatosNoValidosException(ErrorGeneracion.ESTADOS);
		}
	}
	
	/**
	 * El tamaño de la linea principal deben ser 5 secciones
	 * @param primeraLinea
	 * @throws DatosEntradaErroneosException
	 */
	private void validaTamanioPrimeraLinea(String[] primeraLinea) throws DatosNoValidosException {
		if (primeraLinea.length != Utils.ARGUMENTOS_LINEA0) {
			throw new DatosNoValidosException(ErrorGeneracion.PRIMERA_LINEA_MAL_FORMADA);
		}
	}
	
	/**
	 * @param automata
	 * @param alfabetoLenguaje
	 * @throws DatosEntradaErroneosException
	 */
	private void rellenaAlfabetoLenguaje(AutomataPila automata, String alfabetoLenguaje) throws DatosNoValidosException {
		alfabetoLenguaje = alfabetoLenguaje.substring(1, alfabetoLenguaje.length()-1); //Suprimimos las llaves
		for (String a : alfabetoLenguaje.split(Utils.SEPARADOR_ELEMENTOS_CAMPO)) {
			if (a.length()!=1) {
				throw new DatosNoValidosException(ErrorGeneracion.ALFABETO_LENGUAJE.getDescripcion()
						+ ", deben ser caracteres.", ErrorGeneracion.ALFABETO_LENGUAJE);
			}
			automata.getAlfabetoLenguaje().add(a.charAt(0));
		}
	}

	/**
	 * Este metodo pocesa la cadena <b>simbAutomata</b> que debe contener los caracteres
	 * '{' al inicio y '}' al final de los que se extraen 
	 * los simbolos que pueden incluirse en la pila.
	 * 
	 * @param automata
	 * @param simbAutomata
	 * @throws DatosEntradaErroneosException
	 */
	private void rellenaSimbolosAutomata(AutomataPila automata, String simbAutomata)
			throws DatosNoValidosException {
		simbAutomata = simbAutomata.substring(1, simbAutomata.length()-1); //Suprimimos las llaves
		String[] vectorSimbAutomata = simbAutomata.split(Utils.SEPARADOR_ELEMENTOS_CAMPO);		
		
		for(String a : vectorSimbAutomata){
			if (a.length()!=1){
				throw new DatosNoValidosException(ErrorGeneracion.ALFABETO_PILA.getDescripcion()
						+ ", deben ser caracteres.", ErrorGeneracion.ALFABETO_PILA);
			}
			automata.getAlfabetoPila().add(a.charAt(0));
		}
	}
	
	/**
	 * Este metodo pocesa la cadena <b>estados</b> que debe contener los caracteres
	 * '{' al inicio y '}' al final de los que se extraen 
	 * los estados por los que puede transitar el automata.
	 * 
	 * @param automata
	 * @param estados
	 * @throws DatosEntradaErroneosException
	 */
	private void rellenaEstadosAutomata(AutomataPila automata, String estados) throws DatosNoValidosException {
		estados = estados.substring(1, estados.length()-1); //suprimimos los corchetes
		for (String a : estados.split(Utils.SEPARADOR_ELEMENTOS_CAMPO)){
			if (GenericValidator.isBlankOrNull(a)){
				throw new DatosNoValidosException(ErrorGeneracion.ESTADOS);
			}
			automata.getEstadosPila().add(a.trim()); //eliminamos los posibles blancos introducidos
		}
		if (Utils.esCollecionVaciaONull(automata.getEstadosPila())) {
			throw new DatosNoValidosException(ErrorGeneracion.ESTADOS);
		}
	}
	
	private void procesaSimboloInicialPila(AutomataPila automata, String simbInicial) throws DatosNoValidosException {
		if (GenericValidator.isBlankOrNull(simbInicial) || simbInicial.length()!=1 ) {
			throw new DatosNoValidosException(ErrorGeneracion.SIMBOLO_PILA);
		} else if (!automata.getAlfabetoPila().contains(simbInicial.charAt(0))){
			throw new DatosNoValidosException(ErrorGeneracion.SIMBOLO_PILA.getDescripcion() 
					+ "El caracter debe pertenecer al alfabeto de la pila.", ErrorGeneracion.SIMBOLO_PILA);
		} else {
			automata.setInicialPila(simbInicial.charAt(0));
		}
	}

	/**
	 * Dada una linea de fichero o de cadena de entrada, comprueba que los extremos de la transicion esta formado correctamente
	 * @param linea
	 * @param line
	 * @throws DatosEntradaErroneosException
	 */
	private void validaFormatoLineaTransicion(String linea, int line) throws DatosNoValidosException {
		if (!linea.startsWith("f(") || (!linea.endsWith(")"))) {	
			throw new DatosNoValidosException(ErrorTransiciones.TRANSICION_DE_LA_LINEA.getDescripcion()+line
					+DatosNoValidosException.MAL_FORMADA, ErrorTransiciones.TRANSICION_DE_LA_LINEA);
		}
	}
	
	/**
	 * Metodo que dada una linea procesada, la divide separando lat ransicoin de entrada y la transición de salida.
	 * @param linea
	 * @param line
	 * @return
	 * @throws DatosEntradaErroneosException
	 */
	private String[] divideLineaTransicion(String linea, int line) throws DatosNoValidosException {
		String[] transiciones = linea.split(Utils.SEPARADOR_TRANSICIONES); 
		if (transiciones.length != 2 || (!transiciones[0].endsWith(")") || !transiciones[1].startsWith("("))){
			throw new DatosNoValidosException(ErrorTransiciones.TRANSICION_DE_LA_LINEA.getDescripcion()+line
					+DatosNoValidosException.MAL_FORMADA, ErrorTransiciones.TRANSICION_DE_LA_LINEA);
		}
		return transiciones;
	}
	
	/**
	 * @param line
	 * @param transiciones
	 * @return
	 * @throws DatosEntradaErroneosException
	 */
	private String[] recuperaVectorTransicionSalida(int line, String[] transiciones)
			throws DatosNoValidosException {
		String[] vectorTransSalida = transiciones[1].split(Utils.SEPARADOR_ELEMENTOS_CAMPO);
		if (vectorTransSalida.length != 2){
			throw new DatosNoValidosException(ErrorTransiciones.TRANSICION_DE_LA_LINEA.getDescripcion()+line
					+DatosNoValidosException.MAL_FORMADA, ErrorTransiciones.TRANSICION_DE_LA_LINEA);
				}
		return vectorTransSalida;
	}	

	/**
	 * @param line
	 * @param transiciones
	 * @return
	 * @throws DatosEntradaErroneosException
	 */
	private String[] recuperaVectorTransicionEntrada(int line, String[] transiciones)
			throws DatosNoValidosException {
		String[] vectorTransEntrada = transiciones[0].split(Utils.SEPARADOR_ELEMENTOS_CAMPO);
		if (vectorTransEntrada.length != 3){
			throw new DatosNoValidosException(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA.getDescripcion()+line
					+DatosNoValidosException.MAL_FORMADA, ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA);
				}
		return vectorTransEntrada;
	}	
}
