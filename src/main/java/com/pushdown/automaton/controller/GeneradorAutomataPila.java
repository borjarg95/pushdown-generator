package com.pushdown.automaton.controller;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import com.pushdown.automaton.exceptions.AlfabetoNoValidoException;
import com.pushdown.automaton.exceptions.DatosEntradaErroneosException;
import com.pushdown.automaton.model.AutomataPila;
import com.pushdown.automaton.model.TransicionIn;
import com.pushdown.automaton.model.TransicionOut;
import com.pushdown.automaton.utils.CodigosError;
import com.pushdown.automaton.utils.Utils;

/**
 * @author Borjarg95
 *
 */
@Service
public class GeneradorAutomataPila {
	
	private BufferedReader bufferReader;

	/**
	 * Se lee de un fichero de entrada la definicion de un automata a pila, de tal manera que:
	 * 		En la primera linea se definiran:
	 * 			-Alfabeto lenguaje
	 * 			-Simbolos automata
	 * 			-Conjunto de estados
	 * 			-Estado inicial
	 * 			-Simbolo inicial de pila
	 *	Todos ellos, se encuentran separados mediante ";" de este modo se simplifica su procesado.
	 * @throws AlfabetoNoValidoException 
	 **/
	public AutomataPila generaAutomataRuta(String ruta) throws IOException, AlfabetoNoValidoException, DatosEntradaErroneosException{
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
	 * @throws AlfabetoNoValidoException
	 */
	private int incluirTransicionEnAutomata(AutomataPila automata, String linea, int line)
			throws DatosEntradaErroneosException, AlfabetoNoValidoException {
		line++;
		validaFormatoLineaTransicion(linea, line);

		linea = linea.substring(2, linea.length()-1);				 
		String[] transiciones = divideLineaTransicion(linea, line);
		
		//Procesado de transicion
		String[] vectorTransEntrada = recuperaVectorTransicionEntrada(line, transiciones);
		TransicionIn tranIn = generaTransicionEntrada(vectorTransEntrada, automata, line);//contiene ")" al final
		
		String[] vectorTransSalida = recuperaVectorTransicionSalida(line, transiciones);
		TransicionOut tranOut = generaTransicionSalida(vectorTransSalida,automata,line);  //contiene "(" al iniciozz
		
		List<TransicionOut> listaSalida = automata.getFuncionesTransicion().get(tranIn);
		if (listaSalida == null){
			listaSalida = new ArrayList<>();
			listaSalida.add(tranOut);
		}else {
			if (!listaSalida.contains(tranOut)){
				listaSalida.add(tranOut);
			} else {
				throw new DatosEntradaErroneosException("No puedes introducir transiciones repetidas. Linea: "+line);
			}
		}
		automata.getFuncionesTransicion().put(tranIn, listaSalida);
		
		if (tranOut.getNuevaCabezaPila().size()==1 && (tranOut.getNuevaCabezaPila().get(0) == Utils.LAMBDA)){
			if (automata.getTransicionesVaciado() == null)					
				automata.setTransicionesVaciado(new ArrayList<TransicionIn>());						
			automata.getTransicionesVaciado().add(tranIn);
		}
		return line;
	}

	public AutomataPila generaAutomata(String definicion) throws IOException, AlfabetoNoValidoException, DatosEntradaErroneosException{
		if (GenericValidator.isBlankOrNull(definicion)){
			throw new IOException("Error en la creacion del automata");
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
	 * Construye la entrada de la transicion 
	 * @param vectorTransEntrada
	 * @param automata
	 * @param line
	 * @return
	 * @throws IOException
	 * @throws AlfabetoNoValidoException 
	 */	
	private TransicionIn generaTransicionEntrada(String[] vectorTransEntrada, AutomataPila automata, int line) throws DatosEntradaErroneosException, AlfabetoNoValidoException {
		TransicionIn tranEntrada = new TransicionIn();
		if (!automata.getEstadosPila().contains(vectorTransEntrada[0])){
			throw new DatosEntradaErroneosException("El estado "+vectorTransEntrada[0]+" de la linea "+line+" no pertenece a los estados del automata");
		} else{
			tranEntrada.setEstado(vectorTransEntrada[0]);
		}
		if (!(vectorTransEntrada[1].isEmpty())) {
			String simEntradaAlfabeto = vectorTransEntrada[1];
			if (simEntradaAlfabeto.trim().isEmpty()){									
				//Es una transicion lambda
				tranEntrada.setSimbEntrada(Utils.LAMBDA); //utilizamos el s�mbolo @ como lambda.
				
			} else if(simEntradaAlfabeto.length() == 1){
				if (automata.getAlfabetoLenguaje().contains(simEntradaAlfabeto.charAt(0))){
					tranEntrada.setSimbEntrada(simEntradaAlfabeto.charAt(0));
				} else {
					throw new AlfabetoNoValidoException("El simbolo de entrada no pertenece al alfabeto del lenguaje");
				}
			}
		} else {
			throw new DatosEntradaErroneosException("La transicion de entrada de la linea "+line+", es vacia");
		}
		String simbPila = vectorTransEntrada[2].substring(0, vectorTransEntrada[2].length()-1);
		if (!(simbPila.isEmpty()) && (simbPila.length() == 1) && 
				(automata.getAlfabetoPila().contains(simbPila.charAt(0)))){
			tranEntrada.setSimbCabezaPila(simbPila.charAt(0));
		} else {
			throw new DatosEntradaErroneosException("El simbolo  de pila esta mal informado o no pertenece al conjunto");
		}
		return tranEntrada;
	}

	/**
	 * Construye la salida de la transici�n
	 * @param vectorTransSalida
	 * @param automata
	 * @param line
	 * @return
	 * @throws IOException 
	 */
	private TransicionOut generaTransicionSalida(String[] vectorTransSalida, AutomataPila automata, int line) throws DatosEntradaErroneosException {
		TransicionOut tranSalida = new TransicionOut();
		String estadoDestino = vectorTransSalida[0].substring(1);
		if (!automata.getEstadosPila().contains(estadoDestino)){
			throw new DatosEntradaErroneosException("El estado "+estadoDestino+" de la linea "+line+" no pertenece a los estados del aut�mata");		
		} else {
			tranSalida.setEstadoSalida(estadoDestino);
		}
		String nuevaCabezaPila = vectorTransSalida[1];
		List<Character> cabezaPila = new ArrayList<>();
		if (nuevaCabezaPila.trim().isEmpty()){									
			//Es una transici�n lambda
			cabezaPila.add(Utils.LAMBDA);
			tranSalida.setNuevaCabezaPila(cabezaPila); //utilizamos el s�mbolo @ como lambda.
		} else {
			//procesamos la cadena de caracteres a la inversa, para facilitar la inserci�n en la pila
			for (int i = (nuevaCabezaPila.length()-1);i>=0;i--)
				cabezaPila.add(nuevaCabezaPila.charAt(i));
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
	 * @throws AlfabetoNoValidoException 
	 * @throws DatosEntradaErroneosException 
  	*/
	private void procesaPrimeraLinea(AutomataPila automata, String[] primeraLinea) throws AlfabetoNoValidoException, 
			DatosEntradaErroneosException{
		
		validaTamanioPrimeraLinea(primeraLinea);
		String alfabetoLenguaje = primeraLinea[0];
		String simbAutomata = primeraLinea[1];	
		String estados = primeraLinea[2];
		String estadoIni = primeraLinea[3];		
		String simbInicial = primeraLinea[4];
		
		validaDatosPrimeraLinea(alfabetoLenguaje, simbAutomata, estados);
		
		//1	- Rellenamos el alfabeto del lenguaje
		alfabetoLenguaje = alfabetoLenguaje.substring(1, alfabetoLenguaje.length()-1); //Suprimimos las llaves

		for (String a : alfabetoLenguaje.split(Utils.SEPARADOR_ELEMENTOS_CAMPO)) {
			if (a.length()!=1) {
				throw new DatosEntradaErroneosException("Los elementos del alfabeto son caracteres, "
						+ "no puede ser un string compuesto");
			}
			automata.getAlfabetoLenguaje().add(a.charAt(0));
		}
		
		//2 - Rellenamos los simbolos del automata
		simbAutomata = simbAutomata.substring(1, simbAutomata.length()-1); //Suprimimos las llaves
		String[] vectorSimbAutomata = simbAutomata.split(Utils.SEPARADOR_ELEMENTOS_CAMPO);		
		
		for(String a : vectorSimbAutomata){
			if (a.length()!=1){
				throw new DatosEntradaErroneosException("Los simbolos del automata son caracteres, "
						+ "no puede ser un string compuesto");
			}
			automata.getAlfabetoPila().add(a.charAt(0));
		}
		
		//3 - Rellenamos los estados del automata
		estados = estados.substring(1, estados.length()-1); //suprimimos los corchetes
		for (String a : estados.split(Utils.SEPARADOR_ELEMENTOS_CAMPO)){
			if (GenericValidator.isBlankOrNull(a)){
				throw new DatosEntradaErroneosException("Alguno de los estados introducios esta vacio. "
						+ "Deben informarse correctamente");
			}
			automata.getEstadosPila().add(a.trim()); //eliminamos los posibles blancos introducidos
		}
		//4 - Simbolo inicial
		if (GenericValidator.isBlankOrNull(simbInicial) || simbInicial.length()!=1){
			throw new DatosEntradaErroneosException("Los simbolos de pila no pueden ser cadenas de caracteres, "
					+ "debe ser un unico caracter");
		} else {
			automata.setInicialPila(simbInicial.charAt(0));
		}	
		//5 - Estado inicial	
		if (GenericValidator.isBlankOrNull(estadoIni)){
			throw new DatosEntradaErroneosException("Estado inicial no informado");
		} else if (!estados.contains(estadoIni)){
			throw new DatosEntradaErroneosException("El estado inicial debe pertenecer al conjunto de estados");
		} else {
			automata.setEstadoInicial(estadoIni);			
		}
	}

	/**
	 * El tamaño de la linea principal deben ser 5 secciones
	 * @param primeraLinea
	 * @throws DatosEntradaErroneosException
	 */
	private void validaTamanioPrimeraLinea(String[] primeraLinea) throws DatosEntradaErroneosException {
		if (primeraLinea.length != Utils.ARGUMENTOS_LINEA0) {
			throw new DatosEntradaErroneosException("La primera linea no está bien formada. Consulta la guia de mensajes de entrada");
		}
	}
	
	/**
	 * Dada una linea de fichero o de cadena de entrada, comprueba que los extremos de la transicion esta formado correctamente
	 * @param linea
	 * @param line
	 * @throws DatosEntradaErroneosException
	 */
	private void validaFormatoLineaTransicion(String linea, int line) throws DatosEntradaErroneosException {
		if (!linea.startsWith("f(") || (!linea.endsWith(")"))) {	
			throw new DatosEntradaErroneosException(CodigosError.TRANSICION_DE_LA_LINEA.getValue()+line+CodigosError.MAL_FORMADA.getValue());
		}
	}
	
	/**
	 * Metodo que dada una linea procesada, la divide separando lat ransicoin de entrada y la transición de salida.
	 * @param linea
	 * @param line
	 * @return
	 * @throws DatosEntradaErroneosException
	 */
	private String[] divideLineaTransicion(String linea, int line) throws DatosEntradaErroneosException {
		String[] transiciones = linea.split(Utils.SEPARADOR_TRANSICIONES); //genera un vector de dos posiciones 0 --> transicionEntrada | 1 --> transicionSalida
		if (transiciones.length != 2 || (!transiciones[0].endsWith(")") || !transiciones[1].startsWith("("))){
			throw new DatosEntradaErroneosException(CodigosError.TRANSICION_DE_LA_LINEA.getValue()+line+CodigosError.MAL_FORMADA.getValue());
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
			throws DatosEntradaErroneosException {
		String[] vectorTransSalida = transiciones[1].split(Utils.SEPARADOR_ELEMENTOS_CAMPO);
		if (vectorTransSalida.length != 2){
			throw new DatosEntradaErroneosException(CodigosError.TRANSICION_DE_LA_LINEA.getValue()+line+CodigosError.MAL_FORMADA.getValue());
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
			throws DatosEntradaErroneosException {
		String[] vectorTransEntrada = transiciones[0].split(Utils.SEPARADOR_ELEMENTOS_CAMPO);
		if (vectorTransEntrada.length != 3){
			throw new DatosEntradaErroneosException(CodigosError.TRANSICION_DE_LA_LINEA.getValue()+line+CodigosError.MAL_FORMADA.getValue());
		}
		return vectorTransEntrada;
	}	
	
	private void validaDatosPrimeraLinea(String alfabetoLenguaje, String simbAutomata, String estados)
			throws AlfabetoNoValidoException, DatosEntradaErroneosException {
		if (alfabetoLenguaje.length()<=2){
			throw new AlfabetoNoValidoException(CodigosError.ALFABETO_LENGUAJE.getValue());
		}

		if (simbAutomata.length()<=2){
			throw new DatosEntradaErroneosException("El campo simbolosAutomata no está informado");
		}
		
		if (estados.length()<=2){
			throw new DatosEntradaErroneosException("El conjunto estados no está informado");
		}
	}
}
