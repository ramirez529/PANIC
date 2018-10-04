$(document).ready(function() {



  var tel_final = localStorage.getItem('tel_v');
  if ( tel_final === null ){
    $('#modal-informacion').modal('show')
    $("#mensaje-modal").text("Necesitas configurar esta aplicación para poder usarla, enseguida se abrirá la ventana de ajustes")
    setTimeout(function() {
      $('#modal-informacion').modal('hide')
            $('#modal-ajustes').modal('show')
    }, 4000);
  }else {
    $('input#telefono').value = tel_final
}

  $( "#limpiar-btn" ).click(function() {
    $('#modal-ajustes').modal('hide')
    localStorage.clear();
    $('#modal-informacion').modal('show')
    $("#mensaje-modal").text("Se han limpiado tus Ajustes.\nEsta página se actualizará automáticamente ")
    setTimeout(function() {
          location.reload(true);
    }, 3000);


  });

  //EndPoint para petición Breeze
  var endpoint = "https://breeze2-213.collaboratory.avaya.com/services/EventingConnector/events";
  var bfamily = "AAAMIACTC";
  var btype = "AAAMIACTCPANIC";
  var bversion = "1.0";


  $("#ajustes-frm").submit(function() {
    event.preventDefault();
    var datos = $("#ajustes-frm").serializeArray();
    var tel_v = datos["0"].value;
    console.log(tel_v);
    localStorage.setItem("tel_v", tel_v);
    $('#modal-ajustes').modal('hide')
    $('#modal-informacion').modal('show')
    $("#mensaje-modal").text("Tus ajustes se han guardado")
    setTimeout(function() {
      $('#modal-informacion').modal('hide')
    }, 3000);
  });

  if (navigator.geolocation) {
    console.log('Soportado');
    var startPos;
    var geoSuccess = function(position) {
      startPos = position;
      var lat = startPos.coords.latitude;
      var long = startPos.coords.longitude;
      var coordenadas = lat + "," + long;
      console.log("Coordenadas: " + coordenadas);
      var tel_final = localStorage.getItem('tel_v');
      var eventBody = "{\"phoneNumber\":\"" + tel_final + "\",\"latitude\":\"" + lat + "\",\"longitude\":\"" + long + "\"}";
      postbreeze(bfamily, btype, bversion, tel_final, endpoint, eventBody);

    };
  } else {
    console.log('Geolocation no disponible');
    console.log('Ha ocurrido un error con los permisos del GPS ');
    $('#modalerrores').modal('show');
    $('#mensajeerror').text("Ha ocurrido un Error con los permisos del GPS ");
  }

  $("#callbtn").click(function() {
    var geoError = function(error) {
      switch (error.code) {
        case 0:
          console.log('Ha ocurrido un Error Desconocido con el GPS: ');
          $('#modalerrores').modal('show');
          $('#mensajeerror').text("Ha ocurrido un Error Desconocido con el GPS: ");
          break;
        case 1:
          console.log('Ha ocurrido un error con los permisos del GPS ');
          $('#modalerrores').modal('show');
          $('#mensajeerror').text("Ha ocurrido un Error con los permisos del GPS ");
          break;
        case 2:
          console.log('Tu equipo no cuenta con GPS o no se puede acceder a el');
          $('#modalerrores').modal('show');
          $('#mensajeerror').text("Tu equipo no cuenta con GPS o no se puede acceder a el");
          break;
        case 3:
          console.log('No se pudo conectar al GPS');
          $('#modalerrores').modal('show');
          $('#mensajeerror').text("No se puede conectar al GPS");
          break;
        default:
          console.log('Este error no se ha implementadp:' + error.code);
          $('#modalerrores').modal('show');
          $('#mensajeerror').text("Este error no s eha implementado" + error.code);
          break;
      }
    };
    navigator.geolocation.getCurrentPosition(geoSuccess, geoError);

    //Por si queremos qu eesté reportando en Tiempo Real las Coordenadas

    // var watchId = navigator.geolocation.watchPosition(function(position) {
    //   var lat = startPos.coords.latitude;
    //   var long = startPos.coords.longitude;
    //   var coordenadas = lat + "," + long;
    //   console.log("Coordenadas RealTime: " + coordenadas);
    // });
    if (typeof(Storage) !== "undefined") {} else {
      console.log("tu dispositivo no cuenta con localStorage");
    }

  });
});



function postbreeze(bfamily, btype, bversion, tel_final, endpoint, eventBody) {
  var data = new FormData();
  data.append("family", bfamily);
  data.append("type", btype);
  data.append("version", bversion);
  data.append("eventBody", eventBody);
  var xhr = new XMLHttpRequest();
  xhr.addEventListener("readystatechange", function() {
    if (this.readyState === 4) {
      alert('Submit')
    }
  });
  xhr.open("POST", endpoint);
  xhr.send(data)
}
