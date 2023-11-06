package susuarios.sop_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import susuarios.dto.UsuarioDTO;
import susuarios.dto.UsuarioLoginDTO;

public interface GestionUsuariosInt extends Remote{
    public int abrirSesion(UsuarioLoginDTO objUsuario) throws RemoteException;

    public boolean registrarUsuario(UsuarioDTO objUsuario) throws RemoteException;

    public UsuarioDTO consultarUsuario(int identificacion) throws RemoteException;

    public boolean eliminarUsuario(int identificacion) throws RemoteException;

    public boolean editarUsuario(UsuarioDTO objUsuario) throws RemoteException;
}
