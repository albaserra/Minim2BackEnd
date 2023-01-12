package Entities.ValueObjects;

public class Credenciales {

    // ATRIBUTOS

    String correoPou;
    String passwordPou;

    // CONSTRUCTORES

    public Credenciales() {}

    public Credenciales(String correoPou, String passwordPou) {
        this.correoPou = correoPou;
        this.passwordPou = passwordPou;
    }

    //GETTERS Y SETTERS

    public String getCorreoPou() {
        return correoPou;
    }

    public void setCorreoPou(String correoPou) {
        this.correoPou = correoPou;
    }

    public String getPasswordPou() {
        return passwordPou;
    }

    public void setPasswordPou(String passwordPou) {
        this.passwordPou = passwordPou;
    }
}
