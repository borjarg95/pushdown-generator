package com.pushdown.automaton;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pushdown.automaton.controller.GeneradorAutomataPila;
import com.pushdown.automaton.exceptions.DatosNoValidosException;
import com.pushdown.automaton.exceptions.codigos.ErrorGeneracion;
import com.pushdown.automaton.exceptions.codigos.ErrorTransiciones;
import com.pushdown.automaton.model.AutomataPila;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_ValidacionesDatosEntrada {
	
	@Autowired
	GeneradorAutomataPila aut;
	
	@Test (expected = FileNotFoundException.class)
	public void CP1_1_errorEsperado_ruta_null() throws IOException, DatosNoValidosException {
		aut.generaAutomataRuta(null);
	}
	
	@Test (expected = FileNotFoundException.class)
	public void CP1_2_errorEsperado_ruta_erronea() throws IOException, DatosNoValidosException {
		aut.generaAutomataRuta("asds");
	}
	
	@Test	
	public void CP1_3_errorEsperado_definicion_erroneaPorGenerador() {
		try {
			aut.generaAutomata("mal escrito");
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.PRIMERA_LINEA_MAL_FORMADA, e.getCodigoError());
		}
	}
	
	@Test
	public void CP1_4_errorEsperado_def_erroneaPorConstructor() {
		try {
			new AutomataPila("mal escrito", aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.PRIMERA_LINEA_MAL_FORMADA, e.getCodigoError());
		}
	}

	@Test
	public void CP1_5_errorEsperado_definicion_nullPorGenerador() {
		try {
			aut.generaAutomata(null);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.AUTOMATA_NO_INFORMADO, e.getCodigoError());
		}
	}

	@Test
	public void CP1_6_errorEsperado_ruta_nullPorConstructor() {
		try {
			new AutomataPila(null, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.AUTOMATA_NO_INFORMADO, e.getCodigoError());
		}
	}

	/**
	 * Primera linea mal formada -- simbolo alfabeto con multiples caracteres
	 * @throws DatosEntradaErroneosException 
	 */
	@Test 
	public void CP2_1_testPrimeraLineaMalFormada() {
		String definicion = "{a,bbb}{S,A,B};{p,q,r};p;S;\n";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.PRIMERA_LINEA_MAL_FORMADA, e.getCodigoError());
		}
	}
	
	/**
	 * Primera linea mal formada -- simbolo automata con multiples caracteres
	 * @throws DatosEntradaErroneosException 
	 */
	@Test 
	public void CP2_2_testPrimeraLineaMalFormada_alfLen() {
		String definicion = "{a,bbb};{S,A,B};{p,q,r};p;S;\n";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.ALFABETO_LENGUAJE, e.getCodigoError());
		}
	}
	
	/**
	 * Primera linea mal formada -- estados mal informados
	 * @throws DatosEntradaErroneosException 
	 */
	@Test 
	public void CP2_3_testPrimeraLineaMalFormada_estados() {
		String definicion = "{a,b};{D,A,B};{,,};p;D;\n";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.ESTADOS, e.getCodigoError());
		}
	}
	
	/**
	 * Primera linea mal formada -- estado inicial no pertenece al conjunto de estados
	 * @throws DatosEntradaErroneosException 
	 */
	@Test 
	public void CP2_4_testPrimeraLineaMalFormada_estadoInicialErroneo() {
		String definicion = "{a,b};{D,A,B};{p,q,r};t;D;\n";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.ESTADO_INICIAL, e.getCodigoError());
		}
	}
	
	/**
	 * Primera linea mal formada -- simbolo inicial no pertenece al conjunto de simbolos del automata
	 * @throws DatosEntradaErroneosException 
	 */
	@Test 
	public void CP2_5_testPrimeraLineaMalFormada_simbInicialErroneo() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;S;\n";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorGeneracion.SIMBOLO_PILA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP3_1_testTransicionMalFormada_sinSalida() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(a,b,c--)";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorTransiciones.TRANSICION_DE_LA_LINEA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP3_2_testTransicionMalFormada_tranInMal() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(q0,aS)=(q0,XS)";

		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}

	@Test 
	public void CP3_3testTransicionMalFormada_tranInMal() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(q0aS)=(q0,XS)";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP3_4_testTransicionMalFormada_tranInMalEstadoNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(q0,a,S)=(q0,XS)";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("q0 no pertenece a los estados del automata"));
			assertEquals(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}	

	@Test 
	public void CP3_5_testTransicionMalFormada_tranInMalCharInNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,c,S)=(q0,XS)";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("simbolo de entrada no pertenece al alfabeto del lenguaje"));
			assertEquals(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}	

	@Test 
	public void CP3_6_testTransicionMalFormada_tranInMalSimbPilaNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,S)=(q0,XS)";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("el simbolo de pila: S esta mal informado o no pertenece al conjunto"));
			assertEquals(ErrorTransiciones.TRANSICION_IN_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}

	@Test 
	public void CP3_7_testTransicionMalFormada_tranOutMal() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=()";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorTransiciones.TRANSICION_DE_LA_LINEA, e.getCodigoError());
		}
	}

	@Test 
	public void CP3_8_testTransicionMalFormada_tranOutMal() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=(,B)";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertEquals(ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP3_9_testTransicionMalFormada_tranOutMalEstadoNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=(soyUnEstadoFalso, )";
		try {
			new AutomataPila(definicion, aut);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("estado soyUnEstadoFalso no pertenece a los estados del automata"));
			assertEquals(ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}

	@Test 
	public void CP3_10_testTransicionMalFormada_tranOutMalSimPilaNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=(p,_)";
		try {
			AutomataPila automata = new AutomataPila(definicion, aut);
			assertNull(automata);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("símbolo _ no pertenece a los símbolos del automata"));
			assertEquals(ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP3_11_testTransicionMalFormada_tranOutMalSimPilaNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=(p,DA_)";
		try {
			AutomataPila automata = new AutomataPila(definicion, aut);
			assertNull(automata);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("símbolo _ no pertenece a los símbolos del automata"));
			assertEquals(ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP3_12_testTransicionMalFormada_tranOutMalSimPilaNoExiste() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=(p,D_A)";
		try {
			AutomataPila automata = new AutomataPila(definicion, aut);
			assertNull(automata);
		} catch (DatosNoValidosException e){
			assertTrue(e.getLocalizedMessage().endsWith("símbolo _ no pertenece a los símbolos del automata"));
			assertEquals(ErrorTransiciones.TRANSICION_OUT_MAL_DEFINIDA_EN_LINEA, e.getCodigoError());
		}
	}
	
	@Test 
	public void CP4_generacionCorrecta() {
		String definicion = "{a,b};{D,A,B};{p,q,r};p;D;\n"
						+	"f(p,b,D)=(p,DA)";
		try {
			AutomataPila automata = new AutomataPila(definicion, aut);
			assertNotNull(automata);
		} catch (Exception e){
			Assert.fail();
		}
	}
}
