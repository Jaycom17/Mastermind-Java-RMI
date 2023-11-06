package sjuego.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class PartidaEnCursoDTO implements Serializable {
    private JugadorDTO jugador1;
    private JugadorDTO jugador2;
    private ArrayList<Integer> clave;
    private int puntuacionJugador1;
    private int puntuacionJugador2;
    private ArrayList<ArrayList<Integer>> historial;
    private ArrayList<ArrayList<Integer>> historialRespuestas;
    private boolean turno;
    private boolean terminada;
    private int ronda;

    public PartidaEnCursoDTO(JugadorDTO jugador1, JugadorDTO jugador2, ArrayList<Integer> clave, int puntuacionJugador1,
            int puntuacionJugador2, ArrayList<ArrayList<Integer>> historial, ArrayList<ArrayList<Integer>> historialRespuestas, boolean turno, boolean terminada, int ronda) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.clave = clave;
        this.puntuacionJugador1 = puntuacionJugador1;
        this.puntuacionJugador2 = puntuacionJugador2;
        this.historial = historial;
        this.historialRespuestas = historialRespuestas;
        this.turno = turno;
        this.terminada = terminada;
        this.ronda = ronda;
    }

    public JugadorDTO getJugador1() {
        return this.jugador1;
    }

    public JugadorDTO getJugador2() {
        return this.jugador2;
    }

    public ArrayList<Integer> getClave() {
        return this.clave;
    }

    public int getPuntuacionJugador1() {
        return this.puntuacionJugador1;
    }

    public int getPuntuacionJugador2() {
        return this.puntuacionJugador2;
    }

    public ArrayList<ArrayList<Integer>> getHistorial() {
        return this.historial;
    }

    public ArrayList<ArrayList<Integer>> getHistorialRespuestas() {
        return this.historialRespuestas;
    }

    public boolean getTurno() {
        return this.turno;
    }

    public boolean getTerminada() {
        return this.terminada;
    }

    public int getRonda() {
        return this.ronda;
    }

    public void setJugador1(JugadorDTO jugador1) {
        this.jugador1 = jugador1;
    }

    public void setJugador2(JugadorDTO jugador2) {
        this.jugador2 = jugador2;
    }

    public void setClave(ArrayList<Integer> clave) {
        this.clave = clave;
    }

    public void setPuntuacionJugador1(int puntuacionJugador1) {
        this.puntuacionJugador1 = puntuacionJugador1;
    }

    public void setPuntuacionJugador2(int puntuacionJugador2) {
        this.puntuacionJugador2 = puntuacionJugador2;
    }

    public void setHistorial(ArrayList<ArrayList<Integer>> historial) {
        this.historial = historial;
    }

    public void setHistorialRespuestas(ArrayList<ArrayList<Integer>> historialRespuestas) {
        this.historialRespuestas = historialRespuestas;
    }

    public void cambiarRolJugadores(){
        this.jugador1.cambiarRol();
        this.jugador2.cambiarRol();
    }

    public void cambiarTurno(){
        this.turno = !this.turno;
    }
    
    public void setTurno(boolean turno){
        this.turno = turno;
    }

    public void terminar(){
        this.terminada = true;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }
}
