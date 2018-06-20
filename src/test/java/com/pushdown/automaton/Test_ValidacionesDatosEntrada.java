package com.pushdown.automaton;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pushdown.automaton.controller.GeneradorAutomataPila;
import com.pushdown.automaton.exceptions.AlfabetoNoValidoException;
import com.pushdown.automaton.exceptions.DatosEntradaErroneosException;
import com.pushdown.automaton.model.AutomataPila;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test_ValidacionesDatosEntrada {
	
	@Autowired
	GeneradorAutomataPila aut;
		
	@Test (expected = DatosEntradaErroneosException.class)	
	public void errorEsperado_ruta_erroneaPorGenerador() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		AutomataPila automata = aut.generaAutomata("mal escrito.txt");
		assertNotNull(automata);
	}

	@Test (expected = IOException.class)	
	public void errorEsperado_ruta_nullPorGenerador() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		AutomataPila automata = aut.generaAutomata(null);
		assertNotNull(automata);
	}
	
	@Test (expected = DatosEntradaErroneosException.class)	
	public void errorEsperado_ruta_erroneaPorConstructor() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		AutomataPila automata = new AutomataPila("mal escrito", aut);
		assertNotNull(automata);
	}

	@Test (expected = IOException.class)	
	public void errorEsperado_ruta_nullPorConstructor() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		AutomataPila automata = aut.generaAutomata(null);
		assertNotNull(automata);
	}

	/**
	 * Primera linea mal formada -- simbolo fichero con multiples caracteres
	 * @throws DatosEntradaErroneosException 
	 */
	@Test (expected = DatosEntradaErroneosException.class)	
	public void testEntradaPrimeraLineaMalFormada() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		String definicion = "{a,b};{SD,A,B};{p,q,r};p;S;\n";
		AutomataPila automata = new AutomataPila(definicion, aut);
		assertNotNull(automata);
	}
	
	/**
	 * Primera linea mal formada
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws AlfabetoNoValidoException 
	 * @throws DatosEntradaErroneosException 
	 */
	@Test (expected = DatosEntradaErroneosException.class)	
	public void testTransicionFila1MalFormada() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		String definicion = "{a,b};{SD,A,B};{p,q,r};p;S;\n"
					+	"f(a,b,c--)";
		AutomataPila automata = new AutomataPila(definicion, aut);
		assertNotNull(automata);
	}
	
	@Test
	public void testConstructorVacioAutomata(){
		AutomataPila aut = new AutomataPila();
		assertTrue(aut.getInicialPila() == ' ');
		assertTrue(aut.getEstadoInicial().isEmpty());
	}

}
