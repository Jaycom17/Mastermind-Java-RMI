package sregistro.sop_rmi;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import sjuego.dto.PartidaTerminadaDTO;

public class RegistroJuegoImpl extends UnicastRemoteObject implements RegistroJuegoInt{

    private String ARCHIVO = "datos.txt";

    public RegistroJuegoImpl() throws RemoteException{
        super();
    }

    private boolean verificaraArchivo(){
        File archivo = new File(ARCHIVO);

        // Verifica si el archivo existe.
        if (archivo.exists()) {
            return true;
        }

        try {

            // Abre el archivo en modo de escritura.
            FileWriter fileWriter = new FileWriter(archivo, true); // Utiliza "true" para agregar al final del archivo.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribe la primera línea al archivo.
            bufferedWriter.write("idPartida, idJugador1, idJugador2, puntajeJugador1, puntajeJugador2, fecha");
            bufferedWriter.newLine(); // Agrega un salto de línea al final.

            // Cierra el archivo.
            bufferedWriter.close();

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private ArrayList<PartidaTerminadaDTO> getPartidas(){
        ArrayList<PartidaTerminadaDTO> partidas = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(ARCHIVO);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            boolean primeraLinea = true;

            while ((linea = bufferedReader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                } else {
                    String[] datos  = linea.split(",");
                    partidas.add(new PartidaTerminadaDTO(Integer.parseInt(datos[0]), Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), Integer.parseInt(datos[3]), Integer.parseInt(datos[4]), datos[5]));
                }
            }

            bufferedReader.close();

            return partidas;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean guardarPartida(PartidaTerminadaDTO partidaTerminada){
        String contenido = partidaTerminada.getIdPartida()+","+partidaTerminada.getIdRetado()+","+ partidaTerminada.getIdRetador()+"," + partidaTerminada.getPuntajeJugador1()+","+partidaTerminada.getPuntajeJugador2()+","+ partidaTerminada.getFecha();

        if(!verificaraArchivo()){
            return false;
        }

        try {
            // Especifica la ruta del archivo donde deseas escribir el contenido.
            FileWriter archivo = new FileWriter(ARCHIVO);

            BufferedWriter bufferedWriter = new BufferedWriter(archivo);

            // Escribe la nueva línea al final del archivo.
            bufferedWriter.write(contenido);
            bufferedWriter.newLine(); // Agrega un salto de línea al final.

            // Cierra el archivo.
            bufferedWriter.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean registrarJuego(PartidaTerminadaDTO partidaTerminada) throws RemoteException{
        return guardarPartida(partidaTerminada);
    }

    @Override
    public ArrayList<PartidaTerminadaDTO> buscarPartida(int idPartida) throws RemoteException{
        ArrayList<PartidaTerminadaDTO> partidas = getPartidas();

        ArrayList<PartidaTerminadaDTO> partidasEncontradas = new ArrayList<>();

        for (PartidaTerminadaDTO partida : partidas) {
            if (partida.getIdPartida() == idPartida) {
                partidasEncontradas.add(partida);
            }
        }

        return partidasEncontradas;
    }

    @Override
    public int getUltimoIdPartida() throws RemoteException{
        ArrayList<PartidaTerminadaDTO> partidas = getPartidas();

        int mayorId = 0;

        for (PartidaTerminadaDTO partida : partidas) {
            if (partida.getIdPartida() > mayorId) {
                mayorId = partida.getIdPartida();
            }
        }

        return mayorId;
    }

}
