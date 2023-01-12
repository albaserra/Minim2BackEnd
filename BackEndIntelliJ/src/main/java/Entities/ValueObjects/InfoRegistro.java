package Entities.ValueObjects;

public class InfoRegistro {

    //ATRIBUTOS
    String pouId;
    String nombrePou;
    String nacimientoPou;
    String correo;
    String password;

    //CONSTRUCTORES

    public InfoRegistro() {}

    public InfoRegistro(String pouId, String nombrePou, String nacimientoPou, String correo, String password) {
        this.pouId = pouId;
        this.nombrePou = nombrePou;
        this.nacimientoPou = nacimientoPou;
        this.correo = correo;
        this.password = password;
    }

    //GETTERS Y SETTERS

    public String getPouId() {
        return pouId;
    }

    public void setPouId(String pouId) {
        this.pouId = pouId;
    }

    public String getNombrePou() {
        return nombrePou;
    }

    public void setNombrePou(String nombrePou) {
        this.nombrePou = nombrePou;
    }

    public String getNacimientoPou() {
        return nacimientoPou;
    }

    public void setNacimientoPou(String nacimientoPou) {
        this.nacimientoPou = nacimientoPou;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
