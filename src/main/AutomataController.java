package main;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import AP.AutomataPila;

@RestController
public class AutomataController {
	
	public AutomataController() {}

	//TODO solucionar problemas con dependencias/beans apra evitar instanciar objetos
//	@Autowired
	GeneradorAutomataPila generador;
	
//	@Autowired
	ProcesadorPalabras procesador;
	
	@RequestMapping(value= "/")
	@ResponseBody
	String home(){
		return "It works!";
	}
	//TODO crear servicios que recubran llamadas del controller y reorganizar proyecto entero
	@RequestMapping(value = "/Generate", method = RequestMethod.POST)
	public AutomataPila generaAutomata(@RequestBody String entradaConsulta) throws IOException {
		AutomataPila automata = new AutomataPila();
		return automata.generaAutomata(entradaConsulta);
	}
	
	@RequestMapping(value = "/AdvancedGenerate", method = RequestMethod.POST)
	public AutomataPila generaConDefinicion(@RequestBody String principales, List<String> transiciones) throws IOException{
		return new AutomataPila(principales, transiciones);
	}
	
	@RequestMapping(value = "/CheckWord", method = RequestMethod.GET)
	public Boolean validaPalabra(@RequestBody AutomataPila automata, String palabra) throws Exception{
		if (automata != null){
			//Crear metodo en automata para validar palabras utilizando el procesador en lugar de al revés
			return procesador.compruebaPalabraBT(palabra, automata);
		} else {
			throw new Exception("El automata es nulo");
		}
	}
}
