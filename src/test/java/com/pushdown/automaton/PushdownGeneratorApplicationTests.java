package com.pushdown.automaton;

import static org.junit.Assert.assertFalse;
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
import com.pushdown.automaton.controller.ProcesadorPalabras;
import com.pushdown.automaton.exceptions.AlfabetoNoValidoException;
import com.pushdown.automaton.exceptions.DatosEntradaErroneosException;
import com.pushdown.automaton.model.AutomataPila;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushdownGeneratorApplicationTests {
	@Autowired
	GeneradorAutomataPila aut;
	
	@Autowired
	ProcesadorPalabras procesador;
	
	String rutaNoDeter1 = 		 "src/test/resources/automatasTestFicheros/noDeterminista1.txt";
	String rutaDiciembre2016_B = "src/test/resources/automatasTestFicheros/diciembre2016B.txt";
	String rutaDiciembre2016_C = "src/test/resources/automatasTestFicheros/diciembre2016C.txt";
	String rutaDiciembre2016_D = "src/test/resources/automatasTestFicheros/diciembre2016D.txt";
	String rutaDiciembre2014 = 	 "src/test/resources/automatasTestFicheros/diciembre2014.txt";
	
	/**
	 * Fichero mal introducido
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws AlfabetoNoValidoException 
	 * @throws DatosEntradaErroneosException 
	 */
	@Test (expected = DatosEntradaErroneosException.class)	
	public void testGeneraAutomataFicheroErroneo() throws FileNotFoundException, IOException, AlfabetoNoValidoException, DatosEntradaErroneosException {
		AutomataPila automata = aut.generaAutomata("mal escrito.txt");
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
	
	/**
	 * El lenguaje aceptado es:
	 * L = {y(ba)^m | y -> {c,d}*, y m= nº c's en y, m>=0
	 * Debe aceptar (ej):
	 * 	-Palabra vacia
	 * 	-d's 
	 * 	-dcdcdcdbababa (3 c, por tanto ba^3)
	 * todas las d's posibles
	 * @throws Exception
	 */
	@Test
	public void testAPDiciembre2016_D() throws Exception{
		AutomataPila automata = aut.generaAutomataRuta(rutaDiciembre2016_D);
		assertNotNull(automata);
		
		assertTrue(procesador.compruebaPalabraBT("", automata));		
		assertTrue(procesador.compruebaPalabraBT("d", automata));
		assertTrue(procesador.compruebaPalabraBT("ddddd", automata));
		assertTrue(procesador.compruebaPalabraBT("ddddcba", automata));
		assertTrue(procesador.compruebaPalabraBT("cdba", automata));
		assertTrue(procesador.compruebaPalabraBT("cccbababa", automata));
		assertFalse(procesador.compruebaPalabraBT("ccba", automata)); //no debe aceptarse ya que por 2 c's, deberia haber 2 veces "ba"
		assertTrue(procesador.compruebaPalabraBT("ccbaba", automata));
		assertTrue(procesador.compruebaPalabraBT("cccbababa", automata));
		assertFalse(procesador.compruebaPalabraBT("cbad", automata));
		
	}
	
	@Test
	public void testAPNoDeterminista() throws Exception{
		AutomataPila automata = aut.generaAutomataRuta(rutaNoDeter1);
		assertNotNull(automata);
		assertTrue(procesador.compruebaPalabraBT("", automata));
		assertTrue(procesador.compruebaPalabraBT("a",automata));
		assertTrue(procesador.compruebaPalabraBT("ab", automata));
		assertFalse(procesador.compruebaPalabraBT("aab", automata));
		assertTrue(procesador.compruebaPalabraBT("aabb", automata));
		assertTrue(procesador.compruebaPalabraBT("aaabbb", automata));
		assertFalse(procesador.compruebaPalabraBT("abb", automata));
		assertFalse(procesador.compruebaPalabraBT("aa", automata));
		assertFalse(procesador.compruebaPalabraBT("aaaaaaa", automata));
	}
	
	@Test
	public void testAPDiciembre2016C() throws Exception{
		AutomataPila automata = aut.generaAutomataRuta(rutaDiciembre2016_C);
		assertNotNull(automata);
		
		assertTrue(procesador.compruebaPalabraBT("", automata));		
		assertTrue(procesador.compruebaPalabraBT("aaaaaa", automata));
		assertTrue(procesador.compruebaPalabraBT("a", automata));
		assertTrue(procesador.compruebaPalabraBT("10b", automata));
		assertTrue(procesador.compruebaPalabraBT("1010bb", automata));
		assertFalse(procesador.compruebaPalabraBT("1010b", automata));
		assertFalse(procesador.compruebaPalabraBT("1010bbb", automata));
		assertFalse(procesador.compruebaPalabraBT("1010baaa", automata));
		assertTrue(procesador.compruebaPalabraBT("1010bbaaaa", automata));
		assertTrue(procesador.compruebaPalabraBT("1010baaaba", automata));
		assertTrue(procesador.compruebaPalabraBT("1010baaaab", automata));
		assertFalse(procesador.compruebaPalabraBT("1010baaaabb", automata));
		assertFalse(procesador.compruebaPalabraBT("10a10baaaabb", automata));
		assertFalse(procesador.compruebaPalabraBT("10b10baaaabb", automata));

	}
	
	/**
	 * Diciembre 2014 automata pila
	 * El lenguaje aceptado es:
	 *  L = {b^na^mb^(n-m) | n,m ≥ 0, n ≥ m}
	 *  -Palabra vacía
	 *  -(n y m = 1) --> ba
	 *  -(n=2 m=1) --> bbab
	 *  -(n=2 m=2) --> bbaa
	 *  -(n=3 m=2) --> bbbaab
	 *  -(n=3 m=0) --> bbbbbb
	 *  
	 */
	@Test
	public void testAPDiciembre2014() throws Exception{
		AutomataPila automata = aut.generaAutomataRuta(rutaDiciembre2014);
		assertNotNull(automata);
		
		assertTrue(procesador.compruebaPalabraBT("", automata));		
		assertTrue(procesador.compruebaPalabraBT("ba", automata));
		assertTrue(procesador.compruebaPalabraBT("bbab", automata));
		assertFalse(procesador.compruebaPalabraBT("bba", automata));
		assertTrue(procesador.compruebaPalabraBT("bbaa", automata));
		assertFalse(procesador.compruebaPalabraBT("bbaabb", automata));

		assertTrue(procesador.compruebaPalabraBT("bbbaab", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbbb", automata));
		assertFalse(procesador.compruebaPalabraBT("a", automata));
		assertFalse(procesador.compruebaPalabraBT("aa", automata));
		assertFalse(procesador.compruebaPalabraBT("bbabb", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbbbabbbbb", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbbbaabbbb", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbbaaabb", automata));
		assertFalse(procesador.compruebaPalabraBT("bbbbaaabb", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbaaab", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbaaaa", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbaaaa", automata));
		assertFalse(procesador.compruebaPalabraBT("bbbaaaa", automata));
		
	}
	
	@Test
	public void testAutomataString() throws Exception{
		String automataDiciembre = 
				"{a,b};{S,A,B};{p,q,r};p;S;\n"+
				"f(p,b,S)=(p,BS)\n"+
				"f(p,b,B)=(p,BB)\n"+
				"f(p, ,S)=(p, )\n"+
				"f(p,b,B)=(r, )\n"+
				"f(p,a,B)=(q, )\n"+
				"f(q,a,B)=(q, )\n"+
				"f(q, ,S)=(q, )\n"+
				"f(q,b,B)=(r, )\n"+
				"f(r,b,B)=(r, )\n"+
				"f(r, ,S)=(r, )\n";
		AutomataPila automata = aut.generaAutomata(automataDiciembre);
		assertTrue(procesador.compruebaPalabraBT("", automata));		
		assertTrue(procesador.compruebaPalabraBT("ba", automata));
		assertTrue(procesador.compruebaPalabraBT("bbab", automata));
		assertTrue(procesador.compruebaPalabraBT("bbaa", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbaab", automata));
		assertTrue(procesador.compruebaPalabraBT("bbbbbb", automata));
		assertFalse(procesador.compruebaPalabraBT("a", automata));
		assertFalse(procesador.compruebaPalabraBT("aa", automata));
	}
}
