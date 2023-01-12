package Managers;

import Entities.Exceptions.*;
import Entities.ObjetoArmario;
import Entities.ObjetoTienda;
import Entities.Pou;
import Entities.ValueObjects.Credenciales;
import Entities.ValueObjects.InformacionPou;

import java.util.List;
import java.util.Map;

public interface PouGameManager {

    // OPERACIÓN 1: OBTENER NÚMERO DE POUS
    // DEVUELVE: Integer.
    // EXCEPCIONES: -

    public int size();

    // OPERACIÓN 2: REGISTRO POU (CREAR POU)
    // DEVUELVE: -
    // EXCEPCIONES: (CORREO YA EXISTE) / (pouID YA EXISTENTE)

    public void crearPou(String pouId, String nombrePou, String nacimientoPou, String correo, String password) throws
            CorreoYaExisteException, PouIDYaExisteException;

    // OPERACIÓN 3: LOGIN POU
    // DEVUELVE: -
    // EXCEPCIONES: (CORREO NO EXISTE) / (CONTRASEÑA DEL CORREO INCORRECTA)

    public void loginPou(String correo, String password) throws
            CorreoNoExisteException, PasswordIncorrectaException;

    public List<ObjetoTienda> obtenerObjetosTienda();

    // OPERACIÓN 5: OBTENER TODOS LOS POUS
    // DEVUELVE: Map<String, Pou>
    // EXCEPCIONES: -

    ObjetoTienda obtenerInfoObjeto(String articuloId);

    public Map<String, Pou> obtenerPous();

    // OPERACIÓN 6: OBTENER EL ARMARIO DE UN POU POR SU ID ("pouId")
    // DEVUELVE: Map<String, ObjetoArmario>
    // EXCEPCIONES: (NO EXISTE ESTE POU)

    public  Map<String, ObjetoArmario> obtenerObjetosArmarioPou(String pouId);

    // OPERACIÓN 6: OBTENER POU POR SU ID ("pouId")
    // DEVUELVE: Pou
    // EXCEPCIONES: (NO EXISTE ESTE POU)
    public Pou obtenerPou(String pouId) throws
            PouIDNoExisteException;

    // OPERACIÓN 7: AÑADIR OBJETOS A LA TIENDA
    // DEVUELVE: -
    // EXCEPCIONES: (EL ARTICULO YA EXISTE)

    public void addObjetosATienda(String articuloId, String nombreArticulo, Integer precioArticulo, String tipoArticulo, Integer recargaHambre, Integer recargaSalud, Integer recargaDiversion, Integer recargaSueno) throws
            ObjetoTiendaYaExisteException;

    // OPERACIÓN 8: OBTENER OBJETO DE LA TIENDA POR SU ID ("articuloId")
    // DEVUELVE: ObjetoTienda
    // EXCEPCIONES: (NO EXISTE ESTE ARTICULO)

    public ObjetoTienda obtenerObjetoTienda(String articuloId) throws
            ObjetoTiendaNoExisteException;

    // OPERACIÓN 9: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS COMIDAS DE LA TIENDA
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoTienda> obtenerComidasTienda();

    // OPERACIÓN 10: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS BEBIDAS DE LA TIENDA
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoTienda> obtenerBebidasTienda();

    // OPERACIÓN 11: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS POCIONES DE LA TIENDA
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoTienda> obtenerPocionesTienda();

    // OPERACIÓN 12: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS ROPAS DE LA TIENDA
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoTienda> obtenerRopasTienda();

    // OPERACIÓN 14: AÑADIR ELEMENTO ARMARIO POU (POU COMPRA UN OBJETO DE UNA SALA) (HAY QUE PONER CUANTOS)
    // DEVUELVE: -
    // EXCEPCIONES: (OBJETOTIENDA NO EXISTE) / (POU NO EXISTE)

    public void pouCompraArticulos(String pouId, String articuloId, Integer cantidad, String tipoArticulo) throws
            ObjetoTiendaNoExisteException, PouIDNoExisteException, PouNoTieneDineroSuficienteException;

    // OPERACIÓN 15: BORRAR ELEMENTO ARMARIO POU (PORQUE SE HA CONSUMIDO) (SE RESTA 1 (UNITARIAMENTE))
    // DEVUELVE: ObjetoTienda
    // EXCEPCIONES: (ARTICULO NO EXISTE EN EL ARMARIO) / (POU NO EXISTE)

    InformacionPou getInfoAndroidPou(Credenciales credentials) throws PouIDNoExisteException;

    void comprobarCorreo(String gmail) throws CorreoYaExisteException;

    List<Pou> obtenerPousOrdenadosDescendentemente(String columnaId);

    public ObjetoArmario pouConsumeArticulo(String pouId, String articuloId) throws
            PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException, ObjetoArmarioNoDisponible;

    // OPERACIÓN 16: POU MODIFICA SU CAMISETA (OUTFIT)
    // DEVUELVE: -
    // EXCEPCIONES: (ARTICULO NO EXISTE (EN EL ARMARIO DE POU)) / (POU NO EXISTE)

    public void pouCambiaCamiseta(String pouId, String camisetaId) throws
            ObjetoTiendaNoExisteException, PouIDNoExisteException;

    // OPERACIÓN 17: POU MODIFICA SUS ZAPATOS (OUTFIT)
    // DEVUELVE: -
    // EXCEPCIONES: (ARTICULO NO EXISTE (EN EL ARMARIO DE POU)) / (POU NO EXISTE)

    public void pouCambiaZapatos(String pouId, String camisetaId) throws
            ObjetoTiendaNoExisteException, PouIDNoExisteException;

    // OPERACIÓN 18: POU MODIFICA SU GORRA (OUTFIT)
    // DEVUELVE: -
    // EXCEPCIONES: (ARTICULO NO EXISTE (EN EL ARMARIO DE POU)) / (POU NO EXISTE)

    public void pouCambiaGorra(String pouId, String camisetaId) throws
            ObjetoTiendaNoExisteException, PouIDNoExisteException;

    // OPERACIÓN 19: POU MODIFICA SUS GAFAS (OUTFIT)
    // DEVUELVE: -
    // EXCEPCIONES: (ARTICULO NO EXISTE (EN EL ARMARIO DE POU)) / (POU NO EXISTE)

    public void pouCambiaGafas(String pouId, String camisetaId) throws
            ObjetoTiendaNoExisteException, PouIDNoExisteException;

    // OPERACIÓN 20: POU MODIFICA SU NIVEL DE HAMBRE
    // DEVUELVE: -
    // EXCEPCIONES: (POU NO EXISTE) / (SE BAJA DEL NIVEL MINIMO = 0) / (SE PASA DEL NIVEL MAXIMO = 100)

    public void pouModificaNivelHambre(String pouId, Integer varNivelHambre) throws
            PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException;

    // OPERACIÓN 21: POU MODIFICA SU NIVEL DE SALUD
    // DEVUELVE: -
    // EXCEPCIONES: (POU NO EXISTE) / (SE BAJA DEL NIVEL MINIMO = 0) / (SE PASA DEL NIVEL MAXIMO = 100)

    public void pouModificaNivelSalud(String pouId, Integer varNivelSalud) throws
            PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException;

    // OPERACIÓN 22: POU MODIFICA SU NIVEL DE DIVERSION
    // DEVUELVE: -
    // EXCEPCIONES: (POU NO EXISTE) / (SE BAJA DEL NIVEL MINIMO = 0) / (SE PASA DEL NIVEL MAXIMO = 100)

    public void pouModificaNivelDiversion(String pouId, Integer varNivelDiversion) throws
            PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException;

    // OPERACIÓN 23: POU MODIFICA SU NIVEL DE SUEÑO
    // DEVUELVE: -
    // EXCEPCIONES: (POU NO EXISTE) / (SE BAJA DEL NIVEL MINIMO = 0) / (SE PASA DEL NIVEL MAXIMO = 100)

    public void pouModificaNivelSueno(String pouId, Integer varNivelSueno) throws
            PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException;

    // OPERACIÓN 24: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS COMIDAS DEL ARMARIO
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoArmario> obtenerComidasArmario();

    // OPERACIÓN 25: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS BEBIDAS DEL ARMARIO
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoArmario> obtenerBebidasArmario();

    // OPERACIÓN 26: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS POCIONES DEL ARMARIO
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoArmario> obtenerPocionesArmario();

    // OPERACIÓN 27: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LA ROPA DEL ARMARIO
    // DEVUELVE: List<ObjetoTienda>
    // EXCEPCIONES: -

    public List<ObjetoArmario> obtenerRopaArmario();

    // OPERACIÓN 28: POU GASTA DINERO / GANA DINERO.
    // DEVUELVE: -
    // EXCEPCIONES: (POU NO EXISTE) / (POU NO TIENE DINERO SUFICIENTE)

    public void pouModificaDinero(String pouId, double varDinero) throws
            PouIDNoExisteException, PouNoTieneDineroSuficienteException;

    // OPERACIÓN 29: OBTENER EL NÚMERO DE ARTÍCULOS QUE HAY EN LA TIENDA.
    // DEVUELVE: -
    // EXCEPCIONES: -

    public Integer dameNumArticulosTienda();

    //OPERACIÓN 30: OBTENER UN LISTA DEL TIPO DE ARTÍCULO DE LA TIENDA QUE SE PIDE.
    //DEVUELVE: LISTA
    //EXCEPCIONES: -

    public List<ObjetoTienda> listaObjetosTiendaTipo(String tipoArticulo);

    //OPERACIÓN 31: OBTENER UN LISTA DEL TIPO DE ARTÍCULO DEL ARMARIO QUE SE PIDE.
    //DEVUELVE: LISTA
    //EXCEPCIONES: -

    public List<ObjetoArmario> listaObjetosArmarioTipo(String tipoArticulo);

    List<ObjetoTienda> listaObjetosTipo(String tipoArticulo);

    //OPERACIÓN 32: OBTENER UN POU A PARTIR DE UNOS CREDENCIALES
    Pou obtenerPouByCredentials(Credenciales credenciales);

    // OPERACIÓN 33: OBTENER OBJETOARMARIO POR EL ID DE UN POU ("pouId")
    // DEVUELVE: ObjetoArmario
    // EXCEPCIONES: (NO EXISTE ESTE POU)

    public ObjetoArmario obtenerObjetoArmario(String pouId) throws
            PouIDNoExisteException;

    // OPERACIÓN 34: AÑADIR OBJETO AL ARMARIO
    // DEVUELVE: -
    // EXCEPCIONES: -

    public void addObjetosAArmario(int idArmario, String pouId, String tipoArticulo, String idArticulo, Integer cantidad);

    // OPERACIÓN 35: Actualizar un objeto (ObjetoArmario, ObjetoTienda, Pou)

    public void updateObjeto(Object objeto);

    List<ObjetoArmario> obtenerObjetosArmarioPouTipo(String pouId, String tipoArticulo);

    void updateObjetoArmario(String pouId, String idArticulo, int cantidad);


    void updateAndroid(InformacionPou informacionPou);
}
