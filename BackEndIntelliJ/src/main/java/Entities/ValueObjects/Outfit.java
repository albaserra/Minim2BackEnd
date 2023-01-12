package Entities.ValueObjects;

public class Outfit {

    // ATRIBUTOS

    Integer camisetaId;
    Integer pantalonId;
    Integer gorraId;
    Integer gafasId;

    // CONSTRUCTORES

    public Outfit() {}

    public Outfit(Integer camisetaId, Integer pantalonId, Integer gorraId, Integer gafasId) {
        this.camisetaId = camisetaId;
        this.pantalonId = pantalonId;
        this.gorraId = gorraId;
        this.gafasId = gafasId;
    }

    // GETTERS Y SETTERS

    public Integer getCamisetaId() {
        return camisetaId;
    }

    public void setCamisetaId(Integer camisetaId) {
        this.camisetaId = camisetaId;
    }

    public Integer getPantalonId() {
        return pantalonId;
    }

    public void setPantalonId(Integer pantalonId) {
        this.pantalonId = pantalonId;
    }

    public Integer getGorraId() {
        return gorraId;
    }

    public void setGorraId(Integer gorraId) {
        this.gorraId = gorraId;
    }

    public Integer getGafasId() {
        return gafasId;
    }

    public void setGafasId(Integer gafasId) {
        this.gafasId = gafasId;
    }
}
