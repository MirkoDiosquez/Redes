package MirkoMaxi;

import java.io.*;
import java.net.*;

public class Moderador {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Mensaje recibido para moderar: " + mensaje);
                System.out.print("¿Aprobar mensaje? (sí/no): ");
                String decision = consola.readLine();

                if ("sí".equalsIgnoreCase(decision)) {
                    salida.println("APROBADO");
                } else {
                    salida.println("RECHAZADO");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

