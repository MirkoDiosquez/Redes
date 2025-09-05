import java.io.*;
import java.net.*;

public class Moderador {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 50000); //"192.168.1.7"

            PrintWriter salidaServidor =  new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            String mensaje = entradaServidor.readLine() ;
            String decisionSiOno = "" ;
            while (mensaje!= null)
            {
                System.out.println("Mensaje recibido para moderar: " + mensaje);
                System.out.print("¿Aprobar mensaje? (sí/no): ");
                decisionSiOno = consola.readLine();

                if ("si".equalsIgnoreCase(decisionSiOno)) {
                    salidaServidor.println("APROBADO");
                } else {
                    salidaServidor.println("RECHAZADO");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

