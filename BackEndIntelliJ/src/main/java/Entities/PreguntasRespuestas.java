package Entities;

public class PreguntasRespuestas {
    // ATRIBUTOS

    String preguntaId;

    String pregunta;
    String respuesta;

    // CONSTRUCTORES

    public PreguntasRespuestas(String preguntaId, String pregunta, String respuesta) {
        this.preguntaId = preguntaId;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    // GETTERS Y SETTERS

    public String getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(String preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
