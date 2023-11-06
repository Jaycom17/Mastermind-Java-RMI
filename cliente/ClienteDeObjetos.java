package cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cliente.utilidades.UtilidadesConsola;
import cliente.utilidades.UtilidadesRegistroC;
import sjuego.sop_rmi.GestionJuegoInt;
import sjuego.dto.PartidaEnCursoDTO;
import sjuego.dto.PartidaTerminadaDTO;
import susuarios.sop_rmi.GestionUsuariosInt;
import susuarios.dto.UsuarioDTO;
import susuarios.dto.UsuarioLoginDTO;

public class ClienteDeObjetos {
    private static GestionUsuariosInt objRemotoUsuarios;
    private static GestionJuegoInt objRemotoJuego;

    public static void main(String[] args) {
        arrancarServidorUsuarios();
        arrancarServidorJuego();
        MenuPrincipal();

    }

    private static ArrayList<Integer> dividirCadena(String cadena) {
        String[] divisiones = cadena.split(",");

        if (divisiones.length != 5) {
            return null;
        }

        ArrayList<Integer> datos = new ArrayList<>();

        for (int i = 0; i < divisiones.length; i++) {
            datos.add(Integer.parseInt(divisiones[i]));
        }

        return datos;
    }

    private static void arrancarServidorUsuarios() {
        int numPuertoRMIRegistry = 0;
        String direccionIpRMIRegistry = "";

        System.out.println("cual es la direccion ip donde se encuentra el rmiregistry del servidor de usuarios");
        direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
        System.out.println("cual es el numero por el cual escucha el  rmiregistry del servidor de usuarios");
        numPuertoRMIRegistry = UtilidadesConsola.leerEntero();

        objRemotoUsuarios = (GestionUsuariosInt) UtilidadesRegistroC.obtenerObjRemoto(direccionIpRMIRegistry,
                numPuertoRMIRegistry, "gestionUsuarios");
    }

    private static void arrancarServidorJuego() {
        int numPuertoRMIRegistry = 0;
        String direccionIpRMIRegistry = "";

        System.out.println("cual es la direccion ip donde se encuentra el rmiregistry del servidor de juego");
        direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
        System.out.println("cual es el numero por el cual escucha el  rmiregistry del servidor de juego");
        numPuertoRMIRegistry = UtilidadesConsola.leerEntero();

        objRemotoJuego = (GestionJuegoInt) UtilidadesRegistroC.obtenerObjRemoto(direccionIpRMIRegistry,
                numPuertoRMIRegistry, "gestionJuego");
    }

    private static void MenuPrincipal() {
        int opcion = 0;
        do {
            System.out.println("==Menu Principal==");
            System.out.println("1. Abrir Sesion");
            System.out.println("2. Salir");

            opcion = UtilidadesConsola.leerEntero();

            switch (opcion) {
                case 1:
                    Opcion1();
                    break;
                case 2:
                    System.out.println("Salir...");
                    break;
                default:
                    System.out.println("Opción incorrecta");
            }

        } while (opcion != 2);
    }

    private static void Opcion1() {
        try {
            System.out.println("==Inicio de Sesion==");
            System.out.println("Ingrese la identificacion");
            int id = UtilidadesConsola.leerEntero();
            System.out.println("Ingrese el usuario ");
            String usuario = UtilidadesConsola.leerCadena();
            System.out.println("Ingrese la clave ");
            String clave = UtilidadesConsola.leerCadena();

            UsuarioLoginDTO objUsuario = new UsuarioLoginDTO(id, usuario, clave);

            int valor = objRemotoUsuarios.abrirSesion(objUsuario);
            switch (valor) {
                case 0:
                    System.out.println("Inicio de sesion realizado satisfactoriamente...");
                    mostrarMenuPrincipalAdmin();
                    break;
                case 1:
                    System.out.println("Inicio de sesion realizado satisfactoriamente...");
                    mostrarMenuPrincipalJugador(id);
                    break;
                default:
                    System.out.println("no se pudo realizar la operacion...");
                    break;
            }
        } catch (RemoteException e) {
            System.out.println("La operacion no se pudo completar, intente nuevamente...");
        }
    }

    private static void imprimirVector(PartidaEnCursoDTO ptc) {
        System.out.println("intentos");
        for (int i = 0; i < ptc.getHistorial().size(); i++) {
            System.out.println(ptc.getHistorial().get(i).toString());
        }

        System.out.println("-----------------\nResultado");
        for (int i = 0; i < ptc.getHistorialRespuestas().size(); i++) {
            System.out.println(ptc.getHistorialRespuestas().get(i).toString());
        }
    }

    private static void mostrarMenuPrincipalJugador(int id) {

        try {
            int opcion;

            do {
                System.out.println("==Menu Jugador==");
                System.out.println("1. Jugar");
                System.out.println("2. Consultar");
                System.out.println("3. Salir");

                opcion = UtilidadesConsola.leerEntero();
                switch (opcion) {
                    case 1:
                        System.out.println("==Opcion jugar==");
                        System.out.println("Se esta esperando otro jugador...");
                        int objUsuarioCO = objRemotoJuego.iniciarJuego(id);
                        switch (objUsuarioCO) {
                            case 0:
                                if (objRemotoJuego.getIdRetador() == id) {
                                    System.out.println("ingrese la clave");
                                    String clave = UtilidadesConsola.leerCadena();
                                    objRemotoJuego.establecerClave(dividirCadena(clave));

                                    int intento = 0;
                                    while (intento < 10) {
                                        System.out.println("esperando......");
                                        PartidaEnCursoDTO pec = objRemotoJuego.getEstado();
                                        while (pec.getTurno() && !pec.getTerminada()) {
                                            pec = objRemotoJuego.getEstado();
                                            try {
                                                TimeUnit.MILLISECONDS.sleep(250); // Esperar 1 segundo
                                            } catch (InterruptedException e) {
                                                // Manejo de excepciones si ocurre un error
                                                e.printStackTrace();
                                            }
                                        }
                                        if (pec.getTerminada()) {
                                            System.out.println("Partida Terminada");
                                        }

                                        imprimirVector(pec);
                                        System.out.println("ingrese la respuesta");
                                        String respuesta = UtilidadesConsola.leerCadena();
                                        objRemotoJuego.enviarRespuesta(dividirCadena(respuesta));
                                        intento++;
                                    }

                                } else {
                                    int intento = 0;
                                    while (intento < 10) {
                                        System.out.println("esperando......");
                                        PartidaEnCursoDTO pec = objRemotoJuego.getEstado();
                                        while (!pec.getTurno() && !pec.getTerminada()) {
                                            pec = objRemotoJuego.getEstado();
                                            try {
                                                TimeUnit.MILLISECONDS.sleep(250); // Esperar 1 segundo
                                            } catch (InterruptedException e) {
                                                // Manejo de excepciones si ocurre un error
                                                e.printStackTrace();
                                            }
                                        }

                                        if (pec.getTerminada()) {
                                            System.out.println("Partida Terminada");
                                        }

                                        imprimirVector(pec);
                                        System.out.println("ingrese el intento");
                                        String trya = UtilidadesConsola.leerCadena();
                                        objRemotoJuego.enviarIntento(dividirCadena(trya));
                                        intento++;
                                    }
                                }
                                break;
                            case 1:
                                System.out.println("El jugador ya esta registrado");
                                break;

                            case 2:
                                System.out.println("Hay un juego en curso");
                                break;
                            default:
                                System.out.println("no se pudo realizar la operacion...");
                                break;
                        }
                        break;
                    case 2:
                        System.out.println("==Opcion jugar==");
                        System.out.println("Ingrese el id del jugador a consultar sus partidas");
                        int idCO = UtilidadesConsola.leerEntero();
                        ArrayList<PartidaTerminadaDTO> objUsuarioJ = objRemotoJuego.buscarPartida(idCO);
                        if (objUsuarioJ != null) {
                            for (PartidaTerminadaDTO partidaTerminadaDTO : objUsuarioJ) {
                                System.out.println("==Datos de la partida==");
                                System.out.println("El id de la partida es: " + partidaTerminadaDTO.getIdPartida());
                                System.out.println("El id del retador es: " + partidaTerminadaDTO.getIdRetador());
                                System.out.println("El id del retado es: " + partidaTerminadaDTO.getIdRetado());
                                System.out.println("El puntaje de la partida es: " + partidaTerminadaDTO.getPuntajeJugador1());
                                System.out.println("La fecha de la partida es: " + partidaTerminadaDTO.getFecha());
                            }
                        } else {
                            System.out.println("no se pudo consultar las partidas del jugador...");
                        }
                        break;
                    case 3:
                        System.out.println("Salir...");
                        break;
                    default:
                        System.out.println("No se dijito alguna de la opciones disponibles");
                        break;
                }
            } while (opcion != 3);
        } catch (RemoteException e) {
            System.out.println("La operacion no se pudo completar, intente nuevamente..." + e.getMessage());
        }
    }

    private static void mostrarMenuPrincipalAdmin() {
        try {
            int opcion;

            do {
                System.out.println("==Menu Principal==");
                System.out.println("1. Registrar Usuario");
                System.out.println("2. Consultar Usuario");
                System.out.println("3. Eliminar Usuario");
                System.out.println("4. Editar Usuario");
                System.out.println("5. Salir");

                opcion = UtilidadesConsola.leerEntero();
                switch (opcion) {
                    case 1:
                        System.out.println("==Registro de Usuario==");
                        System.out.println("Ingrese la identificacion");
                        int idR = UtilidadesConsola.leerEntero();
                        System.out.println("Ingrese el nombre completo");
                        String nombreCompletoR = UtilidadesConsola.leerCadena();
                        System.out.println("Ingrese el usuario ");
                        String usuarioR = UtilidadesConsola.leerCadena();
                        System.out.println("Ingrese la clave ");
                        String claveR = UtilidadesConsola.leerCadena();
                        UsuarioDTO objUsuarioR = new UsuarioDTO(idR, nombreCompletoR, usuarioR, claveR);
                        boolean valor1 = objRemotoUsuarios.registrarUsuario(objUsuarioR);// invocación al método remoto
                        if (valor1) {
                            System.out.println("registro realizado satisfactoriamente...");
                        } else {
                            System.out.println("no se pudo realizar el registro del usuario...");
                        }
                        break;

                    case 2:
                        System.out.println("==Consulta de Usuario==");
                        System.out.println("Ingrese la identificacion");
                        int idC = UtilidadesConsola.leerEntero();
                        UsuarioDTO objUsuarioC = objRemotoUsuarios.consultarUsuario(idC);
                        if (objUsuarioC != null) {
                            System.out.println("El id de el usuario es: " + objUsuarioC.getId());
                            System.out.println(
                                    "El nombre completo de el usuario es: " + objUsuarioC.getNombreCompeto());
                            System.out.println("El usuario de el usuario es: " + objUsuarioC.getUsuario());
                            System.out.println("La clave de el usuario es: " + objUsuarioC.getClave());
                        } else {
                            System.out.println("no se pudo realizar la consulta del usuario...");
                        }
                        break;
                    case 3:
                        System.out.println("==Eiliminar de Usuario==");
                        System.out.println("Ingrese la identificacion");
                        int idE = UtilidadesConsola.leerEntero();
                        boolean objUsuarioE = objRemotoUsuarios.eliminarUsuario(idE);
                        if (objUsuarioE != false) {
                            System.out.println("El usuario de id: " + idE +
                                    "ha sido eliminado eliminado satisfactoriamente");

                        } else {
                            System.out.println("no se pudo eliminar el usuario...");
                        }
                        break;
                    case 4:
                        System.out.println("==Editar de Usuario==");
                        System.out.println("Ingrese la identificacion");
                        int idED = UtilidadesConsola.leerEntero();
                        UsuarioDTO objUsuarioidED = objRemotoUsuarios.consultarUsuario(idED);
                        if (objUsuarioidED != null) {

                            System.out.println("==Datos Actuales");
                            System.out.println("El id de el usuario es: " + objUsuarioidED.getId());
                            System.out.println(
                                    "El nombre completo de el usuario es: " + objUsuarioidED.getNombreCompeto());
                            System.out.println("El usuario de el usuario es: " + objUsuarioidED.getUsuario());
                            System.out.println("La clave de el usuario es: " + objUsuarioidED.getClave());

                            System.out.println("==Ingresa los nuevo datos==");
                            System.out.println("Ingrese el nuevo nombre completo del cliente: ");
                            String nombreC = UtilidadesConsola.leerCadena();
                            System.out.println("Ingresa el nuevo usuario del cliente: ");
                            String usuarioE = UtilidadesConsola.leerCadena();
                            System.out.println("Ingrese la nueva clave del cliente");
                            String claveE = UtilidadesConsola.leerCadena();

                            UsuarioDTO newObjUsuario = new UsuarioDTO(idED, nombreC, usuarioE, claveE);

                            boolean objUsuarioEDB = objRemotoUsuarios.editarUsuario(newObjUsuario);
                            if (objUsuarioEDB) {
                                System.out.println("El usuario se ha editado correctamente");
                            } else {
                                System.out.println("El usuario no se ha podido editar");
                            }

                        } else {
                            System.out.println("no se pudo encontrar el usuario a editar...");
                        }
                        break;
                    case 5:
                        System.out.println("Salir...");
                        break;
                    default:
                        System.out.println("Opción incorrecta");
                        break;
                }

            } while (opcion != 5);
        } catch (RemoteException e) {
            System.out.println("La operacion no se pudo completar, intente nuevamente...");
        }
    }
}
