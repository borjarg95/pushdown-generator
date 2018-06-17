package com.pushdown.automaton.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pushdown.automaton.exceptions.AlfabetoNoValidoException;
import com.pushdown.automaton.model.AutomataPila;
import com.pushdown.automaton.utils.Utils;

@RestController
public class AutomataController {
	private static final Logger logger = LogManager.getLogger(AutomataController.class.getName());
	private Map<Integer, AutomataPila> automatasGenerados = new HashMap<>();

	public AutomataController() { 
		//constructor vacio
	}

	@Autowired
	@Qualifier("procesadorPalabras")
	private ProcesadorPalabras procesadorPalabras;

	@Autowired
	@Qualifier("generadorAutomataPila")
	private GeneradorAutomataPila generadorAutomataPila;
 
	@RequestMapping(value = "/Generate", method = RequestMethod.POST)
	public ResponseEntity<?> generaAutomata(@RequestBody String entrada) {
		
		String entradaConsulta = Utils.correctorCharEspeciales(entrada);
		try {
			if (automatasGenerados.size() > Utils.TAMANIO_MAPA_AUTOMATAS_GENERADOS)
				automatasGenerados.clear();
			AutomataPila automata = new AutomataPila(entradaConsulta, generadorAutomataPila);
			automata.setIdAutomata(automatasGenerados.size() + 1);
			automatasGenerados.put(automata.getIdAutomata(), automata);
			logger.debug("se genera correctamente el automata: "+automata.getIdAutomata() +" "+entradaConsulta);
			return new ResponseEntity<>(automata, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en la generación del automata " + entradaConsulta + " " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@RequestMapping(value = "/CheckWord/{index}/{palabra}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> validaPalabra(@PathVariable("index") String index, @PathVariable("palabra") String palabra) {
		
		if (automatasGenerados.get(Integer.valueOf(index)) != null) {
			try {
				return new ResponseEntity<>(
						procesadorPalabras.compruebaPalabraBT(Utils.correctorCharEspeciales(palabra),
						automatasGenerados.get(Integer.valueOf(index))),
						HttpStatus.OK);
			} catch (AlfabetoNoValidoException e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			return new ResponseEntity<>("El automata no se ha cargado correctamente, por favor vuelve a generarlo.",
					HttpStatus.INSUFFICIENT_STORAGE);
		}
	}
}
