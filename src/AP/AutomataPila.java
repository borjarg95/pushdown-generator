package AP;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import excepciones.AlfabetoNoValidoException;
import main.GeneradorAutomataPila;


public class AutomataPila {

	private GeneradorAutomataPila generadorAutomataPila;
	/**
	 * @param alfabetoLenguaje
	 * @param alfabetoPila
	 * @param estadosPila
	 * @param estadoInicial
	 * @param inicialPila
	 * @param funcionesTransicion
	 * @param finales
	 */
	private Set<Character> alfabetoLenguaje; 
	private Set<Character> alfabetoPila;
	private Set<String> estadosPila;
	private Character inicialPila;
	private String estadoInicial;
	private Map<TransicionIn, List<TransicionOut>> funcionesTransicion; 
	//clave = estado, valor = transición. Ya que puede haber varias funciones en cada estado.
	private List<String> estadosFinales; //Inicialmente es automata 
										//por vaciado, asi que este campo no importa de momento.
	private List<TransicionIn> transicionesVaciado;
	private int idAutomata;
	/**
	 * Genera un nuevo autómata a pila dando valores a la séptupla que lo forma, siendo
	 *  AP=(∑, Г, Q, A0, q0, f, F) 
	 * @param alfabetoLenguaje ∑
	 * @param alfabetoPila Г
	 * @param estadosPila Q
	 * @param estadoInicial q0
	 * @param inicialPila A0
	 * @param funcionesTransicion f
	 * @param finales F
	 */	
	public AutomataPila(HashSet<Character> alfabetoLenguaje, HashSet<Character> alfabetoPila, HashSet<String> estadosPila,
			Character inicialPila, String estadoInicial, Map<TransicionIn, List<TransicionOut>> funcionesTransicion,
			List<String> finales) {
		this.alfabetoLenguaje = alfabetoLenguaje;
		this.alfabetoPila = alfabetoPila;
		this.estadosPila = estadosPila;
		this.inicialPila = inicialPila;
		this.estadoInicial = estadoInicial;
		this.funcionesTransicion = funcionesTransicion;
		this.estadosFinales = finales;
	}
	
	public AutomataPila generaAutomataConRuta(String ruta) throws FileNotFoundException, IOException, AlfabetoNoValidoException{
		generadorAutomataPila = new GeneradorAutomataPila();
		return generadorAutomataPila.generaAutomataRuta(ruta);
	}
	
	/**
	 * Genera el automata con una definición de texto
	 * @param definicion
	 * @return
	 * @throws IOException
	 * @throws AlfabetoNoValidoException 
	 */
	public AutomataPila(String definicion) throws IOException, AlfabetoNoValidoException{
		generadorAutomataPila = new GeneradorAutomataPila();
		AutomataPila automata = generadorAutomataPila.generaAutomata(definicion);
		this.alfabetoLenguaje = automata.getAlfabetoLenguaje();
		this.alfabetoPila =  automata.getAlfabetoPila();
		this.estadosPila = automata.getEstadosPila();
		this.inicialPila = automata.getInicialPila();
		this.estadoInicial = automata.getEstadoInicial();
		this.funcionesTransicion = automata.getFuncionesTransicion();
		this.estadosFinales = automata.getEstadosFinales();
		
	}
	
	public AutomataPila(String primeraLinea, List<String> transiciones) throws IOException{
//		return generadorAutomataPila.generaAutomata(primeraLinea, transiciones);
	}
	
	/**
	 * Constructor generico
	 */
	public AutomataPila() {
		this.alfabetoLenguaje = new HashSet<>();
		this.alfabetoPila = new HashSet<>();
		this.estadosPila = new HashSet<>();
		this.inicialPila = new Character(' ');
		this.estadoInicial = new String();
		this.funcionesTransicion = new HashMap<>();
		this.estadosFinales = new ArrayList<>();
	}

	public Set<Character> getAlfabetoLenguaje() {
		return alfabetoLenguaje;
	}
	public Set<Character> getAlfabetoPila() {
		return alfabetoPila;
	}
	public Set<String> getEstadosPila() {
		return estadosPila;
	}
	public Character getInicialPila() {
		return inicialPila;
	}
	public String getEstadoInicial() {
		return estadoInicial;
	}
	public Map<TransicionIn, List<TransicionOut>> getFuncionesTransicion() {
		return funcionesTransicion;
	}
	public List<String> getFinales() {
		return estadosFinales;
	}
	public List<String> getEstadosFinales() {
		return estadosFinales;
	}
	public void setAlfabetoLenguaje(Set<Character> alfabetoLenguaje) {
		this.alfabetoLenguaje = alfabetoLenguaje;
	}
	public void setAlfabetoPila(Set<Character> alfabetoPila) {
		this.alfabetoPila = alfabetoPila;
	}
	public void setEstadosPila(Set<String> estadosPila) {
		this.estadosPila = estadosPila;
	}
	public void setInicialPila(Character inicialPila) {
		this.inicialPila = inicialPila;
	}
	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	public void setFuncionesTransicion(Map<TransicionIn, List<TransicionOut>> funcionesTransicion) {
		this.funcionesTransicion = funcionesTransicion;
	}
	public void setEstadosFinales(List<String> estadosFinales) {
		this.estadosFinales = estadosFinales;
	}
	/**
	 * Devuelve el conjunto de todas las transiciones vacias. 
	 * Nos permitirá gestionar las transiciones de vaciado del autómatada.
	 * @return the transicionesVaciado
	 */
	public List<TransicionIn> getTransicionesVaciado() {
		return transicionesVaciado;
	}
	/**
	 * @param transicionesVaciado the transicionesVaciado to set
	 */
	public void setTransicionesVaciado(List<TransicionIn> transicionesVaciado) {
		this.transicionesVaciado = transicionesVaciado;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alfabetoLenguaje == null) ? 0 : alfabetoLenguaje.hashCode());
		result = prime * result + ((alfabetoPila == null) ? 0 : alfabetoPila.hashCode());
		result = prime * result + ((estadoInicial == null) ? 0 : estadoInicial.hashCode());
		result = prime * result + ((estadosFinales == null) ? 0 : estadosFinales.hashCode());
		result = prime * result + ((estadosPila == null) ? 0 : estadosPila.hashCode());
		result = prime * result + ((funcionesTransicion == null) ? 0 : funcionesTransicion.hashCode());
		result = prime * result + idAutomata;
		result = prime * result + ((inicialPila == null) ? 0 : inicialPila.hashCode());
		result = prime * result + ((transicionesVaciado == null) ? 0 : transicionesVaciado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutomataPila other = (AutomataPila) obj;
		if (alfabetoLenguaje == null) {
			if (other.alfabetoLenguaje != null)
				return false;
		} else if (!alfabetoLenguaje.equals(other.alfabetoLenguaje))
			return false;
		if (alfabetoPila == null) {
			if (other.alfabetoPila != null)
				return false;
		} else if (!alfabetoPila.equals(other.alfabetoPila))
			return false;
		if (estadoInicial == null) {
			if (other.estadoInicial != null)
				return false;
		} else if (!estadoInicial.equals(other.estadoInicial))
			return false;
		if (estadosFinales == null) {
			if (other.estadosFinales != null)
				return false;
		} else if (!estadosFinales.equals(other.estadosFinales))
			return false;
		if (estadosPila == null) {
			if (other.estadosPila != null)
				return false;
		} else if (!estadosPila.equals(other.estadosPila))
			return false;
		if (funcionesTransicion == null) {
			if (other.funcionesTransicion != null)
				return false;
		} else if (!funcionesTransicion.equals(other.funcionesTransicion))
			return false;
		if (idAutomata != other.idAutomata)
			return false;
		if (inicialPila == null) {
			if (other.inicialPila != null)
				return false;
		} else if (!inicialPila.equals(other.inicialPila))
			return false;
		if (transicionesVaciado == null) {
			if (other.transicionesVaciado != null)
				return false;
		} else if (!transicionesVaciado.equals(other.transicionesVaciado))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AutomataPila [alfabetoLenguaje=" + alfabetoLenguaje + ", alfabetoPila=" + alfabetoPila
				+ ", estadosPila=" + estadosPila + ", inicialPila=" + inicialPila + ", estadoInicial=" + estadoInicial
				+ ", funcionesTransicion=" + funcionesTransicion + ", estadosFinales=" + estadosFinales + "]";
	}

	public int getIdAutomata() {
		return idAutomata;
	}

	public void setIdAutomata(int idAutomata) {
		this.idAutomata = idAutomata;
	}
	
}
