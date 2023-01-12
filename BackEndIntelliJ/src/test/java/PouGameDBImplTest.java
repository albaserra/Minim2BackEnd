import Entities.Exceptions.*;
import Entities.ObjetoArmario;
import Entities.Pou;
import Managers.PouGameDBManagerImpl;
import Managers.PouGameManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PouGameDBImplTest {
    PouGameManager pgm;

    @Before
    public void setUp() throws ObjetoTiendaYaExisteException {
        pgm = new PouGameDBManagerImpl();
        this.pgm.addObjetosATienda("agua","Agua", 4,"Bebida",0,10, 0, 0);
        this.pgm.addObjetosATienda("aquarius","Aquarius", 6,"Bebida",0,5,0,10);
        this.pgm.addObjetosATienda("candy","Candy", 6,"Comida",10, 0,0,5);
    }

    @After
    public void tearDown(){
        this.pgm = null;
    }

    @Test
    public void registerTest() throws PouIDYaExisteException, CorreoYaExisteException {
        this.pgm.crearPou("2", "2", "2", "2", "2");
        int num = this.pgm.size();
        Assert.assertEquals(4, num);
    }

    @Test
    public void loginTest() throws PasswordIncorrectaException, CorreoNoExisteException {
        this.pgm.loginPou("1","1");
    }

    @Test
    public void addObjetoTiendaTest() throws ObjetoTiendaYaExisteException {
        this.pgm.addObjetosATienda("manzana","Manzana", 5,"Comida",20, 0,0,0);
    }

    @Test
    public void obtenerObjetosArmarioPouTest() throws PouIDNoExisteException {
        Map<String, ObjetoArmario> armario = this.pgm.obtenerObjetosArmarioPou("1");
        List<ObjetoArmario> lista = new ArrayList<>(armario.values());
        Assert.assertEquals(0, armario.size());
        //Assert.assertEquals("C002", lista.get(0).getIdArticulo());
        //Assert.assertEquals("C001", lista.get(1).getIdArticulo());
        //Assert.assertEquals("B001", lista.get(2).getIdArticulo());
    }

    @Test
    public void pouCompraArticulosTest() throws PouIDNoExisteException, ObjetoTiendaNoExisteException, PouNoTieneDineroSuficienteException {
        this.pgm.pouCompraArticulos("eloimoncho", "agua",1,"Bebida");
        Map<String, ObjetoArmario> armario = this.pgm.obtenerObjetosArmarioPou("eloimoncho");
        List<ObjetoArmario> lista = new ArrayList<>(armario.values());
        Assert.assertEquals(1, armario.size());
        //Assert.assertEquals("B002", lista.get(0).getIdArticulo());
        //Assert.assertEquals("B001", lista.get(1).getIdArticulo());
    }

    @Test
    public void updateObjetoTest() throws PouIDNoExisteException {
        Pou pou = this.pgm.obtenerPou("1");
        pou.setDineroPou(300);
        pou.setNivelDiversionPou(70);
        pou.setNivelSuenoPou(60);
        pou.setRecord(88);
        pou.setGafasId("fiesta");
        this.pgm.updateObjeto(pou);
    }

}
