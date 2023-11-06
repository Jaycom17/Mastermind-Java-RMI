package sjuego.sop_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDate;

import sjuego.dto.PartidaTerminadaDTO;
import sjuego.dto.JugadorDTO;
import sregistro.sop_rmi.RegistroJuegoInt;
import sjuego.utilidades.UtilidadesRegistroC;
import sjuego.dto.PartidaEnCursoDTO;

public class GestionJuegoImpl extends UnicastRemoteObject implements GestionJuegoInt {
    private int idPartida;
    private ArrayList<JugadorDTO> jugadores = new ArrayList<>();
    private CountDownLatch latch = new CountDownLatch(2);

    private RegistroJuegoInt objReferenciaRemota;
    private PartidaEnCursoDTO partidaEnCurso;

    public GestionJuegoImpl() throws RemoteException {
        super();
        this.idPartida = objReferenciaRemota.getUltimoIdPartida()+1;
    }

    private void terminarJuego() {
        partidaEnCurso.cambiarRolJugadores();
        partidaEnCurso.setTurno(false);
        partidaEnCurso.setClave(null);
        partidaEnCurso.setHistorial(new ArrayList<ArrayList<Integer>>());
        partidaEnCurso.setHistorialRespuestas(new ArrayList<ArrayList<Integer>>());
        partidaEnCurso.setRonda(2);
    }

    private void terminarPartida() throws RemoteException{
        partidaEnCurso.terminar();
        jugadores.clear();
        latch = new CountDownLatch(2);

        PartidaTerminadaDTO partidaTerminada = new PartidaTerminadaDTO(idPartida, partidaEnCurso.getJugador1().getId(), partidaEnCurso.getJugador2().getId(), partidaEnCurso.getPuntuacionJugador1(), partidaEnCurso.getPuntuacionJugador2(), LocalDate.now().toString());

        objReferenciaRemota.registrarJuego(partidaTerminada);
    }

    @Override
    public int getIdRetador() throws RemoteException {
        return jugadores.get(0).getId();
    }

    @Override
    public PartidaEnCursoDTO getEstado() throws RemoteException {
        return partidaEnCurso;
    }

    /**
     * 1 si no se puede jugar, 0 si se puede jugar y 2 si hay una partida en curso
     */
    @Override
    public int iniciarJuego(int idJugador) throws RemoteException {

        System.out.println("Iniciar Juego");

        try {
            if (jugadores.size() == 2) {
                return 2;
            }

            if (jugadores.size() == 1 && jugadores.get(0).getId() == idJugador) {
                return 1; // El jugador ya está registrado
            }

            JugadorDTO jugador = new JugadorDTO(idJugador, JugadorDTO.Roles.Retado);

            if (jugadores.size() == 1) {
                jugador.setRol(JugadorDTO.Roles.Retador);
            }

            jugadores.add(jugador);

            latch.countDown();

            // Espera hasta que se registren ambos jugadores
            latch.await();

            if (idJugador != jugadores.get(0).getId()) {
                partidaEnCurso = new PartidaEnCursoDTO(jugadores.get(0), jugadores.get(1), null, 20, 20,
                        new ArrayList<ArrayList<Integer>>(), new ArrayList<ArrayList<Integer>>(), false, false, 0);
                idPartida++;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return 0;
    }

    @Override
    public ArrayList<PartidaTerminadaDTO> buscarPartida(int idJugador) throws RemoteException {
        System.out.println("Buscar Partida");
        return null;
    }

    @Override
    public PartidaEnCursoDTO establecerClave(ArrayList<Integer> clave) throws RemoteException {
        System.out.println("Establecer clave");

        partidaEnCurso.setClave(clave);

        partidaEnCurso.cambiarTurno();

        return partidaEnCurso;
    }

    @Override
    public PartidaEnCursoDTO enviarIntento(ArrayList<Integer> intento) throws RemoteException {
        System.out.println("Enviar intento");
        System.out.println(intento.toString());

        ArrayList<ArrayList<Integer>> nuevoHistorial = partidaEnCurso.getHistorial();

        if (intento.equals(partidaEnCurso.getClave()) && partidaEnCurso.getRonda() == 1) {
            terminarJuego();

            return partidaEnCurso;
        }else if (intento.equals(partidaEnCurso.getClave()) && partidaEnCurso.getRonda() == 2) {
            terminarPartida();

            return partidaEnCurso;
        }

        nuevoHistorial.add(intento);

        partidaEnCurso.setHistorial(nuevoHistorial);

        partidaEnCurso.cambiarTurno();

        descontarRetador(1);

        return partidaEnCurso;
    }

    @Override
    public PartidaEnCursoDTO enviarRespuesta(ArrayList<Integer> respuesta) throws RemoteException {
        System.out.println("Enviar respuesta");

        System.out.println(respuesta.toString());

        ArrayList<Integer> nuevaRespuesta = verificarRespuesta(respuesta,
                partidaEnCurso.getHistorial().get(partidaEnCurso.getHistorial().size() - 1));

        ArrayList<ArrayList<Integer>> nuevoHistorial = partidaEnCurso.getHistorialRespuestas();
        nuevoHistorial.add(nuevaRespuesta);

        partidaEnCurso.setHistorialRespuestas(nuevoHistorial);

        partidaEnCurso.cambiarTurno();

        return partidaEnCurso;
    }

    private void descontarRetador(int descuento) {
        partidaEnCurso.setPuntuacionJugador1(partidaEnCurso.getPuntuacionJugador1()
                - (partidaEnCurso.getJugador1().getRol() == JugadorDTO.Roles.Retador ? descuento : 0));

        partidaEnCurso.setPuntuacionJugador2(partidaEnCurso.getPuntuacionJugador2()
                - (partidaEnCurso.getJugador2().getRol() == JugadorDTO.Roles.Retador ? descuento : 0));
    }

    private ArrayList<Integer> calcularRespuesta(ArrayList<Integer> intento) {
        ArrayList<Integer> respuesta = new ArrayList<>();

        for (int i = 0; i < intento.size(); i++) {
            respuesta.add(intento.get(i) == partidaEnCurso.getClave().get(i) ? 1
                    : partidaEnCurso.getClave().contains(intento.get(i)) ? 0 : -1);
        }

        return respuesta;
    }

    private ArrayList<Integer> verificarRespuesta(ArrayList<Integer> respuesta, ArrayList<Integer> intento) {
        ArrayList<Integer> claveValidada = calcularRespuesta(intento);
        if (respuesta.equals(claveValidada)) {
            return respuesta;
        }

        descontarRetador(2);

        return claveValidada;
    }

    public void consultarReferenciaRemota(String direccionIpRMIRegistry, int numPuertoRMIRegistry) {
        System.out.println("Desde consultarReferenciaRemotaDeNotificacion()...");
        this.objReferenciaRemota = (RegistroJuegoInt) UtilidadesRegistroC.obtenerObjRemoto(direccionIpRMIRegistry,
                numPuertoRMIRegistry, "registroJuego");
    }
}
