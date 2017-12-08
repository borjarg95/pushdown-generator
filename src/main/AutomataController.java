package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import AP.AutomataPila;
import excepciones.AlfabetoNoValidoException;

@RestController
public class AutomataController {
	
	private Map<Integer, AutomataPila> automatasGenerados = new HashMap<>();
	public AutomataController() {}

	@Autowired
	@Qualifier("procesadorPalabras")
	private ProcesadorPalabras procesadorPalabras;
	
	@RequestMapping(value= "/")
	@ResponseBody
	String home(){
		return "index.html";
	}
	//TODO crear servicios que recubran llamadas del controller y reorganizar proyecto entero
	@RequestMapping(value = "/Generate", method = RequestMethod.POST)
	public ResponseEntity<?> generaAutomata(@RequestBody String entradaConsulta) throws IOException {
		try{
			if (automatasGenerados.size()> Utils.TAMANIO_MAPA_AUTOMATAS_GENERADOS)
				automatasGenerados.clear();
			//Trabajar directamente sobre el propio objeto, no devolver un automata nuevo dentro de automata
			AutomataPila automata = new AutomataPila(Utils.correctorCharEspeciales(entradaConsulta));
			automata.setIdAutomata(automatasGenerados.size()+1);
			automatasGenerados.put(automata.getIdAutomata(), automata);
			return new ResponseEntity<AutomataPila>(automata, HttpStatus.OK);
		}catch (Throwable e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(value = "/AdvancedGenerate", method = RequestMethod.POST)
	public AutomataPila generaConDefinicion(@RequestBody String principales, List<String> transiciones) throws IOException{
		return new AutomataPila(principales, transiciones);
	}
	
	@RequestMapping(value = "/CheckWord/{index}/{palabra}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> validaPalabra(@PathVariable("index") String index, @PathVariable("palabra") String palabra) throws Exception{
		
		if (automatasGenerados.get(Integer.valueOf(index)) != null){
			//Crear metodo en automata para validar palabras utilizando el procesador en lugar de al revés
			try{
				return new ResponseEntity<Boolean>(procesadorPalabras.compruebaPalabraBT(Utils.correctorCharEspeciales(palabra), automatasGenerados.get(Integer.valueOf(index))), HttpStatus.OK);
			} catch (AlfabetoNoValidoException e){
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<String>("El automata no se ha cargado correctamente, por favor vuelve a generarlo.", HttpStatus.INSUFFICIENT_STORAGE); 
		}
	}
}
