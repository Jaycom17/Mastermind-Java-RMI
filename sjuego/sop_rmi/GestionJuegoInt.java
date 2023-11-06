package sjuego.sop_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import sjuego.dto.PartidaTerminadaDTO;
import sjuego.dto.PartidaEnCursoDTO;

public interface GestionJuegoInt extends Remote{

    public int iniciarJuego(int idJugador) throws RemoteException;

    public ArrayList<PartidaTerminadaDTO> buscarPartida(int idJugador) throws RemoteException;

    public PartidaEnCursoDTO establecerClave(ArrayList<Integer> clave) throws RemoteException;

    public PartidaEnCursoDTO enviarIntento(ArrayList<Integer> intento) throws RemoteException;

    public PartidaEnCursoDTO enviarRespuesta(ArrayList<Integer> respuesta) throws RemoteException;

    public PartidaEnCursoDTO getEstado() throws RemoteException;

    public int getIdRetador() throws RemoteException;
}
