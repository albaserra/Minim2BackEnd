package Entities;

public class ObjetoTienda {

    // ATRIBUTOS

    String articuloId;
    String nombreArticulo;
    double precioArticulo;
    String tipoArticulo;
    Integer recargaHambre;
    Integer recargaSalud;
    Integer recargaDiversion;
    Integer recargaSueno;


    // CONSTRUCTORES

    public ObjetoTienda() {}

    public ObjetoTienda(String articuloId, String nombreArticulo, double precioArticulo, String tipoArticulo, Integer recargaHambre, Integer recargaSalud, Integer recargaDiversion, Integer recargaSueno) {
        this.articuloId = articuloId;
        this.nombreArticulo = nombreArticulo;
        this.precioArticulo = precioArticulo;
        this.tipoArticulo = tipoArticulo;
        this.recargaHambre = recargaHambre;
        this.recargaSalud = recargaSalud;
        this.recargaDiversion = recargaDiversion;
        this.recargaSueno = recargaSueno;
    }

    // GETTERS Y SETTERS

    public String getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(String articuloId) {
        this.articuloId = articuloId;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public double getPrecioArticulo() {
        return precioArticulo;
    }

    public void setPrecioArticulo(double precioArticulo) {
        this.precioArticulo = precioArticulo;
    }

    public Integer getRecargaHambre() {
        return recargaHambre;
    }

    public void setRecargaHambre(Integer recargaHambre) {
        this.recargaHambre = recargaHambre;
    }

    public Integer getRecargaSalud() {
        return recargaSalud;
    }

    public void setRecargaSalud(Integer recargaSalud) {
        this.recargaSalud = recargaSalud;
    }

    public Integer getRecargaDiversion() {
        return recargaDiversion;
    }

    public void setRecargaDiversion(Integer recargaDiversion) {
        this.recargaDiversion = recargaDiversion;
    }

    public Integer getRecargaSueno() {
        return recargaSueno;
    }

    public void setRecargaSueno(Integer recargaSueno) {
        this.recargaSueno = recargaSueno;
    }

    public String getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(String tipoArticulo) {
        this.tipoArticulo = tipoArticulo;
    }

}
