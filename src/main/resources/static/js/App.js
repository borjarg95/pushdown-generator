//Metodo que recupera las imagenes buscadas y las muestra
function generarDefinicionLarga() {
	var texto= $("#texto").val();
	if (texto.length==0){
		alert("¡Debes escribir una descripción!")
	}
	var busqueda = "/Generate"
		$.ajax({
		    url : busqueda,
		    type: "POST",
		    data : texto,
		    success: function(data){
		    	procesarAutomataResultante(data);
		    },
		    error: function (mensaje, textStatus)
		    {
		    	debugger;
		    	alert(mensaje.responseText);
		    }
		});
}	

function procesarAutomataResultante(automata){
	//Vaciamos los resultados anteriores por si generamos un nuevo automata
	$("#infoAutomata").removeClass('hidden');
	$("#alfabetoLenguaje").empty();
	$("#alfabetoPila").empty();
	$("#estadosPila").empty();
	$("#estadoInicial").empty();
	$("#simbInicialPila").empty();
	$("#transiciones").empty();
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
		for (i= 0; i < transiciones[key].length; i++){
			var estadoSalida = transiciones[key][i].estadoSalida;
			var nuevaCabeceraPila = transiciones[key][i].nuevaCabezaPila.join('');
			transicionesValue = transicionesValue.concat("["+ estadoSalida + ", " + nuevaCabeceraPila + "]");
			if (transiciones[key].length > 1 && (i < transiciones[key].length -1)) 
				transicionesValue = transicionesValue.concat(", ");
		}
		transLista = transicionesSalida.concat(key.replace("@", "&lambda;") 
				+ " --> " + transicionesValue.replace("@", "&lambda;"));	
		//añadimos a la lista a mostrar en la información
		$("#transiciones").append("<li>"+transLista+ "</li>");		
	}
}

function compruebaPalabra(){
	var palabra = $("#palabra").val();
	if (palabra.length==0){
		alert("¡Debes escribir una palabra!");
		return;
	}
	var idAutomata = $("#identificador").val();
	debugger;
	var ruta = "/CheckWord/" + idAutomata + "/" + palabra;
		$.getJSON(ruta, function(data){
			debugger;
			alert(data);
			procesa_respuesta(data);
		});	
}

function procesa_respuesta(data){
	
}