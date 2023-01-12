package Entities;

public class ObjetoArmario {

    // ATRIBUTOS

    int idArmario;
    String pouId;
    String tipoArticulo;
    String idArticulo;
    Integer cantidad;


    // CONSTRUCTOR
    public ObjetoArmario(){}

    public ObjetoArmario(int idArmario, String pouId, String tipoArticulo, String idArticulo, Integer cantidad) {
        this.idArmario = idArmario;
        this.pouId = pouId;
        this.tipoArticulo = tipoArticulo;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
    }

    public int getIdArmario() {
        return idArmario;
    }

    public void setIdArmario(int idArmario) {
        this.idArmario = idArmario;
    }

    public String getPouId() {
        return pouId;
    }

    public void setPouId(String pouId) {
        this.pouId = pouId;
    }

    public String getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(String tipoArticulo) {
        this.tipoArticulo = tipoArticulo;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
