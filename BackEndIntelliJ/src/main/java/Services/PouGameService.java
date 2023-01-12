package Services;

import Entities.Exceptions.*;
import Entities.ObjetoArmario;
import Entities.ObjetoTienda;
import Entities.Pou;
import Entities.ValueObjects.Credenciales;
import Entities.ValueObjects.InfoRegistro;
import Entities.ValueObjects.InformacionPou;
import Managers.PouGameDBManagerImpl;
import Managers.PouGameManager;
import Managers.PouGameManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/pougame", description = "Endpoint to Pou Game Service")
@Path("/pougame")

public class PouGameService {
    private PouGameManager jvm;

    final static org.apache.log4j.Logger logger = Logger.getLogger(PouGameManagerImpl.class);

    public PouGameService() throws PouIDYaExisteException, CorreoYaExisteException, ObjetoTiendaYaExisteException, SalaYaExisteException, PouIDNoExisteException {
        this.jvm = PouGameDBManagerImpl.getInstance();
        if (jvm.size()==0) {
            //this.jvm.crearPou("marcmmonfort", "Marc", "28/10/2001", "marc@gmail.com", "28102001");
            //this.jvm.crearPou("victorfernandez", "Victor", "13/06/2001", "victor@gmail.com", "13062001");
            //this.jvm.crearPou("albaserra", "Alba", "29/06/2001", "alba@gmail.com", "29062001");


           // this.jvm.addObjetosATienda("B001","Manzana",1,"Comida",10,0,0,0 );
           // this.jvm.addObjetosATienda("B002","Gafas de sol",30,"Ropa",0,0,0,0);

            //this.jvm.addObjetosATienda("C001","Manzana",1,"Comida",10,0,0,0 );
            //this.jvm.addObjetosATienda("B001","Agua",4,"Bebida",4,4,0,0);
            //this.jvm.addObjetosATienda("P001","Salud",10,"Pocion",0,20,0,0);
            //this.jvm.addObjetosATienda("R001","Gafas de sol",30,"Ropa",0,0,0,0);

        }
    }

    // OPERACION 1: Registro.
    // MÉTODO HTTP: POST.
    // ESTRUCTURA: public void crearPou(String pouId, String nombrePou, String nacimientoPou, String correo, String password);
    // EXCEPCIONES: CorreoYaExisteException, PouIDYaExisteException

    @POST
    @ApiOperation(value = "Registro", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Juego creado satisfactoriamente"),
            @ApiResponse(code = 404, message = "Ya existe el correo"),
            @ApiResponse(code = 405, message = "Ya existe el PouID")
    })
    @Path("/pou/registro")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response crearPou(InfoRegistro infoRegistro){
        try{
            this.jvm.crearPou(infoRegistro.getPouId(), infoRegistro.getNombrePou(), infoRegistro.getNacimientoPou(), infoRegistro.getCorreo(), infoRegistro.getPassword());
        }catch(CorreoYaExisteException e){
            return Response.status(404).build();
        }catch (PouIDYaExisteException e){
            return Response.status(405).build();
        }
        return Response.status(200).build();
    }

    // OPERACION 2: Login.
    // MÉTODO HTTP: POST.
    // ESTRUCTURA: public void loginPou(String correo, String password);
    // EXCEPCIONES: CorreoNoExisteException, PasswordIncorrectaException

    @POST
    @ApiOperation(value = "Login", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login correcto"),
            @ApiResponse(code = 404, message = "El correo no exite"),
            @ApiResponse(code = 405, message = "Contraseña incorrecta")
    })
    @Path("/pou/login")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response loginPou(Credenciales credenciales){
        try{
            this.jvm.loginPou(credenciales.getCorreoPou(), credenciales.getPasswordPou());
        }catch(CorreoNoExisteException e){
            return Response.status(404).build();
        }catch (PasswordIncorrectaException e){
            return Response.status(405).build();
        }
        return Response.status(200).build();
    }

    // OPERACION 3: Obtener la lista de objetos de la tienda
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public List<ObjetoTienda> obtenerObjetosTienda();
    // EXCEPCIONES:-

    @GET
    @ApiOperation(value = "Obtener la lista de objetos de la tienda", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "¡Hecho!", response = ObjetoTienda.class, responseContainer="List"),
    })
    @Path("/tienda/listaObjetos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerObjetosTienda() {
        GenericEntity<List<ObjetoTienda>> listaObjetosTienda = new GenericEntity<List<ObjetoTienda>>(this.jvm.obtenerObjetosTienda()) {};
        return Response.status(200).entity(listaObjetosTienda).build();
    }

    // OPERACION 4: Obtener un Pou
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public Pou obtenerPou(String pouId) throws PouIDNoExisteException;
    // EXCEPCIONES:-
    @GET
    @ApiOperation(value = "Obtener un Pou", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "¡Hecho!", response = Pou.class),
    })
    @Path("/perfil/pou_id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPou(String id) throws PouIDNoExisteException {
        GenericEntity<Pou> miPou = new GenericEntity<Pou>(this.jvm.obtenerPou(id)) {};
        return Response.status(200).entity(miPou).build();
    }

    // OPERACIÓN 14: AÑADIR ELEMENTO ARMARIO POU (POU COMPRA UN OBJETO DE UNA SALA) (HAY QUE PONER CUANTOS)
    // MÉTODO HTTP: PUT.
    // ESTRUCTURA: public void pouCompraArticulos(String pouId, String articuloId, Integer cantidad, String tipoArticulo);
    // EXCEPCIONES: ObjetoTiendaNoExisteException, PouIDNoExisteException, PouNoTieneDineroSuficienteException

    @PUT
    @ApiOperation(value = "Comprar objeto", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!"),
            @ApiResponse(code = 407, message = "Dinero insuficiente"),
            @ApiResponse(code = 406, message = "El objeto introducido no existe")
    })
    @Path("/tienda/comprar/{idPou}/{idCompra}/{cantidadCompra}/{tipo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprarObjeto(@PathParam("idPou") String idPou,@PathParam("idCompra") String idCompra,@PathParam("cantidadCompra") String cantidadCompra,@PathParam("tipo") String tipo) {
        try {
            this.jvm.pouCompraArticulos(idPou, idCompra, Integer.parseInt(cantidadCompra), tipo);
        }catch (ObjetoTiendaNoExisteException e) {
            return Response.status(406).build();
        }catch (PouNoTieneDineroSuficienteException e) {
            return Response.status(407).build();
        }catch (PouIDNoExisteException e){
            return Response.status(405).build();
        }
        return Response.status(201).build();
    }

    // OPERACION 31: OBTENER UN POU A PARTIR DE UNOS CREDENCIALES
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public Pou obtenerPouByCredentials(Credenciales credenciales);
    // EXCEPCIONES: -
    @POST
    @ApiOperation(value = "Obtener un Pou", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "¡Hecho!", response = Pou.class)
    })
    @Path("/perfil/pou")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response obtenerPouByCredentials(Credenciales credenciales){
        GenericEntity<Pou> miPou = new GenericEntity<Pou>(this.jvm.obtenerPouByCredentials(credenciales)) {};
        return Response.status(200).entity(miPou).build();
    }

    // OPERACION X: OBTENER UNA LISTA DE UN TIPO DE ARMARIO A PARTIR DE UNOS ID DE USUARIO Y UN TIPO DE ARMARIO
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public List<ObjetoArmario> obtenerObjetosArmarioPouTipo (String pouId, String tipoArticulo);
    // EXCEPCIONES: -

    @GET
    @ApiOperation(value = "Obtener una lista de objetos del armario de un tipo en concreto y del usuario correspondiente", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!", response = ObjetoArmario.class, responseContainer="List"),
    })
    @Path("/armario/tipo/{idPou}/{tipo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerObjetosArmarioPouTipo(@PathParam("idPou") String idPou, @PathParam("tipo") String tipo) {

        List<ObjetoArmario> listaObjetos = this.jvm.obtenerObjetosArmarioPouTipo(idPou, tipo);

        GenericEntity<List<ObjetoArmario>> enviarListaObjetos = new GenericEntity<List<ObjetoArmario>>(listaObjetos) {};
        return Response.status(201).entity(enviarListaObjetos).build();
    }

    // OPERACION ANDROID 1: OBTENER UNA LISTA CON TODA LA INFORMACIÓN NECESARIA DEL USUARIO
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public List<ObjetoArmario> obtenerObjetosArmarioPouTipo (String pouId, String tipoArticulo);
    // EXCEPCIONES: -
    @GET
    @ApiOperation(value = "Obtener una lista de objetos del armario de un tipo en concreto y del usuario correspondiente", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!", response = InformacionPou.class),
    })
    @Path("/pou/cargarDatos/{gmail}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoAndroidPou(@PathParam("gmail") String gmail, @PathParam("password") String password) throws PouIDNoExisteException {

        Credenciales credenciales = new Credenciales(gmail, password);

        InformacionPou informacionPou= this.jvm.getInfoAndroidPou(credenciales);

        GenericEntity<InformacionPou> enviarListaObjetosAndroid = new GenericEntity<InformacionPou>(informacionPou) {};
        return Response.status(201).entity(enviarListaObjetosAndroid).build();
    }

    // OPERACIÓN ANDROID 2: MODIFICAMOS LAS TABLAS CON LOS NUEVOS VALORES DE LA APP
    // MÉTODO HTTP: PUT.
    // ESTRUCTURA:
    // EXCEPCIONES: ObjetoTiendaNoExisteException, PouIDNoExisteException, PouNoTieneDineroSuficienteException

    @PUT
    @ApiOperation(value = "Actualizar los datos", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!")
    })
    @Path("/pou/actualizarDatos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObjetoArmario(InformacionPou informacionPou) {

        this.jvm.updateAndroid(informacionPou);

        return Response.status(201).build();
    }

    // OPERACIÓN ANDROID 3: COMPROBAMOS SI EL CORREO CAMBIADO ESTÁ DISPONIBLE.
    // MÉTODO HTTP: PUT.
    // ESTRUCTURA: -
    // EXCEPCIONES: CorreoYaExisteException

    @PUT
    @ApiOperation(value = "Comprobar el correo", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!"),
            @ApiResponse(code = 405, message = "¡El correo ya existe!")
    })
    @Path("pou/comprobarCorreo/{gmail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprobarCorreo(@PathParam("gmail") String gmail) throws CorreoYaExisteException{
        try {
            this.jvm.comprobarCorreo(gmail);
        }catch (CorreoYaExisteException e) {
            return Response.status(405).build();
        }
        return Response.status(201).build();
    }

    // OPERACIÓN ANDROID 4: PEDIR LA INFORMACIÓN DE UN ARTÍCULO DE LA TIENDA POR SU ID.
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: -
    // EXCEPCIONES: -

    @GET
    @ApiOperation(value = "Pedir la información de un artículo de la tienda por su ID.", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Información del artículo obtenida!", response = ObjetoTienda.class)
    })
    @Path("/tienda/obtenerarticulo/{articuloid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInfoObjeto(@PathParam("articuloid") String articuloId) {
        ObjetoTienda objetoTienda = this.jvm.obtenerInfoObjeto(articuloId);

        GenericEntity<ObjetoTienda> enviarObjetoTienda = new GenericEntity<ObjetoTienda>(objetoTienda) {};
        return Response.status(201).entity(enviarObjetoTienda).build();
    }

    // OPERACIÓN ANDROID 5: PEDIR LOS POUS ORDENADOS POR UNA DE LAS COLUMNAS.
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: -
    // EXCEPCIONES: -

    @GET
    @ApiOperation(value = "Pedir la información de los Pous ordenados por una de sus columnas.", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!", response = Pou.class, responseContainer="List"),
    })
    @Path("/pou/ranking/{rankingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPousOrdenadosDescendentemente(@PathParam("rankingId") String rankingId) {
        logger.info("LLEGA AQUÍ");
        List<Pou> listaPous = this.jvm.obtenerPousOrdenadosDescendentemente(rankingId);
        GenericEntity<List<Pou>> enviarListaPous = new GenericEntity<List<Pou>>(listaPous) {};
        return Response.status(201).entity(enviarListaPous).build();
    }

/*
    // OPERACION 8: Obtener los Usuarios que han jugado un cierto Juego ordenados por Puntos (de mayor a menor).
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public List<Usuario> obtenerHistorialUsuariosDeJuego(String juegoId);
    // EXCEPCIONES: JuegoIdNoExisteException.

    @GET
    @ApiOperation(value = "Obtener los Usuarios que han jugado un cierto Juego ordenados por Puntos (de mayor a menor)", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!", response = Pou.class, responseContainer="List"),
            @ApiResponse(code = 405, message = "El juego introducido no existe")
    })
    @Path("/juego/jugadoresdeljuego/{juegoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response funcionObtenerHistorialUsuariosDeJuego(@PathParam("juegoId") String juegoId) {
        List<Pou> historial;
        try{
            historial = this.jvm.obtenerHistorialUsuariosDeJuego(juegoId);
        } catch (JuegoIdNoExisteException e) {
            return Response.status(405).build();
        }
        GenericEntity<List<Pou>> historialJugadores = new GenericEntity<List<Pou>>(historial) {};
        return Response.status(201).entity(historialJugadores).build();
    }


    // OPERACION 9: Obtener las Partidas en las que ha jugado un Usuario.
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public List<Partida> obtenerPartidasUsuario(String usuarioId);
    // EXCEPCIONES: UsuarioIdNoExisteException.

    @GET
    @ApiOperation(value = "Obtener las Partidas en las que ha jugado un Usuario", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!", response = Partida.class, responseContainer="List"),
            @ApiResponse(code = 406, message = "El usuario introducido no existe")
    })
    @Path("/partida/partidasdelusuario/{usuarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response funcionObtenerPartidasUsuarios(@PathParam("usuarioId") String usuarioId) {
        List<Partida> partidas;
        try{
            partidas = this.jvm.obtenerPartidasUsuario(usuarioId);
        } catch (UsuarioIdNoExisteException e) {
            return Response.status(406).build();
        }
        GenericEntity<List<Partida>> partidasUsuario = new GenericEntity<List<Partida>>(partidas) {};
        return Response.status(201).entity(partidasUsuario).build();
    }

    // OPERACION 10: Obtener información sobre las Partidas de un Usuario en un cierto Juego.
    // MÉTODO HTTP: GET.
    // ESTRUCTURA: public InfoPartida obtenerInfoUsuarioJuego(String juegoId, String usuarioId);
    // EXCEPCIONES: -

    @GET
    @ApiOperation(value = "Obtener información sobre las Partidas de un Usuario en un cierto Juego", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Hecho!", response = Estado.class)
    })
    @Path("/partida/participacionusuarioenjuego/{usuarioId}/{juegoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response funcionObtenerInfoUsuarioJuego(@PathParam("usuarioId") String usuarioId, @PathParam("juegoId") String juegoId) {
        Estado information = this.jvm.obtenerInfoUsuarioJuego(juegoId, usuarioId);
        return Response.status(201).entity(information).build();
    }







    // ----------------------------------------------------------------------------------------------------

    // NO IMPLEMENTADO.
    // LO QUE HAY ABAJO ES EL QUE TENIA DE PLANTILLA.


    private ObjetoManager om;

    public ObjetoService() {
        this.om = ObjetoManagerImpl.getInstance();
        if (om.size()==0) {
            this.om.addObjectToShop("A001", "FCB", "Primera Equipación Fan", 30);
            this.om.addObjectToShop("B001", "ATM", "Primera Equipación Jugador", 35);
            this.om.addObjectToShop("C001", "BRUGGE", "Primera Equipación Fan (Ferran Jutgol)", 40);

            this.om.registerUser("Marc", "Moran", "28/10/2001", "marcmoran@gmail.com", "28102001");
            this.om.registerUser("Victor", "Fernandez", "13/06/2001", "victorfernandez@gmail.com", "13062001");
            this.om.registerUser("Eloi", "Moncho", "28/08/2001", "eloimoncho@gmail.com", "28082001");
        }
    }

    // ----------------------------------------------------------------------------------------------------

    // OPERACIÓN 1: Registrar un usuario.
    // MÉTODO HTTP: POST.
    // ACLARACIONES: "0" se puede, "1" ya hay un usuario con ese mail.

    @POST
    @ApiOperation(value = "Registrar un usuario", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Registrado correctamente!"),
            @ApiResponse(code = 404, message = "Fallo en el registro. ¡Mail ya existente!")
    })
    @Path("/usuario")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(Usuario user) {
        int verificador = this.om.registerUser(user.getUsername(), user.getUserSurname(), user.getBirthDate(), user.getCredentials().getEmail(), user.getCredentials().getPassword());
        if (verificador == 0){
            return Response.status(201).build();
        }
        else{
            return Response.status(404).build();
        }
    }

    // OPERACIÓN 2: Obtener una lista de usuarios registrados, ordenada por órden alfabético.
    // MÉTODO HTTP: GET.
    // ACLARACIONES: -

    @GET
    @ApiOperation(value = "Obtener una lista de usuarios registrados", notes = "Por órden alfabético")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Se ha obtenido correctamente la lista!", response = Usuario.class, responseContainer="List")
    })
    @Path("/usuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response usuariosPorOrdenAlfabetico() {
        List<Usuario> usuariosOrdenAlfabetico = this.om.usersByAlphabetOrder();
        GenericEntity<List<Usuario>> usuariosOrdenados = new GenericEntity<List<Usuario>>(usuariosOrdenAlfabetico) {};
        return Response.status(201).entity(usuariosOrdenados).build(); // OK.
    }

    // OPERACIÓN 3: Hacer el login de un usuario.
    // MÉTODO HTTP: POST.
    // ACLARACIONES: "0" se puede, "1" el login no es correcto.

    @POST
    @ApiOperation(value = "Login de un usuario", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Registrado correctamente!"),
            @ApiResponse(code = 404, message = "Fallo en el login. ¡El email y/o la password son incorrectos!")
    })
    @Path("/usuario/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Credenciales credentials) {
        int verificador = this.om.userLogin (credentials.getEmail(), credentials.getPassword());
        if (verificador == 1)  {
            return Response.status(404).build();
        }
        else{
            return Response.status(201).build();
        }
    }

    // OPERACIÓN 4: Añadir un nuevo objeto a la tienda.
    // MÉTODO HTTP: POST.
    // ACLARACIONES: -

    @POST
    @ApiOperation(value = "Añadir un nuevo objeto a la tienda", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Objeto añadido correctamente!"),
    })
    @Path("/tienda")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nuevoObjetoParaTienda(ObjetoTienda objecte) {
        this.om.addObjectToShop(objecte.getObjectId(), objecte.getObjectName(), objecte.getObjectDescription(), objecte.getObjectCoins());
        return Response.status(201).build();
    }

    // OPERACIÓN 5: Obtener una lista de objetos ordenados por precio (de mayor a menor).
    // MÉTODO HTTP: GET.
    // ACLARACIONES: -

    @GET
    @ApiOperation(value = "Obtener una lista de objetos ordenados por precio", notes = "Por órden decreciente (de mayor a menor)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Se ha obtenido correctamente la lista!", response = ObjetoTienda.class, responseContainer="List")
    })
    @Path("/tienda")
    @Produces(MediaType.APPLICATION_JSON)
    public Response objetosPorPrecioDecreciente() {
        List<ObjetoTienda> objetosPrecioDecreciente = this.om.objectsByDescendentPrice();
        GenericEntity<List<ObjetoTienda>> objetosOrdenados = new GenericEntity<List<ObjetoTienda>>(objetosPrecioDecreciente) {};
        return Response.status(201).entity(objetosOrdenados).build(); // OK.
    }

    // OPERACIÓN 6: Compra de un objeto por parte de un usuario.
    // MÉTODO HTTP: PUT.
    // ACLARACIONES: "0" se puede, "1" no existe el usuario, "2" no hay saldo suficiente.

    @PUT
    @ApiOperation(value = "Compra de un objeto por parte de un usuario", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Se ha realizado la compra!"),
            @ApiResponse(code = 404, message = "No se ha ralizado la compra. ¡No existe el usuario!"),
            @ApiResponse(code = 406, message = "No se ha realizado la compra. ¡El usuario no tiene saldo suficiente!")
    })
    @Path("/tienda/{objectId}/{userId}")
    @Produces(MediaType.APPLICATION_JSON) // ESTO
    public Response compraObjetoPorUsuario(@PathParam("objectId") String idObjeto, @PathParam("userId") String idUsuario) {
        int verificador = this.om.buyObjectByUser(idObjeto, idUsuario);
        if (verificador == 0) {
            return Response.status(201).build();
        } else if (verificador == 1) {
            return Response.status(404).build();
        }
        else { // verificador == 2
            return Response.status(406).build();
        }
    }

    // OPERACIÓN 7: Obtener una lista de los objetos comprados por un usuario.
    // MÉTODO HTTP: GET.
    // ACLARACIONES: -

    @GET
    @ApiOperation(value = "Obtener una lista de objetos comprados por un usuario", notes = "-")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "¡Se ha obtenido correctamente la lista!", response = ObjetoTienda.class, responseContainer="List")
    })
    @Path("/tienda/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response objetosCompradosPorUsuario(@PathParam("userId") String idUsuario) {
        List<ObjetoTienda> objetosComprados = this.om.objectBoughtByUser(idUsuario);
        GenericEntity<List<ObjetoTienda>> adquisiciones = new GenericEntity<List<ObjetoTienda>>(objetosComprados) {};
        return Response.status(201).entity(adquisiciones).build(); // OK.
    }
    */
}

