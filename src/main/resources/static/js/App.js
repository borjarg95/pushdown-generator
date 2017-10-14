//Metodo que recupera las imagenes buscadas y las muestra
function generarDefinicionLarga() {
	var texto= $("#texto").val(); //tiene ese texto en nombre de foto, descripcion...
	if (texto.length==0){
		alert("¡Debes escribir una descripción!")
	}
	var busqueda = "/Generate"
//		$.postJSON(busqueda, function(data){
//				mostrar_fotos(data);
//			});		
		debugger;
		$.ajax({
		    url : busqueda,
		    type: "POST",
		    data : texto,
		    success: function(data, textStatus, jqXHR)
		    {
		    	debugger;
		    	print(data);
		    },
		    error: function (jqXHR, textStatus, errorThrown)
		    {
		    	alert("Vaya mierda de creación");
		    }
		});
}	
		//request with URL,success callback
//		$.post(busqueda,function(data)
//		{
//			debugger;
//			print(data)
//		});

//Método que muestra cada imagen recuperada de la búsqueda
function mostrar_fotos(info) { 
	$("#imagenes").empty(); //Para que los resultados no se apilen con cada busqueda 
	for (var i = 0; i < info.photos.photo.length; i++) {
		var item = info.photos.photo[i];
		var url = 'https://farm' + item.farm + ".staticflickr.com/" + item.server + '/' + item.id + '_' + item.secret + '_m.jpg';
		$("#imagenes").append($('<img class="icon-img img-responsive" onclick="mostrar_modal(this)"></img>').attr("src", url));
	}
}

//Función que amplia una imagen junto a su informacióny listas asociadas 
function mostrar_modal(img){

	/*1 - VACIAMOS CONTENEDORES CON LA INFORMACIÓN 
		DE LA IMAGEN PREVISUALIZADA ANTERIORMENTE 
		PARA EVITAR SOLAPAMIENTOS.
	*/
	$('#setPhoto').empty();
	$('#poolPhoto').empty();
	$('#galeriesPhoto').empty();
	$('#descripcionBody').empty();
	$('#photo-body').empty();
	$('#titlePhoto').empty();
	
	/* 2 - ADAPTACIÓN DE TAMAÑO DE IMAGEN
		EN FUNCIÓN DE DONDE HA SIDO SELECCIONADA:
		*/
	//si la imagen es de la busqueda central
	var big = img.src.replace("_m.jpg", "_c.jpg");
	//si la imagen es de la lista seleccionada
	var big = big.replace("_s.jpg", "_c.jpg");

	//3 - GENERACIÓN DE IMAGEN AMPLIADA Y OBTENCIÓN DE ID_PHOTO
	var img = '<img class="img-modal img-responsive" id="fotoAmpliada" src="'+big+'"></img>';	
	var fotoSplit = big.split('/');
	var idPhoto = fotoSplit[4].split('_')[0];
	$("#photo-body").append(img);
	$('#myModal').modal("show");	
	
	//4 - OBTENER DE INFORMACIÓN DE LA IMAGEN
	//Carga descripción 
	$.getJSON('https://api.flickr.com/services/rest/?&method=flickr.photos.getInfo&api_key=' + api_key + '&photo_id=' + idPhoto + '&format=json&nojsoncallback=1',
	function(info){
		var descr = info.photo.description._content;
		var titulo = info.photo.title._content;
		//guardamos el user_id paramostrar el resto de fotos en el botón interior
		var usuario = info.photo.owner.nsid;
		
		//Si no tiene título, no incluye el rótulo
		if (titulo.length !=0){
			$("#titlePhoto").append("&emsp;<b>Título:</b>&emsp;" +titulo);
		}
		//Si no tiene descripción, no incluye el rótulo de la descripción
		if (descr.length !=0){ //&emsp; --> inserta una tabulación en el html
			$("#descripcionBody").append("&emsp;<b>Descripción:</b><br><br>&emsp;&emsp;" +descr); //Tabulamos el rotulo "descripción" y el contenido del mismo
		}
		//Almacenamos el identificación del usuario para poder reutilizarlo en otras consultas
		document.getElementById("usuario").value = usuario;
	});
	
	//Carga los grupos y listas a los que pertenece la imagen
	$.getJSON('https://api.flickr.com/services/rest/?&method=flickr.photos.getAllContexts&api_key=' + api_key + '&photo_id=' + idPhoto + '&format=json&nojsoncallback=1',
	function(info){
		cargarContextos(info);
	});
	
	//Carga las galerias públicas a las que pertenece:   	
	$.getJSON('https://api.flickr.com/services/rest/?&method=flickr.galleries.getListForPhoto&api_key=' + api_key + '&photo_id=' + idPhoto + '&format=json&nojsoncallback=1',
	 function(info){
		 cargarGalerias(info);
	 });
}

	/**Generamos una llamada al metodo feedLista que mostrará las imagenes de la colección 
	*	y a su vez guardará la lista en las seleccionadas del mismo módo para:
	*	-Álbumes
	*	-Grupos
	*	-Galerías públicas
	*en las listas guardadas para futura consulta
	*/

//Método que carga los grupos y álbumes a los que pertenece la foto generando links para posibles consultas de sus imagenes
function cargarContextos(info){	
		//identificamos S (albumes), P (grupos) para poder realimentar la categoría adecuada en la lista de listas seleccionadas
	for (var i=0; i< info.set.length;i++){
		
		var item = info.set[i];
		set = '<a href=# onclick=\"feedListas(\''+item.id+'\',\'S\',\''+item.title+'\');\">'+item.title + '</a>';
		elementoLista = "<li>"+ set + "</li>";
		//añadimos a la lista a mostrar en la información
		$("#setPhoto").append(elementoLista);
	}
	for (var j=0; j< info.pool.length;j++){			
		var item = info.pool[j];
		var idGroup = item.id;
		pool = '<a href=# onclick=\"feedListas(\''+idGroup+'\',\'P\',\''+item.title+'\');\">'+item.title + '</a>';
		elementoLista = "<li>"+ pool + "</li>";
		$("#poolPhoto").append(elementoLista);
	}					
}

//Método que carga las galerias
function cargarGalerias(info){
	var galerias = info.galleries;
	if (galerias.total ==0){
		return;
	} else{
		//identificamos G (gallerias) con el mismo procesado explicado en el metodo "cargarContextos"
		for (var i=0; i< galerias.total;i++){
			
			var item = galerias.gallery[i];
			gallery = '<a href=# onclick=\"feedListas(\''+item.id+'\',\'G\',\''+item.title._content+'\');\">'+item.title._content + '</a>';
			elementoLista = "<li>"+ gallery + "</li>";			
			$("#galeriesPhoto").append(elementoLista);
		}
	}
}

function feedListas(id, tipo, nombre){
	mostrar_fotosContextos(id, tipo);	
	enlace = '<a href=# onclick=mostrar_fotosContextos(\''+id+'\',\''+tipo+'\');>'+nombre + '</a>';
	elementoLista = "<li>"+ enlace + "</li>";
	var i = 0;
	//Añadimos a la lista a mostrar en la información
	switch(tipo) {
		case 'S': //Album
			var listaAlbumes = document.getElementById("albumesGuardados").childNodes;
			for (i=0; i<listaAlbumes.length; i++){
				if (listaAlbumes[i].innerText == nombre){
					break;
				}		
			}
			if (i == listaAlbumes.length){
				$("#albumesGuardados").append(elementoLista);				
			}
			break;
		case 'G': //Galerías
			var listaGalerias = document.getElementById("galeriasGuardadas").childNodes;
			for (i=0; i<listaGalerias.length; i++){
				if (listaGalerias[i].innerText == nombre){
					break;
				}		
			}
			if (i == listaGalerias.length){		
				$("#galeriasGuardadas").append(elementoLista);
			}
			break;
		case 'P': //Grupos
			var listaGrupos = document.getElementById("gruposGuardados").childNodes;
			for (i=0; i<listaGrupos.length; i++){
				if (listaGrupos[i].innerText == nombre){
					break;
				}		
			}
			if (i == listaGrupos.length){			
				$("#gruposGuardados").append(elementoLista);
			}
			break;
	}	
}

//Este método nos permite añadir una imagen a la lista de fotos seleccionadas
function aniadirASel(){
	var aux = $('#photo-body');
	var rutaImagen = aux[0].childNodes[0].currentSrc;	
	var miniatura = rutaImagen.replace("_c.jpg", "_s.jpg"); 
	//Comprobamos que la foto no se encuentra en las ya seleccionadas
	var divFotos = document.getElementById("select-pictures").childNodes;
	for (var i=0; i<divFotos.length; i++){
		if (divFotos[i].currentSrc == miniatura){
			alert("¡La imagen ya ha sido seleccionada!");
			return;
		}		
	}
	$("#select-pictures").append($('<img class="icon-img img-responsive" onclick="mostrar_modal(this)"></img>').attr("src", miniatura));
}

//Metodo que muestra las fotos de un usuario específico tras pulsar el botón de "Ver más del usuario"
function mostrar_fotosUsuario() {	
	var usuario = document.getElementById("usuario").value;
	var busqueda = 'https://api.flickr.com/services/rest/?&method=flickr.photos.search&api_key=' 
		+ api_key +'&user_id=' + usuario + '&format=json&nojsoncallback=1';		
		$.getJSON(busqueda,function(data){
				mostrar_fotos(data);
			});
	//Despues de precargar todas las fotos del usuario, cerramos la imagen ampliada para ver el resto de imagenes
	$('#cerrarImg').click();
}

//Metodo que muestra las fotos de la lista seleccionada anteriormente
function mostrar_fotosContextos(id, tipo) {
	var busqueda = 'https://api.flickr.com/services/rest/?&method=flickr.photos.search&api_key=' + api_key;
	
	//En función del caracter de entrada (S,P,G), cargaremos la lista seleccionada	
	switch(tipo) {
		case 'S': //Album	
			var usuario = document.getElementById("usuario").value;
			busqueda = busqueda.concat("&photoset_id=" + id + "&user_id="+ usuario);
			busqueda = busqueda.replace("flickr.photos.search", "flickr.photosets.getPhotos");
			busqueda = busqueda.concat('&format=json&nojsoncallback=1');
			$.getJSON(busqueda, function(data){
				mostrar_fotosAlbum(data);
			});
			//Despues de precargar todas las fotos, cerramos la imagen ampliada para ver el resto de imagenes
			$('#cerrarImg').click();			
			return;
			
		case 'P': //Grupo
			debugger;
			busqueda = busqueda.concat("&group_id="+ id);
			break;
			
		case 'G': //Grupo
			busqueda = busqueda.concat("&gallery_id="+ id);
			busqueda = busqueda.replace("flickr.photos.search","flickr.galleries.getPhotos");
			break;			
	}
	busqueda = busqueda.concat('&format=json&nojsoncallback=1');
		$.getJSON(busqueda, function(data){
				mostrar_fotos(data);
			});
	//Despues de precargar todas las fotos, cerramos la imagen ampliada para ver el resto de imagenes
	$('#cerrarImg').click();

}

//Método que muestra las imagenes de un album (set)
function mostrar_fotosAlbum(info) { 
	$("#imagenes").empty(); //Para que los resultados no se apilen con cada busqueda

	for (var i = 0; i < info.photoset.photo.length; i++) {
		var item = info.photoset.photo[i];
		var url = 'https://farm' + item.farm + ".staticflickr.com/" + item.server + '/' + item.id + '_' + item.secret + '_m.jpg';
		$("#imagenes").append($('<img class="icon-img img-responsive" onclick="mostrar_modal(this)"></img>').attr("src", url));
	}
}