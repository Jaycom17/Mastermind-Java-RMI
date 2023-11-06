package susuarios.sop_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import susuarios.dto.UsuarioDTO;
import susuarios.dto.UsuarioLoginDTO;

public class GestionUsuariosImpl extends UnicastRemoteObject implements GestionUsuariosInt{
    private ArrayList<UsuarioDTO> misUsuarios;
    private UsuarioDTO userAdmin;
    
    public GestionUsuariosImpl() throws RemoteException {
        super();
        this.misUsuarios = new ArrayList<UsuarioDTO>();
        this.userAdmin = new UsuarioDTO(0, "Hermen Egildo", "admin", "admin");
    }

    @Override
    public int abrirSesion(UsuarioLoginDTO obUsuario) throws RemoteException{
        System.out.println("Iniciando Sesion");

        if (obUsuario.getId() == this.userAdmin.getId() && obUsuario.getUsuario().equals(this.userAdmin.getUsuario()) && obUsuario.getClave().equals(this.userAdmin.getClave())) {
            return 0;
        }

        for (UsuarioDTO usuarioDTO : this.misUsuarios) {
            if(usuarioDTO.getId() == obUsuario.getId() && usuarioDTO.getUsuario().equals(obUsuario.getUsuario()) && usuarioDTO.getClave().equals(obUsuario.getClave())){
                return 1;
            }
        }

        return -1;
    }
    
    @Override
    public boolean registrarUsuario(UsuarioDTO objUsuario) throws RemoteException {
        System.out.println("Registro usuario");

        for (UsuarioDTO usuarioDTO : this.misUsuarios) {
            if(usuarioDTO.getId() == objUsuario.getId()){
                return false;
            }
        }

        if (this.misUsuarios.size() < 5) {
            this.misUsuarios.add(objUsuario);
            return true;
        }
        
        return false;
    }

    @Override
    public UsuarioDTO consultarUsuario(int identificacion) throws RemoteException {
        System.out.println("Consultar usuario");
        
        for (UsuarioDTO usuarioDTO : this.misUsuarios) {
            if(usuarioDTO.getId() == identificacion){
                return usuarioDTO;
            }
        }
        
        return null;
    }

    @Override
    public boolean eliminarUsuario(int identificacion) throws RemoteException {
        System.out.println("Eliminar usuario");
        
        for (UsuarioDTO usuarioDTO : this.misUsuarios) {
            if(usuarioDTO.getId() == identificacion){
                return this.misUsuarios.remove(usuarioDTO);
            }
        }
        
        return false;
    }

    @Override
    public boolean editarUsuario(UsuarioDTO objUsuario) throws RemoteException {
        System.out.println("Editar usuario");
        
        for (UsuarioDTO usuarioDTO : this.misUsuarios) {
            if(usuarioDTO.getId() == objUsuario.getId()){
                return this.misUsuarios.set(this.misUsuarios.indexOf(usuarioDTO), objUsuario) != null;
            }
        }
        
        return false;
    }
}
