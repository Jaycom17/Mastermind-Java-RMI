package sjuego.dto;

import java.io.Serializable;

public class JugadorDTO implements Serializable{

    public enum Roles {
        Retador,
        Retado,
    }

    private int id;
    private Roles rol;

    public JugadorDTO(int id, Roles rol) {
        this.id = id;
        this.rol = rol;
    }

    public int getId() {
        return this.id;
    }

    public Roles getRol() {
        return this.rol;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public void cambiarRol() {
        if (this.rol == Roles.Retador) {
            this.rol = Roles.Retado;
        } else {
            this.rol = Roles.Retador;
        }
    }
}
