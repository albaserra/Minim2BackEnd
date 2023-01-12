
function registro() {
    const userid = $('#registro_userId').val();
    const pouname = $('#registro_pouname').val();
    const birthday = $('#registro_birthday').val();
    const email = $('#registro_email').val();
    const email2 = $('#registro_email2').val();
    const password = $('#registro_password').val();
    const password2 = $('#registro_password2').val();

    if (userid === "" || pouname === "" || birthday === "" || email === "" || email2 === "" || password === "" || password2 === ""){
        swal({title:"¡WARNING!", text:"¡Asegurate de que no hayas dejado ningun textbox en blanco!", icon:"info"});
    }else if (email !== email2){
        swal({title:"¡WARNING!", text:"¡Los emails que has introducido no son iguales!", icon:"info"});
    }else if ( password !== password2) {
        swal({title:"¡WARNING!", text:"¡Las contraseñas que has introducido no son iguales!", icon:"info"});
    }else{
        $.post({
            url: '/dsaApp/pougame/pou/registro',
            data: JSON.stringify({"pouId": userid, "nombrePou": pouname, "nacimientoPou": birthday, "correo": email, "password": password}),
            contentType: 'application/json; charset=utf-8'
        })
            .done(function(data, status){
                console.log(status);
                swal({title:"¡Registro con exito!", text:"¡Ahora podrás hacer el login correctamente!", icon:"success"});
                window.location.href = "login.html";
            })
            .fail(function(xhr, err){
                console.log(xhr.status);
                if(xhr.status === 405){
                    swal({title:"¡Ya hay un usuario con ese ID!", text:"¡Escoge otro ID para tu Pou!", icon:"error"});
                }
                else{
                    swal({title:"¡Ya hay una cuenta asociada a este correo!", text:"¡Tienes cuenta, ves a hacer el registro directamente!", icon:"error"});
                    window.location.href = "login.html";
                }
            });
    }
}

function login() {
    const email = $('#login_email').val();
    const password = $('#login_password').val();
    console.log(email);
    console.log(password);
    localStorage.setItem('correo', email);
    localStorage.setItem('password',password);
    if (email === "" || password === "")
        swal({title:"¡WARNING!", text:"¡Asegurate de que no hayas dejado ningun textbox en blanco!", icon:"info"});
    else{
        $.post({
            url: '/dsaApp/pougame/pou/login',
            data: JSON.stringify({"correoPou": email, "passwordPou": password}),
            contentType: 'application/json; charset=utf-8'
        })
            .done(function(data, status){
                swal({title:"¡Login con exito!", text:"¡Preparate para entrar al mundo de tu Pou!", icon:"success"});
                window.location.href = "miPou.html";
            })
            .fail(function(xhr, err){
                console.log(xhr.status);
                if(xhr.status === 405){
                    swal({title:"¡La contraseña introducida es incorrecta!", text:"¡Piensa bien cual es tu contraseña!", icon:"error"});
                }
                else{
                    swal({title:"¡El correo introducido no existe, registrate!", text:"¡Antes de empezar tu aventura debes registrarte!", icon:"error"});
                    window.location.href = "registro.html";
                }
            });
    }
}


function GetListaObjectosTienda(){
    // Create an XMLHttpRequest object
    const xhttp = new XMLHttpRequest();

    // Define a callback function
    xhttp.onload = function() {
        // Here you can use the Data
        console.log("Información recibida");
        const datos = JSON.parse(this.responseText);
        console.log(datos);
        $('#resultados').empty();
        for (var i=0;i<datos.length;++i)
        {
            $("#resultados").append("<tr>"+
                "<td>"+datos[i].nombreArticulo+"</td>"+
                "<td>"+datos[i].articuloId+"</td>"+
                "<td>"+datos[i].precioArticulo+"</td>"+
                "<td>"+datos[i].tipoArticulo+"</td>"+
                "<td>"+datos[i].recargaHambre+"</td>"+
                "<td>"+datos[i].recargaSalud+"</td>"+
                "<td>"+datos[i].recargaDiversion+"</td>"+
                "<td>"+datos[i].recargaSueno+"</td>"+
                "</tr>");
        }
    }

    // Send a request
    console.log("Se envía la petición");
    const url = 'http://localhost:8080/dsaApp/pougame/tienda/listaObjetos';
    //var url='http://147.83.7.203/dsaApp/pougame/tienda/listaObjetos';
    xhttp.open("GET",url, true);
    xhttp.send();
}

function obtenerPouByCredentials(){

    const correo = localStorage.getItem('correo');
    const password = localStorage.getItem('password');

    console.log(correo);
    console.log(password);

    $.post({
        url: '/dsaApp/pougame/perfil/pou',
        data: JSON.stringify({"correoPou": correo, "passwordPou": password}),
        contentType: 'application/json; charset=utf-8',
    })
        .done(function(data, status){

            const pouId = data.pouId;
            localStorage.setItem('pouId', pouId);
            console.log(pouId);

            const nombrePou = data.nombrePou;
            console.log(nombrePou);

            const dineroPou = data.dineroPou;
            console.log(dineroPou);

            const nacimientoPou = data.nacimientoPou;
            console.log(nacimientoPou);

            const correoPou = data.correoPou;
            console.log(correoPou);

            const passwordPou = data.passwordPou;
            console.log(passwordPou);

            const record = data.record;
            console.log(record);

            const nivelHambrePou = data.nivelHambrePou;
            console.log(nivelHambrePou);

            const nivelSaludPou = data.nivelSaludPou;
            console.log(nivelSaludPou);

            const nivelDiversionPou = data.nivelDiversionPou;
            console.log(nivelDiversionPou);

            const nivelSuenoPou = data.nivelSuenoPou;
            console.log(nivelSuenoPou);

            const camisetaId = data.camisetaId;
            const zapatosId = data.zapatosId;
            const gorraId = data.gorraId;
            const gafasId = data.gafasId;

            document.getElementById("idPou").innerText = pouId;
            document.getElementById("nombrePou").innerText = nombrePou;
            document.getElementById("dineroPou").innerText = dineroPou;
            document.getElementById("birthday").innerText = nacimientoPou;
            document.getElementById("mailPou").innerText = correoPou;
            document.getElementById("record").innerText = record;
            document.getElementById("estadoHambre").value = nivelHambrePou;
            document.getElementById("estadoSalud").value = nivelSaludPou;
            document.getElementById("estadoDiversion").value = nivelDiversionPou;
            document.getElementById("estadoSueno").value = nivelSuenoPou;
            swal({title:"¡Cargados los datos del Pou!", text:"¡Ahora puedes navegar por tu web!", icon:"success"});
        })
        .fail(function(xhr, err){
            console.log(xhr.status);
            if(xhr.status === 405){
                swal({title:"¡¡No funciona!!", text:"¡Vuelve a intentarlo!", icon:"error"});
            }
        });

}

/*
function comprarObjeto(idCompra, cantidadCompra, tipo){

    const idPou = localStorage.getItem('pouId');
    console.log(idPou);
    console.log(idCompra);
    console.log(cantidadCompra);
    console.log(tipo);
    let devuelve = 3;

    $.ajax({
        type: 'PUT',
        url: '/dsaApp/pougame/tienda/comprar/' + idPou + '/' + idCompra + '/' + cantidadCompra + '/' + tipo,
        dataType: 'text',
        success: function(data, status){
            activarConfetti()
        },
        error: function(xhr, err){
            const devuelve = 1;
            console.log(xhr.status);
            swal({title:"¡Error al comprar un objeto!", text:"¡Revise los datos de la compra!", icon:"error"});

            if(xhr.status === 406){
                swal({title:"¡Error al comprar un objeto!", text:"¡Revise el ID de la compra!", icon:"error"});
                //devuelve=xhr.status;
            }
            else{
                swal({title:"¡Error al comprar un objeto!", text:"¡No tiene dinero suficiente!", icon:"error"});
                //devuelve=xhr.status;
            }
        }
    });
    console.log(devuelve);
    return devuelve;
}

 */

function tiendaAcciones(idCompra, cantidadCompra, tipo, nombreArticulo, precioArticulo){

    const idPou = localStorage.getItem('pouId');
    console.log(idPou);
    console.log(idCompra);
    console.log(cantidadCompra);
    console.log(tipo);

    $.ajax({
        type: 'PUT',
        url: '/dsaApp/pougame/tienda/comprar/' + idPou + '/' + idCompra + '/' + cantidadCompra + '/' + tipo,
        dataType: 'text',
        success: function(data, status){
            activarConfetti(nombreArticulo, idCompra, tipo, cantidadCompra, precioArticulo);
        },
        error: function(xhr, err){

            if(xhr.status === 406){
                swal({title:"¡Error al comprar un objeto!", text:"¡Revise el ID de la compra!", icon:"error"});
            }
            else{
                swal({title:"¡Error al comprar un objeto!", text:"¡No tiene dinero suficiente!", icon:"error"});
            }
        }
    });
}

function armarioTipo(){

    const idPou = localStorage.getItem('pouId');
    console.log(idPou);
    const tipo = document.getElementById("auxTipo").innerText;
    console.log(tipo);

// Create an XMLHttpRequest object
    const xhttp = new XMLHttpRequest();

    // Define a callback function
    xhttp.onload = function() {
        // Here you can use the Data
        console.log("Información recibida");
        const datos = JSON.parse(this.responseText);
        console.log(datos);
        let htmlLine = '<p></p>';
        for (var i=0;i<datos.length;++i)
        {
            console.log(datos[i].idArticulo);
            console.log(datos[i].tipoArticulo);
            console.log(datos[i].cantidad);
            htmlLine+='<div class="card__objeto">' +
                '<div class="card__cabecera">' +
                '<h3 style="text-transform: uppercase">' + datos[i].idArticulo + '</h3>' +
                '<img class="imgTienda" src="img/articulo_tienda_' + datos[i].idArticulo + '.png" alt="">' +
                '<p class="cantidad" id="cantidadAgua">Cantidad: ' + datos[i].cantidad + '</p>' +
                '</div>' +
                '</div>';
        }
        $('#aux').append(htmlLine);
    }

    // Send a request
    console.log("Se envía la petición");
    const url = 'http://localhost:8080/dsaApp/pougame/armario/tipo/' + idPou + '/' + tipo;
    //var url='http://147.83.7.203/dsaApp/pougame/armario/comida/' + idPou + '/' + tipo;
    xhttp.open("GET",url, true);
    xhttp.send();

}

function borrarDatos(){
    localStorage.clear();
}

function activarConfetti(nombreArticulo, idArticulo, tipoArticulo, cantidadArticulo, precioArticulo){

    $('.popup').empty();
    let htmlLine = '<div class="card__cabecera">' +
        '<h3>' + nombreArticulo + '</h3>' +
        '<img class="imgTienda" src="img/articulo_tienda_' + idArticulo + '.png" alt="">' +
        '<p id="tipoAgua">' + tipoArticulo + '</p>' +
        '<p>Cantidad comprada: ' + cantidadArticulo + '</p>' +
        '<div class="contenedor_precio">' +
        '<p>Precio total: ' + cantidadArticulo*precioArticulo + '</p>' +
        '<img class="monedaImg" src="img/Coin.png">' +
        '</div>' +
        '</div>' +
        '<div>' +
        '<a class="close" onClick="desactivarConfetti()">x</a>' +
        '</div>';
    $('.popup').append(htmlLine);

    let btn = document.querySelector('.btn');
    let close = document.querySelector('.close');
    let popup = document.querySelector('.popup');
    let confe = document.querySelector('#my-canvas');
    popup.classList.add('active')
    confe.classList.add('active')
    const confettiSettings = {target: 'my-canvas'};
    const confetti = new ConfettiGenerator(confettiSettings);
    confetti.render();
}

function desactivarConfetti(){
    let btn = document.querySelector('.btn');
    let close = document.querySelector('.close');
    let popup = document.querySelector('.popup');
    let confe = document.querySelector('#my-canvas');
    popup.classList.remove('active')
    confe.classList.remove('active')
    var confettiSettings = { target: 'my-canvas' };
    var confetti = new ConfettiGenerator(confettiSettings);
    confetti.render();
}

/*
function tiendaAcciones(idCompra, cantidadCompra, tipo, nombreArticulo, precioArticulo){
    //const solucion = comprarObjeto(idCompra, cantidadCompra, tipo);
    //console.log(solucion);
    //comprarObjeto(idCompra, cantidadCompra, tipo);
    //activarConfetti(nombreArticulo, idCompra, tipo, cantidadCompra, precioArticulo);


    if (solucion === 201){
        activarConfetti(nombreArticulo, idCompra, tipo, cantidadCompra, precioArticulo);
    }



}
*/
