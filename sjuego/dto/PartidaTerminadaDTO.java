package sjuego.dto;

import java.io.Serializable;

public class PartidaTerminadaDTO implements Serializable{
    private int idPartida;
    private int idRetador;
    private int idRetado;
    private int puntajeJugador1;
    private int puntajeJugador2;
    private String fecha;

    public PartidaTerminadaDTO(int idPartida, int idRetador, int idRetado, int puntajeJugador1, int puntajeJugador2, String fecha) {
        this.idPartida = idPartida;
        this.idRetador = idRetador;
        this.idRetado = idRetado;
        this.puntajeJugador1 = puntajeJugador1;
        this.puntajeJugador2 = puntajeJugador2;
        this.fecha = fecha;
    }

    public int getIdPartida() {
        return this.idPartida;
    }

    public int getIdRetador() {
        return this.idRetador;
    }

    public int getIdRetado() {
        return this.idRetado;
    }

    public int getPuntajeJugador1() {
        return this.puntajeJugador1;
    }

    public int getPuntajeJugador2() {
        return this.puntajeJugador2;
    }

    public String getFecha() {
        return this.fecha;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public void setIdRetador(int idRetador) {
        this.idRetador = idRetador;
    }

    public void setRetado(int idRetado) {
        this.idRetado = idRetado;
    }

    public void setPuntajeJugador1(int puntaje) {
        this.puntajeJugador1 = puntaje;
    }

    public void setPuntajeJugador2(int puntaje) {
        this.puntajeJugador2 = puntaje;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
