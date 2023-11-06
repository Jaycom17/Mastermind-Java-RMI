package sjuego;

import java.rmi.RemoteException;

import sjuego.utilidades.UtilidadesConsola;
import sjuego.utilidades.UtilidadesRegistroS;
import sjuego.sop_rmi.GestionJuegoImpl;

public class ServidorDeObjetos2 {
    public static void main(String args[]) throws RemoteException {

        int numPuertoRMIRegistry = 0;
        String direccionIpRMIRegistry = "";

        System.out.println("Cual es el la dirección ip donde se encuentra  el rmiregistry ");
        direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
        System.out.println("Cual es el número de puerto por el cual escucha el rmiregistry ");
        numPuertoRMIRegistry = UtilidadesConsola.leerEntero();

        GestionJuegoImpl objRemoto = new GestionJuegoImpl();// se leasigna el puerto de escucha del objeto remoto
        objRemoto.consultarReferenciaRemota(direccionIpRMIRegistry, numPuertoRMIRegistry);

        try {
            UtilidadesRegistroS.arrancarNS(numPuertoRMIRegistry);
            UtilidadesRegistroS.RegistrarObjetoRemoto(objRemoto, direccionIpRMIRegistry, numPuertoRMIRegistry,
                    "gestionJuego");

        } catch (Exception e) {
            System.err.println("No fue posible Arrancar el NS o Registrar el objeto remoto" + e.getMessage());
        }

    }
}
