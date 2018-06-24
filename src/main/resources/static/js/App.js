/**
 * GLOBAL VARs
 */
const EXAMPLE_NOT_DETERMINISTIC = "{a,b};{S,A,B};{p,q,r};p;S;\nf(p,b,S)=(p,BS)\nf(p,b,B)=(p,BB)\nf(p, ,S)=(p, )\nf(p,b,B)=(r, )\nf(p,a,B)=(q, )\nf(q,a,B)=(q, )\nf(q, ,S)=(q, )\nf(q,b,B)=(r, )\nf(r,b,B)=(r, )\nf(r, ,S)=(r, )";
const EXAMPLE_SIMPLE_DETERMINISTIC = "{a,b};{S,N};{q0,q1};q0;S;\nf(q0,a,S)=(q0,N)\nf(q0,a,N)=(q0,NN)\nf(q0,b,N)=(q1, )\nf(q1,b,N)=(q1, )";
/**
 * Metodo que procesa la entrada y genera un nuevo automata
 * 
 */
function generarDefinicionLarga() {
	var texto= $("#texto").val();
	if (texto.length==0){
		ModalNotif("¡Debes escribir una descripción!")
		return 0;
	}
	var busqueda = "/generate"
		$.ajax({
		    url : busqueda,
		    type: "POST",
		    data : texto,
		    success: function(data){
		    	procesarAutomataResultante(data);
		    },
		    error: function (mensaje, textStatus)
		    {
				ModalNotif(mensaje.responseText);
		    }
		});
}	

/**
 * 
 * 
 */
function procesarAutomataResultante(automata){
	vaciarAutomata();
	$("#identificador").val(automata.idAutomata.toString());
	//Generamos las estructuras de los datos a mostrar
	$("#alfabetoLenguaje").append(automata.alfabetoLenguaje.join(', '));
	$("#alfabetoPila").append(automata.alfabetoPila.join(', '));
	$("#estadosPila").append(automata.estadosPila.join(', '));
	$("#estadoInicial").append(automata.estadoInicial);
	$("#simbInicialPila").append(automata.inicialPila);
	$("#transiciones").append();
	var transiciones= automata.funcionesTransicion;
	var transicionesSalida = "";
	//generar una linea por cada transicion entrada salida
	for (var key in transiciones) {
		var transicionesValue = "";		
		for (var i= 0; i < transiciones[key].length; i++){
			var estadoSalida = transiciones[key][i].estadoSalida;
			var nuevaCabeceraPila = transiciones[key][i].nuevaCabezaPila.join('');
			transicionesValue = transicionesValue.concat("["+ estadoSalida + ", " + nuevaCabeceraPila + "]");
			if (transiciones[key].length > 1 && (i < transiciones[key].length -1)) 
				transicionesValue = transicionesValue.concat(", ");
		}
		var transLista = transicionesSalida.concat(key.replace("@", "&lambda;") 
				+ " --> " + transicionesValue.replace("@", "&lambda;"));	
		//añadimos a la lista a mostrar en la información
		$("#transiciones").append("<li>"+transLista+ "</li>");		
	}
	$("#pg-examples").fadeOut();
}

function compruebaPalabra(){
	var palabra = $("#palabra").val();
	if (palabra.length==0){
		ModalNotif("¡Debes escribir una palabra!");
		return;
	} else if(palabra.charAt(0) == " "){ //si empieza por blanco es la palabra vacía
		palabra = "+";
	}
	var idAutomata = $("#identificador").val();
	var ruta = "/checkword/" + idAutomata + "/" + palabra;
		$.ajax({
		    url: ruta,
		    type: 'GET',
		    success: function(data){
		    	procesa_respuesta(data, palabra);
		    },
		    error: function(data) {
		    	procesa_respuesta(false, palabra);
		        ModalNotif(data.responseText);
		    }
		});
		
		
}

//Añade a la lista si la palabra está aceptada o no

function procesa_respuesta(data, palabra){
	if (palabra=="+") palabra = "vacia"
	if (data){
		$("#resultados").append("<li><b class=\"palabraAceptada\">"+ palabra + "</b> aceptada.");
	} else {
		$("#resultados").append("<li><b class=\"palabraRechazada\">"+ palabra + "</b> rechazada.");

	}
}

/**
 * Esta función permite vaciar la informacion mostrada del automata generado anteriormente
 * para evitar apilar información innecesaria
 */
function vaciarAutomata(){
	//Vaciamos los resultados anteriores por si generamos un nuevo automata
	$("#alfabetoLenguaje").empty();
	$("#alfabetoPila").empty();
	$("#estadosPila").empty();
	$("#estadoInicial").empty();
	$("#simbInicialPila").empty();
	$("#transiciones").empty();
	$("#resultados").empty();
	$("#pg-definition").fadeIn();
}

function ModalNotif(responseMessage) {
    let ele = $("#modal-notification")
	let secToClose = 4000;
	ele.modal("show");
	if (typeof(responseMessage) !== "undefined") {
		$("#modal-notification-response-text").text(responseMessage)
		setTimeout(function(){
			ele.modal('hide')
		}, secToClose);
	}
}

function UseExample(e) {
	let element = $(e.currentTarget);
	let exampleText = "";
	switch (element.attr("id")) {
		case "useExampleNotDeterministic":
			exampleText = $("#automaton-not-deterministic-code").text();
			break;
		case "useSimpleExampleDeterministic":
			exampleText = $("#automaton-deterministic-code").text();
			break;
		default:
			ModalNotif("Fail at selecting example to use")
			break;
	}
	$("#texto").text(exampleText)
}

// Main point for JS
$(document).ready(function () {
	// first at all, hide all are neccessary
	$("#pg-definition").hide();
	// Insert data examples
	$("#automaton-not-deterministic-code").text(EXAMPLE_NOT_DETERMINISTIC);
	$("#automaton-deterministic-code").text(EXAMPLE_SIMPLE_DETERMINISTIC)
	// Enable hooks
	$("#useExampleNotDeterministic").on('click', UseExample)
	$("#useSimpleExampleDeterministic").on('click', UseExample)
    console.log("Pushdown-generator app ready to works");
});