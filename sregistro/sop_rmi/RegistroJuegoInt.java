package sregistro.sop_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import sjuego.dto.PartidaTerminadaDTO;

public interface RegistroJuegoInt extends Remote{

    public boolean registrarJuego(PartidaTerminadaDTO partidaTerminada) throws RemoteException;

    public ArrayList<PartidaTerminadaDTO> buscarPartida(int idPartida) throws RemoteException;

    public int getUltimoIdPartida() throws RemoteException;
}
