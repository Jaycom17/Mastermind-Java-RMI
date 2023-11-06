package susuarios.dto;

import java.io.Serializable;

public class UsuarioDTO implements Serializable{
    private int id;
    private String nombreCompeto;
    private String usuario;
    private String clave;

    public UsuarioDTO(int id, String nombreCompeto, String usuario, String clave) {
        this.id = id;
        this.nombreCompeto = nombreCompeto;
        this.usuario = usuario;
        this.clave = clave;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombreCompeto() {
        return this.nombreCompeto;
    }
    
    public void setNombreCompeto(String nombreCompeto) {
        this.nombreCompeto = nombreCompeto;
    }
    
    public String getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }
}
