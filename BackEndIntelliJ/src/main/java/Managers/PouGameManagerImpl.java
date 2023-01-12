package Managers;

import Entities.Exceptions.*;
import Entities.ObjetoArmario;
import Entities.ObjetoTienda;
import Entities.Pou;
import Entities.ValueObjects.Credenciales;
import Entities.ValueObjects.InformacionPou;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;

import java.util.*;

public class PouGameManagerImpl implements PouGameManager {


    Map<String, Pou> pousGame; // Hashmap con todos los pous registrados. ---> KEY = "pouId" (String)

    Map<String, ObjetoTienda> objetosTienda; // Lista con todos los elementos de la tienda. ---> KEY = "articuloId" (String)

    Map<String, ObjetoArmario> objetosArmario; // Lista con todos los elementos del armario. ---> KEY = "articuloId" (String)

    private static PouGameManager instance;

    final static Logger logger = Logger.getLogger(PouGameManagerImpl.class);

    public static PouGameManager getInstance() {
        if (instance == null) instance = new PouGameManagerImpl();
        return instance;
    }

    public PouGameManagerImpl() {
        this.pousGame = new HashMap<>();
        this.objetosTienda = new HashMap<>();
        this.objetosArmario = new HashMap<>();
    }

    // OPERACIÓN 1: OBTENER NÚMERO DE POUS

    @Override
    public int size() {
        int ret = this.pousGame.size();
        logger.info("Número de Pous: " + ret);
        return ret;
    }

    // OPERACIÓN 2: REGISTRO POU (CREAR POU)

    @Override
    public void crearPou(String pouId, String nombrePou, String nacimientoPou, String correo, String password) throws CorreoYaExisteException, PouIDYaExisteException {
        logger.info("Se quiere registrar un Pou con ID " + pouId + ".");
        Pou nuevoPou = new Pou(pouId, nombrePou, nacimientoPou, correo, password);
        // Recorremos los Pous registrados para ver si hay alguno con este correo.
        boolean mailYaExiste = false;
        List<Pou> listaPous = new ArrayList<>(this.pousGame.values());
        for (int i = 0; i < listaPous.size(); i++) {
            if (Objects.equals(listaPous.get(i).getCorreoPou(), correo)) {
                mailYaExiste = true;
                break;
            }
        }
        if (this.pousGame.containsKey(pouId)) {
            logger.warn("El ID de Pou " + pouId + " ya existe.");
            throw new PouIDYaExisteException();
        } else if (mailYaExiste) {
            logger.warn("El correo " + correo + " ya está registrado en un Pou.");
            throw new CorreoYaExisteException();
        } else {
            this.pousGame.put(pouId, nuevoPou);
            logger.info("Pou creado con ID " + pouId + ".");
        }
    }

    // OPERACIÓN 3: LOGIN POU

    @Override
    public void loginPou(String correo, String password) throws CorreoNoExisteException, PasswordIncorrectaException {
        logger.info("Se quiere hacer login con el correo " + correo + " y la contraseña " + password + ".");
        boolean mailExiste = false;
        boolean contraCorrecta = false;
        String pouId = null;
        List<Pou> listaPous = new ArrayList<>(this.pousGame.values());
        for (int i = 0; i < listaPous.size(); i++) {
            if (Objects.equals(listaPous.get(i).getCorreoPou(), correo)) {
                mailExiste = true;
                if (Objects.equals(listaPous.get(i).getPasswordPou(), password)) {
                    contraCorrecta = true;
                    pouId = listaPous.get(i).getPouId();
                }
                break;
            }
        }
        if (!mailExiste) {
            logger.warn("No hay ningún Pou registrado con el correo " + correo + ".");
            throw new CorreoNoExisteException();
        } else if (!contraCorrecta) {
            logger.warn("La contraseña introducida, " + correo + ", no es la correcta para este correo.");
            throw new PasswordIncorrectaException();
        } else {
            logger.info("Se ha hecho correctamente el login en el Pou con ID " + pouId + ".");
        }
    }

    // OPERACIÓN 4: OBTENER TODOS LOS OBJETOS TIENDA

    @Override
    public List<ObjetoTienda> obtenerObjetosTienda() {
        List<ObjetoTienda> listaObjetosTienda = new ArrayList<>(this.objetosTienda.values());
        return listaObjetosTienda;
    }

    // OPERACIÓN 5: OBTENER TODOS LOS POUS

    @Override
    public Map<String, Pou> obtenerPous() {
        logger.info("Queremos obtener un Map con todos los pous registrados ");
        int num = this.pousGame.size();
        logger.info("Número de Pous: " + num);
        return this.pousGame;
    }

    @Override
    public Map<String, ObjetoArmario> obtenerObjetosArmarioPou(String pouId) {

        return null;
    }

    @Override
    public List<ObjetoArmario> obtenerObjetosArmarioPouTipo(String pouId, String tipoArticulo) {
        return null;
    }

    // OPERACIÓN 6: OBTENER POU POR SU ID ("pouId")

    @Override
    public Pou obtenerPou(String pouId) throws PouIDNoExisteException {
        logger.info("Se quiere obtener el Pou que se identifica con el id " + pouId + ".");
        Pou pouEncontrado = new Pou();
        if (this.pousGame.containsKey(pouId)) {
            logger.info("El Pou si que existe. Vamos a realizar una búsqueda para encontrarlo.");
            if (this.pousGame.containsKey(pouId)){
                logger.info("El Pou sí que existe. Vamos a realizar una búsqueda para encontrarlo.");
                List<Pou> listaPous = new ArrayList<>(this.pousGame.values());
                for (int i = 0; i < listaPous.size(); i++) {
                    if (Objects.equals(listaPous.get(i).getPouId(), pouId)) {
                        pouEncontrado = listaPous.get(i);
                        logger.info("Se ha encontrado el Pou con id " + pouId + ".");
                    }
                }
            }
        }
        else {
            logger.info("El Pou con id " + pouId + " no existe.");
            throw new PouIDNoExisteException();
        }
        return pouEncontrado;
    }

    // OPERACIÓN 7: AÑADIR OBJETOS A LA TIENDA

    @Override
    public void addObjetosATienda(String articuloId, String nombreArticulo, Integer precioArticulo, String tipoArticulo, Integer recargaHambre, Integer recargaSalud, Integer recargaDiversion, Integer recargaSueno) throws ObjetoTiendaYaExisteException {
        logger.info("Se quiere añadir un ObjetoTienda con ID " + articuloId + ".");
        ObjetoTienda nuevoObjetoTienda = new ObjetoTienda(articuloId, nombreArticulo, precioArticulo, tipoArticulo, recargaHambre, recargaSalud, recargaDiversion, recargaSueno);
        if (this.objetosTienda.containsKey(articuloId)) {
            logger.warn("La ID del articulo " + articuloId + " ya existe.");
            throw new ObjetoTiendaYaExisteException();
        } else {
            this.objetosTienda.put(articuloId, nuevoObjetoTienda);
            logger.info("Pou creado con ID " + articuloId + ".");
        }
    }

    // OPERACIÓN 8: OBTENER OBJETO DE LA TIENDA POR SU ID ("articuloId")

    @Override

    public ObjetoTienda obtenerObjetoTienda(String articuloId) throws ObjetoTiendaNoExisteException {
        logger.info("Se quiere obtener el objeto de la tienda que se identifica con el id "+articuloId+".");
        ObjetoTienda objetoTiendaEncontrado = new ObjetoTienda();
        if (this.objetosTienda.containsKey(articuloId)){
            logger.info("El objeto con id "+articuloId+" sí que existe. Vamos a realizar una búsqueda para encontrarlo.");
            List<ObjetoTienda> listaObjetosTienda = new ArrayList<>(this.objetosTienda.values());
            for (ObjetoTienda objetoTienda : listaObjetosTienda) {
                if (Objects.equals(objetoTienda.getArticuloId(), articuloId)) {
                    objetoTiendaEncontrado = objetoTienda;
                    logger.info("Se ha encontrado el objeto con id " + articuloId + ".");
                }
            }
        }
        else{
            logger.info("El objeto con id "+articuloId+" no existe.");
            throw new ObjetoTiendaNoExisteException();
        }
        return objetoTiendaEncontrado;
    }

    // OPERACIÓN 9: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS COMIDAS DE LA TIENDA

    @Override
    public List<ObjetoTienda> obtenerComidasTienda() {
        logger.info("Se quiere obtener las comidas de la tienda ordenadas por precio creciente");
        List<ObjetoTienda> listaComidas = this.listaObjetosTiendaTipo("Comida");
        int num = listaComidas.size();
        logger.info("Hay " + num + " comidas en la tienda");
        return listaComidas;
    }

    // OPERACIÓN 10: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS BEBIDAS DE LA TIENDA

    @Override
    public List<ObjetoTienda> obtenerBebidasTienda() {
        logger.info("Se quiere obtener las bebidas de la tienda ordenadas por precio creciente");
        List<ObjetoTienda> listaBebidas = this.listaObjetosTiendaTipo("Bebida");
        int num = listaBebidas.size();
        logger.info("Hay " + num + " bebidas en la tienda");
        return listaBebidas;
    }

    // OPERACIÓN 11: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS POCIONES DE LA TIENDA

    @Override
    public List<ObjetoTienda> obtenerPocionesTienda() {
        logger.info("Se quiere obtener las pociones de la tienda ordenadas por precio creciente");
        List<ObjetoTienda> listaPociones = this.listaObjetosTiendaTipo("Pocion");
        int num = listaPociones.size();
        logger.info("Hay " + num + " pociones en la tienda");
        return listaPociones;
    }

    // OPERACIÓN 12: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS ROPAS DE LA TIENDA

    @Override
    public List<ObjetoTienda> obtenerRopasTienda() {
        logger.info("Se quiere obtener las prendas de ropa de la tienda ordenadas por precio creciente");
        List<ObjetoTienda> listaRopa = this.listaObjetosTiendaTipo("Ropa");
        int num = listaRopa.size();
        logger.info("Hay " + num + " prendas de ropa en la tienda");
        return listaRopa;
    }

    // OPERACIÓN 14: AÑADIR ELEMENTO ARMARIO POU (POU COMPRA UN OBJETO DE UNA SALA) (HAY QUE PONER CUANTOS)
    @Override
    public void pouCompraArticulos(String pouId, String articuloId, Integer cantidad, String tipoArticulo) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {
        logger.info("El Pou con ID "+pouId+" quiere comprar "+cantidad+" objetos con ID "+articuloId+" que son del tipo "+tipoArticulo+".");
        if(this.objetosTienda.containsKey(articuloId)){
            logger.info("El objeto existe.");
            if (this.pousGame.containsKey(pouId)){
                logger.info("El Pou existe.");
                List<ObjetoTienda> listaObjetosTienda = new ArrayList<>(this.objetosTienda.values());
                logger.info("El armario "+ listaObjetosTienda.size() + ".");
                for (int i = 0; i<listaObjetosTienda.size(); i++){
                    if(Objects.equals(listaObjetosTienda.get(i).getArticuloId(), articuloId)){
                        logger.info("El objeto con ID "+articuloId+" ha sido encontrado.");
                        ObjetoTienda objetoTienda = listaObjetosTienda.get(i);
                        List<ObjetoArmario> listaObjetosArmario = new ArrayList<>(this.objetosArmario.values());
                        logger.info("El armario "+ listaObjetosArmario.size() + ".");
                        for (int n = 0; n<listaObjetosArmario.size(); n++){
                            logger.info("El armario" );
                            if(Objects.equals(listaObjetosArmario.get(i).getPouId(), pouId)) {
                                logger.info("El armario correspondiente al Pou con identificador " + pouId + " ha sido encontrado.");
                                ObjetoArmario objetoArmario = listaObjetosArmario.get(i);
                                if (Objects.equals(objetoTienda.getTipoArticulo(), tipoArticulo)) {
                                    if (Objects.equals(tipoArticulo, "Comida")) {
                                        logger.info("El objeto es de tipo Comida.");
                                        objetoArmario.setCantidad(objetoArmario.getCantidad()+cantidad);
                                    }
                                    if (Objects.equals(tipoArticulo, "Bebida")) {
                                        logger.info("El objeto es de tipo Bebida.");
                                        objetoArmario.setCantidad(objetoArmario.getCantidad()+cantidad);
                                    }
                                    if (Objects.equals(tipoArticulo, "Pocion")) {
                                        logger.info("El objeto es de tipo Pocion.");
                                        objetoArmario.setCantidad(objetoArmario.getCantidad()+cantidad);
                                    }
                                    if (Objects.equals(tipoArticulo, "Ropa")) {
                                        logger.info("El objeto es de tipo Ropa.");
                                        objetoArmario.setCantidad(objetoArmario.getCantidad()+cantidad);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                throw new PouIDNoExisteException();
            }

        }
        else{
            throw new ObjetoTiendaNoExisteException();
        }
    }

    // OPERACIÓN 15: BORRAR ELEMENTO ARMARIO POU (PORQUE SE HA CONSUMIDO) (SE RESTA 1 (UNITARIAMENTE))
    // Podríem fer aquí que aquí es cridi les funcions de modificar nivel i així és com que et prens la poció o lo que sigui

    @Override
    public ObjetoArmario pouConsumeArticulo(String pouId, String articuloId) throws ObjetoArmarioNoDisponible, PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {
        logger.info("Se quiere que el Pou con id " + pouId + " consuma un artículo de su armario con id "+ articuloId + ".");
        Pou miPou = this.obtenerPou(pouId);
        ObjetoArmario objetoEncontrado = new ObjetoArmario();
        ObjetoTienda objetoTienda = new ObjetoTienda();
        List<ObjetoArmario> listaArmario = new ArrayList<>(this.objetosArmario.values());
        for (int i = 0; i < listaArmario.size(); i++) {
            if (Objects.equals(listaArmario.get(i).getIdArticulo(), articuloId)) {
                objetoEncontrado = listaArmario.get(i);
                logger.info("Se ha encontrado el artículo con id " + articuloId + ".");
                Integer cantidad = objetoEncontrado.getCantidad() - 1;
                objetoEncontrado.setCantidad(cantidad);
                if (Objects.equals(objetoEncontrado.getTipoArticulo(), "Comida")) {
                    logger.info("El artículo es una comida");
                    int varNivel = objetoTienda.getRecargaHambre();
                    this.pouModificaNivelHambre(pouId, varNivel);
                } else if (Objects.equals(objetoEncontrado.getTipoArticulo(), "Bebida")) {
                    logger.info("El artículo es una bebida");
                    int varNivel = objetoTienda.getRecargaHambre();
                    this.pouModificaNivelHambre(pouId, varNivel);
                } else if (Objects.equals(objetoEncontrado.getTipoArticulo(), "Pocion")) {
                    logger.info("El artículo es una poción");
                    int varNivel = objetoTienda.getRecargaSalud();
                    this.pouModificaNivelSalud(pouId, varNivel);
                }
            } else {
                throw new ObjetoArmarioNoDisponible();
            }
        }
        return objetoEncontrado;
    }

    // OPERACIÓN 16: POU MODIFICA SU CAMISETA (OUTFIT)

    @Override
    public void pouCambiaCamiseta(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException{

    }

    // OPERACIÓN 17: POU MODIFICA SUS ZAPATOS (OUTFIT)

    @Override
    public void pouCambiaZapatos(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    // OPERACIÓN 18: POU MODIFICA SU GORRA (OUTFIT)

    @Override
    public void pouCambiaGorra(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    // OPERACIÓN 19: POU MODIFICA SUS GAFAS (OUTFIT)

    @Override
    public void pouCambiaGafas(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    // OPERACIÓN 20: POU MODIFICA SU NIVEL DE HAMBRE

    @Override
    public void pouModificaNivelHambre(String pouId, Integer varNivelHambre) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {
        logger.info("Se quiere que el Pou con id "+pouId+" modifique su nivel de hambre en " +varNivelHambre+" puntos.");
        Pou miPou = this.obtenerPou(pouId);
        miPou.setNivelHambrePou(miPou.getNivelHambrePou()+varNivelHambre);
        if(miPou.getNivelHambrePou()>100){
            miPou.setNivelHambrePou(100);
            throw new NivelPorEncimaDelMaximoException();
        }
        if(miPou.getNivelHambrePou()<0){
            miPou.setNivelHambrePou(0);
            throw new NivelPorDebajoDelMinimoException();
        }

        logger.info("El pou con id "+pouId+" tiene "+miPou.getNivelHambrePou()+" nivel de hambre.");
    }

    // OPERACIÓN 21: POU MODIFICA SU NIVEL DE SALUD

    @Override
    public void pouModificaNivelSalud(String pouId, Integer varNivelSalud) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {
        logger.info("Se quiere que el Pou con id "+pouId+" modifique su nivel de hambre en " +varNivelSalud+" puntos.");
        Pou miPou = this.obtenerPou(pouId);
        miPou.setNivelSaludPou(miPou.getNivelSaludPou()+varNivelSalud);
        if(miPou.getNivelSaludPou()>100){
            miPou.setNivelSaludPou(100);
            throw new NivelPorEncimaDelMaximoException();
        }
        if(miPou.getNivelSaludPou()<0){
            miPou.setNivelSaludPou(0);
            throw new NivelPorDebajoDelMinimoException();
        }
        logger.info("El pou con id "+pouId+" tiene "+miPou.getNivelSaludPou()+" nivel de salud.");
    }


    // OPERACIÓN 22: POU MODIFICA SU NIVEL DE DIVERSION

    @Override
    public void pouModificaNivelDiversion(String pouId, Integer varNivelDiversion) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {
        logger.info("Se quiere que el Pou con id "+pouId+" modifique su nivel de hambre en " +varNivelDiversion+" puntos.");
        Pou miPou = this.obtenerPou(pouId);
        miPou.setNivelDiversionPou(miPou.getNivelDiversionPou()+varNivelDiversion);
        if(miPou.getNivelDiversionPou()>100){
            miPou.setNivelDiversionPou(100);
            throw new NivelPorEncimaDelMaximoException();
        }
        if(miPou.getNivelDiversionPou()<0){
            miPou.setNivelDiversionPou(0);
            throw new NivelPorDebajoDelMinimoException();
        }
        logger.info("El pou con id "+pouId+" tiene "+miPou.getNivelDiversionPou()+" nivel de diversión.");
    }

    // OPERACIÓN 23: POU MODIFICA SU NIVEL DE SUEÑO

    @Override
    public void pouModificaNivelSueno(String pouId, Integer varNivelSueno) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {
        logger.info("Se quiere que el Pou con id "+pouId+" modifique su nivel de hambre en " +varNivelSueno+" puntos.");
        Pou miPou = this.obtenerPou(pouId);
        miPou.setNivelSuenoPou(miPou.getNivelSuenoPou()+varNivelSueno);
        if(miPou.getNivelSuenoPou()>100){
            miPou.setNivelSuenoPou(100);
            throw new NivelPorEncimaDelMaximoException();
        }
        if(miPou.getNivelSuenoPou()<0){
            miPou.setNivelSuenoPou(0);
            throw new NivelPorDebajoDelMinimoException();
        }
        logger.info("El pou con id "+pouId+" tiene "+varNivelSueno+" nivel de sueño.");
    }

    // OPERACIÓN 24: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS COMIDAS DEL ARMARIO

    @Override
    public List<ObjetoArmario> obtenerComidasArmario() {
        logger.info("Se quiere obtener las comidas del armario.");
        List<ObjetoArmario> listaComidas = this.listaObjetosArmarioTipo("Comida");
        int num = listaComidas.size();
        logger.info("Hay " + num + " comidas en el armario");
        return listaComidas;
    }

    // OPERACIÓN 25: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS BEBIDAS DEL ARMARIO

    @Override
    public List<ObjetoArmario> obtenerBebidasArmario() {
        logger.info("Se quiere obtener las bebidas del armario.");
        List<ObjetoArmario> listaBebidas = this.listaObjetosArmarioTipo("Bebida");
        int num = listaBebidas.size();
        logger.info("Hay " + num + " bebidas en el armario");
        return listaBebidas;
    }

    // OPERACIÓN 26: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LAS POCIONES DEL ARMARIO

    @Override
    public List<ObjetoArmario> obtenerPocionesArmario() {
        logger.info("Se quiere obtener las pociones del armario.");
        List<ObjetoArmario> listaPociones = this.listaObjetosArmarioTipo("Pocion");
        int num = listaPociones.size();
        logger.info("Hay " + num + " pociones en el armario");
        return listaPociones;
    }

    // OPERACIÓN 27: OBTENER, POR ORDEN DE PRECIO CRECIENTE (DE - A +), LA ROPA DEL ARMARIO

    @Override
    public List<ObjetoArmario> obtenerRopaArmario() {
        logger.info("Se quiere obtener la ropa del armario.");
        List<ObjetoArmario> listaRopa = this.listaObjetosArmarioTipo("Ropa");
        int num = listaRopa.size();
        logger.info("Hay " + num + " prendas de ropa en el armario");
        return listaRopa;
    }

    // OPERACIÓN 28: POU GASTA DINERO / GANA DINERO.

    @Override
    public void pouModificaDinero(String pouId, double varDinero) throws PouIDNoExisteException, PouNoTieneDineroSuficienteException {

    }

    // OPERACIÓN 29: OBTENER EL NÚMERO DE ARTÍCULOS QUE HAY EN LA TIENDA.

    public Integer dameNumArticulosTienda() {
        return this.objetosTienda.size();
    }

    //OPERACIÓN 30: OBTENER UN LISTA DEL TIPO DE ARTÍCULO DE LA TIENDA QUE SE PIDE.
    @Override
    public List<ObjetoTienda> listaObjetosTiendaTipo(String tipoArticulo) {
        logger.info("Se quiere obtener una lista de objetos de tipo " + tipoArticulo + ".");
        List<ObjetoTienda> listaObjetosTienda = new ArrayList<>(this.objetosTienda.values());
        List<ObjetoTienda> listaTipo = new ArrayList<>();
        for (int i = 0; i < this.objetosTienda.size(); i++) {
            if (Objects.equals(listaObjetosTienda.get(i).getTipoArticulo(), tipoArticulo)) {
                listaTipo.add(listaObjetosTienda.get(i));
            }
        }
        listaTipo.sort(Comparator.comparingDouble(ObjetoTienda::getPrecioArticulo));
        return listaTipo;
    }

    //OPERACIÓN 31: OBTENER UN LISTA DEL TIPO DE ARTÍCULO DEL ARMARIO QUE SE PIDE.
    @Override
    public List<ObjetoArmario> listaObjetosArmarioTipo(String tipoArticulo) {
        logger.info("Se quiere obtener una lista de objetos de tipo " + tipoArticulo + ".");
        List<ObjetoArmario> listaObjetosArmario = new ArrayList<>(this.objetosArmario.values());
        List<ObjetoArmario> listaTipo = new ArrayList<>();
        for (int i = 0; i < this.objetosArmario.size(); i++) {
            if (Objects.equals(listaObjetosArmario.get(i).getTipoArticulo(), tipoArticulo)) {
                listaTipo.add(listaObjetosArmario.get(i));
            }
        }
        return listaTipo;
    }

    @Override
    public List<ObjetoTienda> listaObjetosTipo(String tipoArticulo) {
        return null;
    }

    //OPERACIÓN 32: OBTENER UN POU A PARTIR DE UNOS CREDENCIALES
    @Override
    public Pou obtenerPouByCredentials(Credenciales credenciales) {
        logger.info("Se quiere obtener el pou que tenga el correo" + credenciales.getCorreoPou() + " y la contraseña " + credenciales.getPasswordPou() + ".");
        List<Pou> listaPous = new ArrayList<>(this.pousGame.values());
        String pouId = "";
        Pou miPou = new Pou();
        for (Pou pous : listaPous) {
            if (Objects.equals(pous.getCorreoPou(), credenciales.getCorreoPou())) {
                if (Objects.equals(pous.getPasswordPou(), credenciales.getPasswordPou())) {
                    pouId = pous.getPouId();
                    miPou = pous;
                }
                break;
            }
        }
        logger.info("La ID del Pou encontrado es " + pouId + ".");
        return miPou;
    }

    // OPERACIÓN 33: OBTENER OBJETOARMARIO POR EL ID DE UN POU ("pouId")
    public ObjetoArmario obtenerObjetoArmario(String pouId) throws PouIDNoExisteException{
        logger.info("Se quiere obtener el Armario del Pou que se identifica con el id " + pouId + ".");
        ObjetoArmario objetoArmarioEncontrado = new ObjetoArmario();
        if (this.objetosArmario.containsKey(pouId)) {
            logger.info("El ObjetoArmario si que existe. Vamos a realizar una búsqueda para encontrarlo.");
            if (this.objetosArmario.containsKey(pouId)){
                List<ObjetoArmario> listaObjetosArmario = new ArrayList<>(this.objetosArmario.values());
                for (int i = 0; i < listaObjetosArmario.size(); i++) {
                    if (Objects.equals(listaObjetosArmario.get(i).getPouId(), pouId)) {
                        objetoArmarioEncontrado = listaObjetosArmario.get(i);
                        logger.info("Se ha encontrado el objeto del Armario del Pou con id " + pouId + ".");
                    }
                }
            }
        }
        else {
            logger.info("El Pou con id " + pouId + " no existe.");
            throw new PouIDNoExisteException();
        }
        return objetoArmarioEncontrado;
    }

    // OPERACIÓN 34: AÑADIR OBJETO AL ARMARIO
    @Override
    public void addObjetosAArmario(int idArmario, String pouId, String tipoArticulo, String idArticulo, Integer cantidad) {
        logger.info("Se quiere añadir un ObjetoArmario con ID " + idArticulo + ".");
        ObjetoArmario nuevoObjetoArmario = new ObjetoArmario(idArmario, pouId, tipoArticulo, idArticulo, cantidad);
        if (this.objetosArmario.containsKey(idArticulo)) {
            logger.warn("La ID del articulo " + idArticulo + " ya existe, se añaden "+cantidad+" unidad.");
            nuevoObjetoArmario.setCantidad(nuevoObjetoArmario.getCantidad()+cantidad);
        } else {
            this.objetosArmario.put(idArticulo, nuevoObjetoArmario);
            logger.info("Artículo con ID " + idArticulo + " añadido.");
        }
    }

    @Override
    public void updateObjeto(Object objeto) {

    }

    @Override
    public void updateObjetoArmario(String pouId, String idArticulo, int cantidad){

    }

    @Override
    public InformacionPou getInfoAndroidPou(Credenciales credentials) throws PouIDNoExisteException {
        Pou miPou = obtenerPouByCredentials(credentials);

        List<ObjetoArmario> miArmarioListComida  = obtenerObjetosArmarioPouTipo(miPou.getPouId(), "Comida");
        int amountCandy = 0;
        int amountManzana = 0;
        int amountPizza = 0;
        for(int i = 0 ; i<miArmarioListComida.size() ; i++){

            ObjetoArmario aux = miArmarioListComida.get(i);
            if("manzana".equals(aux.getIdArticulo())){
                amountManzana = aux.getCantidad();
            } else if ("candy".equals(aux.getIdArticulo())) {
                amountCandy = aux.getCantidad();
            }
            else{
                amountPizza = aux.getCantidad();
            }
        }

        List<ObjetoArmario> miArmarioListBebida  = obtenerObjetosArmarioPouTipo(miPou.getPouId(), "Bebida");
        int amountAgua = 0;
        int amountAquarius = 0;
        int amountRoncola = 0;
        for(int i = 0 ; i<miArmarioListBebida.size() ; i++){

            ObjetoArmario aux = miArmarioListBebida.get(i);
            if("agua".equals(aux.getIdArticulo())){
                amountAgua = aux.getCantidad();
            } else if ("aquarius".equals(aux.getIdArticulo())) {
                amountAquarius = aux.getCantidad();
            }
            else{
                amountRoncola = aux.getCantidad();
            }
        }

        List<ObjetoArmario> miArmarioListPocion  = obtenerObjetosArmarioPouTipo(miPou.getPouId(), "Poción");
        int amountHambre = 0;
        int amountSalud = 0;
        int amountDiversion = 0;
        int amountSueno = 0;
        for(int i = 0 ; i<miArmarioListPocion.size() ; i++){

            ObjetoArmario aux = miArmarioListPocion.get(i);
            if("hambre".equals(aux.getIdArticulo())){
                amountHambre= aux.getCantidad();
            } else if ("salud".equals(aux.getIdArticulo())) {
                amountSalud = aux.getCantidad();
            } else if ("diversion".equals(aux.getIdArticulo())) {
                amountDiversion = aux.getCantidad();
            } else{
                amountSueno = aux.getCantidad();
            }
        }

        List<ObjetoArmario> miArmarioListRopa  = obtenerObjetosArmarioPouTipo(miPou.getPouId(), "Ropa");
        String posee_pijama = "NO";
        String posee_fcb = "NO";
        String posee_spain = "NO";
        String posee_messi = "NO";
        String posee_rafa = "NO";
        String posee_veja = "NO";
        String posee_fiesta = "NO";
        String posee_rayban = "NO";
        String posee_ciclismo = "NO";
        String posee_cerveza = "NO";
        String posee_boina = "NO";
        String posee_polo = "NO";
        for(int i = 0 ; i<miArmarioListRopa.size() ; i++){

            ObjetoArmario aux = miArmarioListRopa.get(i);
            if("pijama".equals(aux.getIdArticulo())){
                posee_pijama = "YES";
            } else if ("fcb".equals(aux.getIdArticulo())) {
                posee_fcb = "YES";
            } else if ("spain".equals(aux.getIdArticulo())) {
                posee_spain = "YES";
            } else if ("messi".equals(aux.getIdArticulo())) {
                posee_messi = "YES";
            } else if ("rafa".equals(aux.getIdArticulo())) {
                posee_rafa = "YES";
            } else if ("veja".equals(aux.getIdArticulo())) {
                posee_veja = "YES";
            } else if ("fiesta".equals(aux.getIdArticulo())) {
                posee_fiesta = "YES";
            } else if ("rayban".equals(aux.getIdArticulo())) {
                posee_rayban = "YES";
            } else if ("ciclismo".equals(aux.getIdArticulo())) {
                posee_ciclismo = "YES";
            } else if ("cerveza".equals(aux.getIdArticulo())) {
                posee_cerveza = "YES";
            } else if ("boina".equals(aux.getIdArticulo())) {
                posee_boina = "YES";
            } else{
                posee_polo = "YES";
            }
        }

        InformacionPou completo = new InformacionPou(miPou.getPouId(), miPou.getNombrePou(), miPou.getNacimientoPou(), miPou.getCorreoPou(), miPou.getPasswordPou(), miPou.getRecord(), miPou.getNivelHambrePou(), miPou.getNivelSaludPou(), miPou.getNivelDiversionPou(), miPou.getNivelSuenoPou(), miPou.getDineroPou(), amountCandy, amountManzana, amountPizza, amountAgua, amountAquarius, amountRoncola, amountHambre, amountSalud, amountDiversion, amountSueno, miPou.getCamisetaId(), miPou.getZapatosId(), miPou.getGafasId(), miPou.getGorraId(), posee_pijama, posee_fcb, posee_spain, posee_messi, posee_rafa, posee_veja, posee_fiesta, posee_rayban, posee_ciclismo, posee_cerveza, posee_boina, posee_polo);
        return completo;
    }

    @Override
    public void updateAndroid(InformacionPou informacionPou){}

    @Override
    public void comprobarCorreo(String gmail) throws CorreoYaExisteException {}

    @Override
    public ObjetoTienda obtenerInfoObjeto(String articuloId){
        return null;
    }

    @Override
    public List<Pou> obtenerPousOrdenadosDescendentemente (String columnaId){
        return null;
    }
}
