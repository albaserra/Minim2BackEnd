import Entities.Exceptions.*;
import Entities.ObjetoArmario;
import Entities.ObjetoTienda;
import Entities.Pou;
import Managers.PouGameManager;
import Managers.PouGameManagerImpl;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PouGameImplTest {

    final static Logger logger = Logger.getLogger(PouGameManagerImpl.class);

    PouGameManager jvm;

    @Before

    public void setUp() throws PouIDYaExisteException, CorreoYaExisteException, ObjetoTiendaYaExisteException, SalaYaExisteException, PouIDNoExisteException, ObjetoTiendaNoExisteException {
        this.jvm = new PouGameManagerImpl();

        this.jvm.crearPou("marcmmonfort", "Marc", "28/10/2001", "marc@gmail.com", "28102001");
        this.jvm.crearPou("victorfernandez", "Victor", "13/06/2001", "victor@gmail.com", "13062001");
        this.jvm.crearPou("albaserra", "Alba", "29/06/2001", "alba@gmail.com", "29062001");

        this.jvm.addObjetosATienda("C001","Manzana",1,"Comida",10,0,0,0 );
        this.jvm.addObjetosATienda("C002","Pan",1,"Comida",10,0,0,0 );
        this.jvm.addObjetosATienda("B001","Agua",4,"Bebida",4,4,0,0);
        this.jvm.addObjetosATienda("P001","Salud",10,"Pocion",0,20,0,0);
        this.jvm.addObjetosATienda("R001","Gafas de sol",30,"Ropa",0,0,0,0);

        this.jvm.addObjetosAArmario(0,"albaserra","Bebida","B001", 1);
        this.jvm.addObjetosAArmario(1,"albaserra","Comida","C001",2);
        this.jvm.addObjetosAArmario(2,"albaserra","Comida","C002",1);
    }

    @After
    public void tearDown() {
        this.jvm = null;
    }

    @Test
    public void testRegistrarPou() throws PouIDYaExisteException, CorreoYaExisteException {
        // CASO 1 = ID del Pou ya existe.
        Assert.assertThrows(PouIDYaExisteException.class, ()-> this.jvm.crearPou("marcmmonfort", "Carlos", "20/10/2001", "carlos@gmail.com", "20102001"));
        // CASO 2 = Correo del Pou ya registrado.
        Assert.assertThrows(CorreoYaExisteException.class, ()-> this.jvm.crearPou("carlossainz", "Carlos", "20/10/2001", "marc@gmail.com", "20102001"));
        // CASO 3 = Registro Satisfactorio.
        this.jvm.crearPou("eloimoncho", "Eloi", "28/08/2001", "eloi@gmail.com", "28082001");
        Assert.assertEquals(4, this.jvm.size());
        Map<String, Pou> listaPous = this.jvm.obtenerPous();
        Assert.assertEquals(4,listaPous.size());
    }

    @Test
    public void testLoginPou() throws CorreoNoExisteException, PasswordIncorrectaException{
        // CASO 1 = Correo del Login no existe.
        Assert.assertThrows(CorreoNoExisteException.class, ()-> this.jvm.loginPou("psg@gmail.com","12345678"));
        // CASO 2 = Contraseña del Login no es la correcta.
        Assert.assertThrows(PasswordIncorrectaException.class, ()-> this.jvm.loginPou("marc@gmail.com","28102000"));
        // CASO 3 = Login Satisfactorio.
        this.jvm.loginPou("marc@gmail.com","28102001");
    }

    @Test
    public void testObtenerPou() throws PouIDNoExisteException{
        // CASO 1 = El Pou no existe. No se encuentra el Id.
        Assert.assertThrows(PouIDNoExisteException.class, () -> this.jvm.obtenerPou("eloimoncho"));
        // CASO 2 = El Pou sí que existe.
        Pou test = this.jvm.obtenerPou("marcmmonfort");
        Assert.assertEquals("marcmmonfort",test.getPouId());
    }

    @Test
    public void testObtenerPous() throws PouIDYaExisteException, CorreoYaExisteException {
        Map<String, Pou> pouMap = this.jvm.obtenerPous();
        Assert.assertEquals(3, pouMap.size());
        this.jvm.crearPou("eloimoncho", "Eloi", "28/08/2001", "eloi@gmail.com", "28082001");
        pouMap = this.jvm.obtenerPous();
        Assert.assertEquals(4, pouMap.size());
    }

    @Test
    public void testObtenerObjetoTienda() throws ObjetoTiendaNoExisteException{
        // CASO 1 = El objeto no existe. No se encuentra el Id.
        Assert.assertThrows(ObjetoTiendaNoExisteException.class, () -> this.jvm.obtenerObjetoTienda("A001"));
        // CASO 2 = El objeto sí que existe.
        ObjetoTienda test = this.jvm.obtenerObjetoTienda("C001");
        Assert.assertEquals("C001",test.getArticuloId());
    }

    @Test
    public void testObtenerComidasTienda() throws ObjetoTiendaYaExisteException {
        List<ObjetoTienda> listaComidas = this.jvm.obtenerComidasTienda();
        int numComidas = listaComidas.size();
        Assert.assertEquals(2, numComidas);
        this.jvm.addObjetosATienda("C100","Patatas",2,"Comida",10,0,0,0 );
        this.jvm.addObjetosATienda("C103","Arroz",3,"Comida",15,0,0,0 );
        listaComidas = this.jvm.obtenerComidasTienda();
        numComidas = listaComidas.size();
        Assert.assertEquals("C002",listaComidas.get(0).getArticuloId());
        Assert.assertEquals("C001",listaComidas.get(1).getArticuloId());
        Assert.assertEquals("C100",listaComidas.get(2).getArticuloId());
        Assert.assertEquals("C103",listaComidas.get(3).getArticuloId());
        Assert.assertEquals(4, numComidas);
    }

    @Test
    public void testObtenerBebidasTienda() throws ObjetoTiendaYaExisteException {
        List<ObjetoTienda> listaBebidas = this.jvm.obtenerBebidasTienda();
        int numBebidas = listaBebidas.size();
        Assert.assertEquals(1, numBebidas);
        this.jvm.addObjetosATienda("B002","Fanta",10,"Bebida",5,0,0,0 );
        this.jvm.addObjetosATienda("B003","Cafe",6,"Bebida",3,0,0,0 );
        listaBebidas = this.jvm.obtenerBebidasTienda();
        numBebidas = listaBebidas.size();
        Assert.assertEquals("B001",listaBebidas.get(0).getArticuloId());
        Assert.assertEquals("B003",listaBebidas.get(1).getArticuloId());
        Assert.assertEquals("B002",listaBebidas.get(2).getArticuloId());
        Assert.assertEquals(3, numBebidas);
    }

    @Test
    public void testObtenerPocionesTienda() throws ObjetoTiendaYaExisteException {
        List<ObjetoTienda> listaPociones = this.jvm.obtenerPocionesTienda();
        int numPociones = listaPociones.size();
        Assert.assertEquals(1, numPociones);
        this.jvm.addObjetosATienda("P002","Hambre",12,"Pocion",20,0,0,0);
        this.jvm.addObjetosATienda("P003","Energia",11,"Pocion",0,0,0,20);
        listaPociones = this.jvm.obtenerPocionesTienda();
        numPociones = listaPociones.size();
        Assert.assertEquals("P001",listaPociones.get(0).getArticuloId());
        Assert.assertEquals("P003",listaPociones.get(1).getArticuloId());
        Assert.assertEquals("P002",listaPociones.get(2).getArticuloId());
        Assert.assertEquals(3, numPociones);
    }

    @Test
    public void testObtenerRopasTienda() throws ObjetoTiendaYaExisteException {
        List<ObjetoTienda> listaRopas = this.jvm.obtenerRopasTienda();
        int numPociones = listaRopas.size();
        Assert.assertEquals(1, numPociones);
        this.jvm.addObjetosATienda("R002","Gorra",20,"Ropa",0,0,0,0);
        this.jvm.addObjetosATienda("R003","Camiseta",15,"Ropa",0,0,0,0);
        listaRopas = this.jvm.obtenerRopasTienda();
        numPociones = listaRopas.size();
        Assert.assertEquals("R003",listaRopas.get(0).getArticuloId());
        Assert.assertEquals("R002",listaRopas.get(1).getArticuloId());
        Assert.assertEquals("R001",listaRopas.get(2).getArticuloId());
        Assert.assertEquals(3, numPociones);
    }

    @Test
    public void testPouModificaNivelHambre()throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException{
        //CASO 1 = ID del pou no existe
        Assert.assertThrows(PouIDNoExisteException.class, () -> this.jvm.pouModificaNivelHambre("eloimoncho",20));
        //CASO 2 = Nivel por debajo del mínimo
        Assert.assertThrows(NivelPorDebajoDelMinimoException.class, () -> this.jvm.pouModificaNivelHambre("marcmmonfort",-120));
        //CASO 3 = Nivel por encima del máximo
        Assert.assertThrows(NivelPorEncimaDelMaximoException.class, () -> this.jvm.pouModificaNivelHambre("marcmmonfort",120));
        //CASO 4 = Se resta el nivel de hambre
        this.jvm.pouModificaNivelHambre("albaserra",-30);
        int miNivel = this.jvm.obtenerPou("albaserra").getNivelHambrePou();
        Assert.assertEquals(70, miNivel);
        //CASO 5 = Se suma el nivel de hambre
        this.jvm.pouModificaNivelHambre("albaserra",5);
        miNivel = this.jvm.obtenerPou("albaserra").getNivelHambrePou();
        Assert.assertEquals(75, miNivel);
    }

    @Test
    public void pouModificaNivelSalud()throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException{
        //CASO 1 = ID del pou no existe
        Assert.assertThrows(PouIDNoExisteException.class, () -> this.jvm.pouModificaNivelSalud("eloimoncho",20));
        //CASO 2 = Nivel por debajo del mínimo
        Assert.assertThrows(NivelPorDebajoDelMinimoException.class, () -> this.jvm.pouModificaNivelSalud("marcmmonfort",-120));
        //CASO 3 = Nivel por encima del máximo
        Assert.assertThrows(NivelPorEncimaDelMaximoException.class, () -> this.jvm.pouModificaNivelSalud("marcmmonfort",120));
        //CASO 4 = Se resta el nivel de hambre
        this.jvm.pouModificaNivelSalud("albaserra",-30);
        int miNivel = this.jvm.obtenerPou("albaserra").getNivelSaludPou();
        Assert.assertEquals(70, miNivel);
        //CASO 5 = Se suma el nivel de hambre
        this.jvm.pouModificaNivelSalud("albaserra",5);
        miNivel = this.jvm.obtenerPou("albaserra").getNivelSaludPou();
        Assert.assertEquals(75, miNivel);
    }

    @Test
    public void pouModificaNivelDiversion()throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException{
        //CASO 1 = ID del pou no existe
        Assert.assertThrows(PouIDNoExisteException.class, () -> this.jvm.pouModificaNivelDiversion("eloimoncho",20));
        //CASO 2 = Nivel por debajo del mínimo
        Assert.assertThrows(NivelPorDebajoDelMinimoException.class, () -> this.jvm.pouModificaNivelDiversion("marcmmonfort",-120));
        //CASO 3 = Nivel por encima del máximo
        Assert.assertThrows(NivelPorEncimaDelMaximoException.class, () -> this.jvm.pouModificaNivelDiversion("marcmmonfort",120));
        //CASO 4 = Se resta el nivel de hambre
        this.jvm.pouModificaNivelDiversion("albaserra",-40);
        int miNivel = this.jvm.obtenerPou("albaserra").getNivelDiversionPou();
        Assert.assertEquals(60, miNivel);
        //CASO 5 = Se suma el nivel de hambre
        this.jvm.pouModificaNivelDiversion("albaserra",30);
        miNivel = this.jvm.obtenerPou("albaserra").getNivelDiversionPou();
        Assert.assertEquals(90, miNivel);
    }

    @Test
    public void pouModificaNivelSueno()throws PouIDNoExisteException, NivelPorDebajoDelMinimoException, NivelPorEncimaDelMaximoException{
        //CASO 1 = ID del pou no existe
        Assert.assertThrows(PouIDNoExisteException.class, () -> this.jvm.pouModificaNivelSueno("eloimoncho",20));
        //CASO 2 = Nivel por debajo del mínimo
        Assert.assertThrows(NivelPorDebajoDelMinimoException.class, () -> this.jvm.pouModificaNivelSueno("marcmmonfort",-120));
        //CASO 3 = Nivel por encima del máximo
        Assert.assertThrows(NivelPorEncimaDelMaximoException.class, () -> this.jvm.pouModificaNivelSueno("marcmmonfort",120));
        //CASO 4 = Se resta el nivel de hambre
        this.jvm.pouModificaNivelSueno("albaserra",-30);
        int miNivel = this.jvm.obtenerPou("albaserra").getNivelSuenoPou();
        Assert.assertEquals(70, miNivel);
        //CASO 5 = Se suma el nivel de hambre
        this.jvm.pouModificaNivelSueno("albaserra",10);
        miNivel = this.jvm.obtenerPou("albaserra").getNivelSuenoPou();
        Assert.assertEquals(80, miNivel);
    }

    @Test
    public void pouCompraArticulosTest() throws ObjetoTiendaNoExisteException, PouIDNoExisteException, PouNoTieneDineroSuficienteException{
        //CASO 1 = ID del pou no existe
        Assert.assertThrows(PouIDNoExisteException.class, () -> this.jvm.pouCompraArticulos("carlossainz","C001",1,"Comida"));
        //CASO 2 = ID del objeto no existe
        Assert.assertThrows(ObjetoTiendaNoExisteException.class, () -> this.jvm.pouCompraArticulos("albaserra","AAAA",1,"Comida"));
        //CASO 3 = Todos los datos son correctos.
        this.jvm.pouCompraArticulos("albaserra","B001",1,"Bebida");
        this.jvm.pouCompraArticulos("albaserra","C001",2,"Comida");
        this.jvm.pouCompraArticulos("albaserra","C002",2,"Comida");
        List<ObjetoArmario> listaBebidas = this.jvm.listaObjetosArmarioTipo("Bebida");
        List<ObjetoArmario> listaComidas = this.jvm.listaObjetosArmarioTipo("Comida");
        Assert.assertEquals(1, listaBebidas.size());
        Assert.assertEquals(2, listaComidas.size());

    }

}

    /*

    VIEJO ...

    @Test
    public void testCrearJuego() {
        // Creamos un juego.
        this.jvm.crearJuego("Fall Guys", "Juego de retos", 3);
        Assert.assertEquals(4, this.jvm.numJuegos());

        // Creamos otro juego.
        this.jvm.crearJuego("NBA", "Juego de baloncesto", 4);
        Assert.assertEquals(5, this.jvm.numJuegos());
    }
>>>>>>> 97b2c2e7d5f44c6250b070abcdc8ac7c4e41dd11

    @Test
    public void pouCompraArticulos(){

    }
}

     */
