//package Test;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import org.junit.Test;
//
//import AP.AutomataPila;
//import main.GeneradorAutomataPila;
//import main.ProcesadorPalabras;
//
//public class GeneradorAutomataPilaTest {
//	private final GeneradorAutomataPila aut = new GeneradorAutomataPila();
//	String rutaDiciembre2016 = "automatasTest/diciembre2016.txt";
//	String rutaNoDeter1 = "automatasTest/noDeterminista1.txt";
//	String rutaDiciembre2016C = "automatasTest/diciembre2016C.txt";
//	String rutaDiciembre2014 = "automatasTest/diciembre2014.txt";
//
//	
//	/**
//	 * Fichero mal introducido
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 */
//	@Test (expected = FileNotFoundException.class)	
//	public void testGeneraAutomata() throws FileNotFoundException, IOException {
//		AutomataPila automata = aut.generaAutomata("mal escrito.txt");
//		assertNotNull(automata);
//	}
//
//	/**
//	 * El lenguaje aceptado es:
//	 * L = {y(ba)^m | y -> {c,d}*, y m= nº c's en y, m>=0
//	 * Debe aceptar (ej):
//	 * 	-Palabra vacia
//	 * 	-d's 
//	 * 	-dcdcdcdbababa (3 c, por tanto ba^3)
//	 * todas las d's posibles
//	 * @throws Exception
//	 */
//	@Test
//	public void testAPDiciembre2016() throws Exception{
//		
//		AutomataPila automata = aut.generaAutomata(rutaDiciembre2016);
//		assertNotNull(automata);
//		ProcesadorPalabras procesador = new ProcesadorPalabras();
//		
//		assertTrue(procesador.compruebaPalabraBT("", automata));		
//		assertTrue(procesador.compruebaPalabraBT("d", automata));
//		assertTrue(procesador.compruebaPalabraBT("ddddd", automata));
//		assertTrue(procesador.compruebaPalabraBT("ddddcba", automata));
//		assertTrue(procesador.compruebaPalabraBT("cdba", automata));
//		assertTrue(procesador.compruebaPalabraBT("cccbababa", automata));
//		assertFalse(procesador.compruebaPalabraBT("ccba", automata)); //se vuelve loco haciendo backtraking, analizar
//		assertFalse(procesador.compruebaPalabraBT("cbad", automata));
//		
//	}
//	
//	@Test
//	public void testAPNoDeterminista() throws Exception{
//		AutomataPila automata = aut.generaAutomata(rutaNoDeter1);
//		assertNotNull(automata);
//		ProcesadorPalabras procesador = new ProcesadorPalabras();
//		assertTrue(procesador.compruebaPalabraBT("", automata));
//		assertTrue(procesador.compruebaPalabraBT("a",automata));
//		assertTrue(procesador.compruebaPalabraBT("ab", automata));
//		assertFalse(procesador.compruebaPalabraBT("aab", automata));
//		assertTrue(procesador.compruebaPalabraBT("aabb", automata));
//		assertTrue(procesador.compruebaPalabraBT("aaabbb", automata));
//		assertFalse(procesador.compruebaPalabraBT("abb", automata));
//		assertFalse(procesador.compruebaPalabraBT("aa", automata));
//		assertFalse(procesador.compruebaPalabraBT("aaaaaaa", automata));
//	}
//	
//	@Test
//	public void testAPDiciembre2016C() throws Exception{
//		AutomataPila automata = aut.generaAutomata(rutaDiciembre2016C);
//		assertNotNull(automata);
//		ProcesadorPalabras procesador = new ProcesadorPalabras();
//		
//		assertTrue(procesador.compruebaPalabraBT("", automata));		
//		assertTrue(procesador.compruebaPalabraBT("aaaaaa", automata));
//		assertTrue(procesador.compruebaPalabraBT("a", automata));
//		assertTrue(procesador.compruebaPalabraBT("10b", automata));
//		assertTrue(procesador.compruebaPalabraBT("1010bb", automata));
//		assertFalse(procesador.compruebaPalabraBT("1010b", automata));
//		assertFalse(procesador.compruebaPalabraBT("1010bbb", automata));
//		assertFalse(procesador.compruebaPalabraBT("1010baaa", automata));
//		assertTrue(procesador.compruebaPalabraBT("1010bbaaaa", automata));
//		assertTrue(procesador.compruebaPalabraBT("1010baaaba", automata));
//		assertTrue(procesador.compruebaPalabraBT("1010baaaab", automata));
//		assertFalse(procesador.compruebaPalabraBT("1010baaaabb", automata));
//		assertFalse(procesador.compruebaPalabraBT("10a10baaaabb", automata));
//		assertFalse(procesador.compruebaPalabraBT("10b10baaaabb", automata));
//
//	}
//	
//	/**
//	 * Diciembre 2014 automata pila
//	 * El lenguaje aceptado es:
//	 *  L = {b^na^mb^(n-m) | n,m ≥ 0, n ≥ m}
//	 *  -Palabra vacía
//	 *  -(n y m = 1) --> ba
//	 *  -(n=2 m=1) --> bbab
//	 *  -(n=2 m=2) --> bbaa
//	 *  -(n=3 m=2) --> bbbaab
//	 *  -(n=3 m=0) --> bbbbbb
//	 *  
//	 */
//	@Test
//	public void testAPDiciembre2014() throws Exception{
//		String prueba = "e\ne\ne\nee\ne";
//		System.out.println(prueba);
//		AutomataPila automata = aut.generaAutomata(rutaDiciembre2014);
//		assertNotNull(automata);
//		ProcesadorPalabras procesador = new ProcesadorPalabras();
//		
//		assertTrue(procesador.compruebaPalabraBT("", automata));		
//		assertTrue(procesador.compruebaPalabraBT("ba", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbab", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbaa", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbbaab", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbbbbb", automata));
//		assertFalse(procesador.compruebaPalabraBT("a", automata));
//		assertFalse(procesador.compruebaPalabraBT("aa", automata));
//		
////		assertFalse(procesador.compruebaPalabraBT("bbb", automata));
////		assertFalse(procesador.compruebaPalabraBT("bbba", automata));
////		assertFalse(procesador.compruebaPalabraBT("bba", automata));
//		
//	}
//	
//	@Test
//	public void testAutomataString() throws Exception{
//		String automataDiciembre = 
//				"{a,b};{S,A,B};{p,q,r};p;S;\n"+
//				"f(p,b,S)=(p,BS)\n"+
//				"f(p,b,B)=(p,BB)\n"+
//				"f(p, ,S)=(p, )\n"+
//				"f(p,b,B)=(r, )\n"+
//				"f(p,a,B)=(q, )\n"+
//				"f(q,a,B)=(q, )\n"+
//				"f(q, ,S)=(q, )\n"+
//				"f(q,b,B)=(r, )\n"+
//				"f(r,b,B)=(r, )\n"+
//				"f(r, ,S)=(r, )\n";
//		ProcesadorPalabras procesador = new ProcesadorPalabras();
//		AutomataPila automata = aut.generaAutomata(automataDiciembre);
//		assertTrue(procesador.compruebaPalabraBT("", automata));		
//		assertTrue(procesador.compruebaPalabraBT("ba", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbab", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbaa", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbbaab", automata));
//		assertTrue(procesador.compruebaPalabraBT("bbbbbb", automata));
//		assertFalse(procesador.compruebaPalabraBT("a", automata));
//		assertFalse(procesador.compruebaPalabraBT("aa", automata));
//
//	}
//}