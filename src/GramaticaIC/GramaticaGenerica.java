package GramaticaIC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import AP.AutomataPila;
import AP.TransicionIn;
import AP.TransicionOut;
import main.ProcesadorPalabras;
import main.Utils;

/**
 * @author Borjarg95
 *
 * Este objeto se formará es la primera transformación del autómata en gramática,
 *  definiendo:
 * 		G = (N, ∑, S, P), siendo:
 *		   -N = Conjunto de simbolos no terminales
 *		   -∑ = Símbolos terminales
 *		   -S = axioma 
 *		   -P = Conjunto de plantillas de producciones de la gramática
 *  
 *  Cabe destacar que se transformaran las PlantillaProduccion 
 *   por letras o símbolos de un conjunto predefinido. 
 */
public class GramaticaGenerica {

	@Autowired
	ProcesadorPalabras procesador;
	
	/**
	 * Ejemplo de produccion:
	 * 		Z --> lambda | {a,[pAp],[pAq]}		El conjunto esta formado por lambda y "{a,[pAp],[pAq]}"
	 * 		siendo {a,[pAp],[pAq]} una lista con las producciones de esa regla.?
	 */
	private Map<Character,Set<LinkedList<PlantillaProduccion>>> producciones;
	/**
	 * No terminales: están formados por el axioma+ distintas PlantillasProduccion
	 */
	private Set<PlantillaProduccion> noTerminales;
	private Set<Character> terminales;
	private Character axioma;	
	
	
	/**
	 * Constructor vacío
	 */
	public GramaticaGenerica(){};
	
	/**
	 * Con un automata de entrada, generamos la GramaticaGenerica
	 * -Precondiciones:
	 * 		+Verificar que el automata esta formado correctamente
	 * 		+Automata por vaciado
	 * @param automata
	 */
	public GramaticaGenerica(AutomataPila automata){
		this.terminales = automata.getAlfabetoLenguaje();
		this.axioma = automata.getInicialPila();
//		this.producciones = generarProducciones(automata);
//		this.noTerminales = filtrarProduccionesRepetidas();
	}
	
	/**
	 * Procesa cada una de las transiciones del autómata en producciones.
	 * La longitud de la producción será equivalente a tantas plantillas como caracteres compongan cada TransicionOut.
	 * Ejemplo:
	 * 	-f(p,b,B)=(p,BB) --> BB --> [p,B,p][p,B,q]
	 * 
	 * @param automata
	 * @return
	 * @throws Exception 
	 */
	private HashMap<Character,Set<LinkedList<PlantillaProduccion>>> generarProducciones(AutomataPila automata) throws Exception{
		
		//1- CREACIÓN DE VARIABLES
		String lambda = Character.toString(Utils.LAMBDA);
		HashMap<Character, Set<LinkedList<PlantillaProduccion>>> mapaProducciones = new HashMap<>();
		PlantillaProduccion produccionAux = null;		
		
		//2- SI EL AUTOMATA ACEPTA LA PALABRA VACÍA, INCLUIMOS LA PRODUCCIÓN LAMBDA EN EL AXIOMA
		if (procesador.compruebaPalabraBT(lambda, automata)){
			produccionAux = new PlantillaProduccion(lambda, Utils.LAMBDA, lambda);
			LinkedList<PlantillaProduccion> produccionLambda = new LinkedList<>();
			produccionLambda.add(produccionAux);
			HashSet<LinkedList<PlantillaProduccion>> transicionEnAxioma = new HashSet<>();
			transicionEnAxioma.add(produccionLambda);
			
			mapaProducciones.put(axioma, transicionEnAxioma);
		}
		//3- Continuar con el resto de transiciones
		//TODO para cada una de las transiciones, explorar toda la lista interna y transformarla en plantilla 
		Set<TransicionIn> transEntrada = automata.getFuncionesTransicion().keySet();
		List<TransicionOut> transicionesSalida = null;
		for (TransicionIn tranIn: transEntrada){
			transicionesSalida = automata.getFuncionesTransicion().get(tranIn);
			//generamos una plantilla de producción por cada transOut.
			for (TransicionOut tranOut : transicionesSalida){
//				tranOut.get
			}
		}
		return mapaProducciones;
		
	}
}
