package Managers;

import Entities.Exceptions.*;
import Entities.ObjetoArmario;
import Entities.ObjetoTienda;
import Entities.Pou;
import Entities.ValueObjects.Credenciales;
import Entities.ValueObjects.InformacionPou;
import edu.upc.eetac.dsa.FactorySession;
import edu.upc.eetac.dsa.Session;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class PouGameDBManagerImpl implements PouGameManager {

    Session session;

    Map<String, Pou> pousGame; // Hashmap con todos los pous registrados. ---> KEY = "pouId" (String)
    List<ObjetoTienda> objetosTienda; // Lista con todos los elementos de la tienda. ---> KEY = "articuloId" (Integer)

    Map<String, ObjetoArmario> objetosArmario; // Lista con todos los elementos del armario. ---> KEY = "articuloId" (Integer)

    private static PouGameManager instance;
    final static org.apache.log4j.Logger logger = Logger.getLogger(PouGameManagerImpl.class);

    public static PouGameManager getInstance(){
        if (instance==null) instance = new PouGameDBManagerImpl();
        return instance;
    }

    public PouGameDBManagerImpl(){
        this.session = FactorySession.openSession("jdbc:mariadb://localhost:3306/crud","eloim", "YES");
        this.pousGame = new HashMap<>();
        this.objetosTienda = new ArrayList<>();
        this.objetosArmario = new HashMap<>();
        this.pousGame = this.obtenerPous();
        this.objetosTienda = this.obtenerObjetosTienda();
    }

    public int size() {
        int ret = this.pousGame.size();
        return ret;
    }

    @Override
    public void crearPou(String pouId, String nombrePou, String nacimientoPou, String correo, String password) throws CorreoYaExisteException, PouIDYaExisteException {
        Pou miPou = new Pou(pouId,nombrePou,nacimientoPou,correo, password);
        List<Pou> listaPous= this.session.findAll(Pou.class);

        for(Pou p:listaPous){
            if(Objects.equals(p.getCorreoPou(),correo)){
                throw new CorreoYaExisteException();
            }
        }
        this.session.save(miPou);
        this.pousGame.put(pouId,miPou);
        logger.info("El usuario pou "+ pouId + " se ha añadido correctamente");
    }

    @Override
    public void loginPou(String correo, String password) throws CorreoNoExisteException, PasswordIncorrectaException {
        List<Object> listaPous = this.session.findAll(Pou.class);
        boolean mailExiste = false;
        boolean contraCorrecta = false;
        String pouId = null;
        for(Object pou : listaPous){
            Pou p = (Pou) pou;
            if(Objects.equals(p.getCorreoPou(), correo)) {
                mailExiste = true;
                if (Objects.equals(p.getPasswordPou(), password)) {
                    contraCorrecta = true;
                    pouId = p.getPouId();
                }
            }
        }
        if (!mailExiste) {
            logger.warn("No hay ningún Pou registrado con el correo " + correo + ".");
            throw new CorreoNoExisteException();
        } else if (!contraCorrecta) {
            logger.warn("La contraseña introducida, " + password + ", no es la correcta para este correo.");
            throw new PasswordIncorrectaException();
        } else {
            logger.info("Se ha hecho correctamente el login en el Pou con ID " + pouId + ".");
        }
    }

    @Override
    public List<ObjetoTienda> obtenerObjetosTienda() {
        List<ObjetoTienda> list = this.session.findAll(ObjetoTienda.class);
        return list;
    }

    @Override
    public ObjetoTienda obtenerInfoObjeto(String articuloId){
        ObjetoTienda objetoEncontrado = null;
        List<ObjetoTienda> todosLosObjetos = obtenerObjetosTienda();
        int i=0;
        boolean encontrado = false;
        while ((i<todosLosObjetos.size())&&(!encontrado)){
            if (Objects.equals(todosLosObjetos.get(i).getArticuloId(), articuloId)){
                objetoEncontrado = todosLosObjetos.get(i);
                encontrado = true;
            }
            i++;
        }
        return objetoEncontrado;
    }

    @Override
    public Map<String, Pou> obtenerPous() {
        List<Object> listaPous= this.session.findAll(Pou.class);
        for(int i=0; i<listaPous.size();i++) {
            Pou pou = (Pou) listaPous.get(i);
            this.pousGame.put(pou.getPouId(), pou);
        }
        return pousGame;
    }


    @Override
    public Map<String, ObjetoArmario> obtenerObjetosArmarioPou(String pouId) {
        Map<String, ObjetoArmario> armarioPou = new HashMap<>();
        List<ObjetoArmario> armarioPouLista = (List<ObjetoArmario>) this.session.getElementos(ObjetoArmario.class, "pouId",pouId);
        for(int i=0; i<armarioPouLista.size();i++) {
            armarioPou.put(armarioPouLista.get(i).getIdArticulo(),armarioPouLista.get(i));
        }
        return armarioPou;
    }

    @Override
    public List<ObjetoArmario> obtenerObjetosArmarioPouTipo (String pouId, String tipoArticulo){
        Map<String, ObjetoArmario> armarioPou = this.obtenerObjetosArmarioPou(pouId);
        List<ObjetoArmario> armarioTipo = new ArrayList<>();
        List<ObjetoArmario> armarioPouLista = new ArrayList<>(armarioPou.values());
        for(int i=0;i<armarioPouLista.size();i++){
            if(Objects.equals(armarioPouLista.get(i).getTipoArticulo(), tipoArticulo)){
                armarioTipo.add(armarioPouLista.get(i));
            }
        }
        return armarioTipo;
    }

    @Override
    public Pou obtenerPou(String pouId) throws PouIDNoExisteException {
        Pou p = (Pou) this.session.get(Pou.class, pouId);
        return p;
    }

    @Override
    public void addObjetosATienda(String articuloId, String nombreArticulo, Integer precioArticulo, String tipoArticulo, Integer recargaHambre, Integer recargaSalud, Integer recargaDiversion, Integer recargaSueno) throws ObjetoTiendaYaExisteException {
        ObjetoTienda o = new ObjetoTienda(articuloId,nombreArticulo,precioArticulo,tipoArticulo,recargaHambre,recargaSalud,recargaDiversion,recargaSueno);
        this.objetosTienda.add(o);
        this.session.save(o);

    }

    @Override
    public ObjetoTienda obtenerObjetoTienda(String articuloId) throws ObjetoTiendaNoExisteException {
        ObjetoTienda o = (ObjetoTienda) this.session.get(ObjetoTienda.class,articuloId);
        return o;
    }

    @Override
    public List<ObjetoTienda> obtenerComidasTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerBebidasTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerPocionesTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerRopasTienda() {
        return null;
    }

    @Override
    public void pouCompraArticulos(String pouId, String articuloId, Integer cantidad, String tipoArticulo) throws ObjetoTiendaNoExisteException, PouIDNoExisteException, PouNoTieneDineroSuficienteException {
        List<ObjetoTienda> listaTienda = (List<ObjetoTienda>) this.session.getElementos(ObjetoTienda.class, "articuloId", articuloId);
        ObjetoTienda objetoTienda = listaTienda.get(0);
        Pou pou = (Pou) this.session.get(Pou.class,pouId);
        String tipoProducto = objetoTienda.getTipoArticulo();
        int precio = (int) objetoTienda.getPrecioArticulo();
        int descuento = precio * cantidad;
        if(pou.getDineroPou()-descuento < 0){
            throw new PouNoTieneDineroSuficienteException();
        }
        pou.setDineroPou(pou.getDineroPou()-descuento);
        Map<String, ObjetoArmario> miArmario = this.obtenerObjetosArmarioPou(pouId);
        List<ObjetoArmario> listaArmario = new ArrayList<>(miArmario.values());
        this.session.update(pou);
        for(int i=0;i< listaArmario.size();i++){
            if(Objects.equals(listaArmario.get(i).getIdArticulo(), articuloId)){
                int idArmario = listaArmario.get(i).getIdArmario();
                int nuevaCantidad = listaArmario.get(i).getCantidad()+cantidad;
                ObjetoArmario a = new ObjetoArmario(idArmario,pouId,tipoProducto,articuloId, nuevaCantidad);
                logger.info("El objeto es "+articuloId+ " y tendrá " +nuevaCantidad + ".");
                this.session.update(a);
                return;
            }
        }
        List<Object> lista = this.session.findAll(ObjetoArmario.class);
        int idArmario = lista.size();
        ObjetoArmario b = new ObjetoArmario(idArmario,pouId,tipoProducto,articuloId,cantidad);
        this.session.save(b);
        logger.info("El objeto añadido es: " + articuloId);
    }

    @Override
    public InformacionPou getInfoAndroidPou(Credenciales credentials) throws PouIDNoExisteException {

        logger.info("Obteniendo la info POU");

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
        logger.info("Info obtenida");
        InformacionPou completo = new InformacionPou(miPou.getPouId(), miPou.getNombrePou(), miPou.getNacimientoPou(), miPou.getCorreoPou(), miPou.getPasswordPou(), miPou.getRecord(), miPou.getNivelHambrePou(), miPou.getNivelSaludPou(), miPou.getNivelDiversionPou(), miPou.getNivelSuenoPou(), miPou.getDineroPou(), amountCandy, amountManzana, amountPizza, amountAgua, amountAquarius, amountRoncola, amountHambre, amountSalud, amountDiversion, amountSueno, miPou.getCamisetaId(), miPou.getZapatosId(), miPou.getGafasId(), miPou.getGorraId(), posee_pijama, posee_fcb, posee_spain, posee_messi, posee_rafa, posee_veja, posee_fiesta, posee_rayban, posee_ciclismo, posee_cerveza, posee_boina, posee_polo);
        return completo;
    }

    @Override
    public void comprobarCorreo(String gmail) throws CorreoYaExisteException{
        Map<String, Pou> hashmapPous = obtenerPous();
        List<Pou> listaPous = new ArrayList<>(hashmapPous.values());

        logger.info("Gmail recibido: "+gmail);

        int i=0;
        while (i<listaPous.size()){
            logger.info("Gmail comprobado: "+listaPous.get(i).getCorreoPou());
            if (Objects.equals(listaPous.get(i).getCorreoPou(), gmail)){
                logger.info("SALTA EL AVISO");
                throw new CorreoYaExisteException();
            }
            i++;
        }
    }

    @Override
    public List<Pou> obtenerPousOrdenadosDescendentemente (String columnaId){
        List<Pou> listaPous = this.session.obtenerObjetosOrdenadosPorAlgo(Pou.class, columnaId);
        logger.info("ID del Primero en DB: "+listaPous.get(0).getPouId());
        return listaPous;
    }

    @Override
    public ObjetoArmario pouConsumeArticulo(String pouId, String articuloId) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException, ObjetoArmarioNoDisponible {
        return null;
    }

    @Override
    public void pouCambiaCamiseta(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCambiaZapatos(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCambiaGorra(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCambiaGafas(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouModificaNivelHambre(String pouId, Integer varNivelHambre) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public void pouModificaNivelSalud(String pouId, Integer varNivelSalud) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public void pouModificaNivelDiversion(String pouId, Integer varNivelDiversion) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public void pouModificaNivelSueno(String pouId, Integer varNivelSueno) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public List<ObjetoArmario> obtenerComidasArmario() {
        return null;
    }

    @Override
    public List<ObjetoArmario> obtenerBebidasArmario() {
        return null;
    }

    @Override
    public List<ObjetoArmario> obtenerPocionesArmario() {
        return null;
    }

    @Override
    public List<ObjetoArmario> obtenerRopaArmario() {
        return null;
    }

    @Override
    public void pouModificaDinero(String pouId, double varDinero) throws PouIDNoExisteException, PouNoTieneDineroSuficienteException {

    }

    @Override
    public Integer dameNumArticulosTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> listaObjetosTiendaTipo(String tipoArticulo) {
        return null;
    }

    @Override
    public List<ObjetoArmario> listaObjetosArmarioTipo(String tipoArticulo) {
        return null;
    }

    @Override
    public List<ObjetoTienda> listaObjetosTipo(String tipoArticulo) {

        return null;
    }

    @Override
    public Pou obtenerPouByCredentials(Credenciales credenciales) {
        logger.info("Se quiere obtener el pou que tenga el correo" + credenciales.getCorreoPou() + " y la contraseña " + credenciales.getPasswordPou() + ".");
        List<Pou> listaPous = this.session.findAll(Pou.class);
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

    @Override
    public ObjetoArmario obtenerObjetoArmario(String pouId) throws PouIDNoExisteException {
        return null;
    }

    @Override
    public void addObjetosAArmario(int idArmario, String pouId, String tipoArticulo, String idArticulo, Integer cantidad) {
        ObjetoArmario a = new ObjetoArmario(idArmario,pouId,tipoArticulo, idArticulo,cantidad);
        this.session.save(a);
        this.objetosArmario.put(pouId, a);
    }

    @Override
    public void updateObjeto(Object objeto) {
        this.session.update(objeto);
    }

    @Override
    public void updateObjetoArmario(String pouId, String idArticulo, int cantidad) {

        try {
            this.session.updateObjetoArmario(cantidad,pouId,idArticulo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAndroid(InformacionPou informacionPou){

        Pou nuevosDatosPou = new Pou(informacionPou.getData_pouId(), informacionPou.getData_nombrePou(), informacionPou.getData_nacimientoPou(), informacionPou.getData_correoPou(), informacionPou.getData_passwordPou());

        nuevosDatosPou.setDineroPou(informacionPou.getAmountDinero());
        nuevosDatosPou.setNivelHambrePou(informacionPou.getLvlHambre());
        nuevosDatosPou.setNivelSaludPou(informacionPou.getLvlSalud());
        nuevosDatosPou.setNivelDiversionPou(informacionPou.getLvlDiversion());
        nuevosDatosPou.setNivelSuenoPou(informacionPou.getLvlSueno());
        nuevosDatosPou.setCamisetaId(informacionPou.getPouCamiseta());
        nuevosDatosPou.setZapatosId(informacionPou.getPouBambas());
        nuevosDatosPou.setGorraId(informacionPou.getPouGorro());
        nuevosDatosPou.setGafasId(informacionPou.getPouGafas());
        nuevosDatosPou.setRecord(informacionPou.getRecordPou());

        updateObjeto(nuevosDatosPou);


        int amountCandy = informacionPou.getAmountCandy();
        int amountManzana = informacionPou.getAmountManzana();
        int amountPizza = informacionPou.getAmountPizza();
        int amountAgua = informacionPou.getAmountAgua();
        int amountAquarius = informacionPou.getAmountAquarius();
        int amountRoncola = informacionPou.getAmountRoncola();
        int amountHambre = informacionPou.getAmountHambre();
        int amountSalud = informacionPou.getAmountSalud();
        int amountDiversion = informacionPou.getAmountDiversion();
        int amountSueno = informacionPou.getAmountSueno();


        updateObjetoArmario(informacionPou.getData_pouId(),"candy",amountCandy);
        updateObjetoArmario(informacionPou.getData_pouId(),"manzana",amountManzana);
        updateObjetoArmario(informacionPou.getData_pouId(),"pizza",amountPizza);
        updateObjetoArmario(informacionPou.getData_pouId(),"agua",amountAgua);
        updateObjetoArmario(informacionPou.getData_pouId(),"aquarius",amountAquarius);
        updateObjetoArmario(informacionPou.getData_pouId(),"roncola",amountRoncola);
        updateObjetoArmario(informacionPou.getData_pouId(),"hambre",amountHambre);
        updateObjetoArmario(informacionPou.getData_pouId(),"salud",amountSalud);
        updateObjetoArmario(informacionPou.getData_pouId(),"diversion",amountDiversion);
        updateObjetoArmario(informacionPou.getData_pouId(),"sueno",amountSueno);

    }
/*
    @Override
    public List<ObjetoTienda> obtenerObjetosTienda() {
        return null;
    }



    @Override
    public Map<String, Pou> obtenerPous() {
        return null;
    }

    @Override
    public Pou obtenerPou(String pouId) throws PouIDNoExisteException {
        return null;
    }

    @Override
    public void addObjetosATienda(String articuloId, String nombreArticulo, double precioArticulo, String tipoArticulo, Integer recargaHambre, Integer recargaSalud, Integer recargaDiversion, Integer recargaSueno) throws ObjetoTiendaYaExisteException {

    }

    @Override
    public ObjetoTienda obtenerObjetoTienda(String articuloId) throws ObjetoTiendaNoExisteException {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerComidasTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerBebidasTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerPocionesTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerRopasTienda() {
        return null;
    }

    @Override
    public void crearSala(String pouId, String salaId, String nombreSala) throws SalaYaExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCompraArticulos(String pouId, String articuloId, Integer cantidad) throws SalaNoExisteException, ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public ObjetoTienda pouConsumeArticulo(String pouId, String articuloId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {
        return null;
    }

    @Override
    public void pouCambiaCamiseta(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCambiaPantalon(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCambiaGorra(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouCambiaGafas(String pouId, String camisetaId) throws ObjetoTiendaNoExisteException, PouIDNoExisteException {

    }

    @Override
    public void pouModificaNivelHambre(String pouId, Integer varNivelHambre) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public void pouModificaNivelSalud(String pouId, Integer varNivelSalud) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public void pouModificaNivelDiversion(String pouId, Integer varNivelDiversion) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public void pouModificaNivelSueno(String pouId, Integer varNivelSueno) throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException {

    }

    @Override
    public List<ObjetoTienda> obtenerComidasArmario() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerBebidasArmario() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerPocionesArmario() {
        return null;
    }

    @Override
    public List<ObjetoTienda> obtenerRopaArmario() {
        return null;
    }

    @Override
    public void pouModificaDinero(String pouId, double varDinero) throws PouIDNoExisteException, PouNoTieneDineroSuficienteException {

    }

    @Override
    public Integer dameNumArticulosTienda() {
        return null;
    }

    @Override
    public List<ObjetoTienda> listaObjetosTipo(String tipoArticulo) {
        return null;
    }

*/
}
