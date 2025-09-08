import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.1.7", 50000);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            // Pedir el nombre/nick al iniciar (solo una vez)
            System.out.print("Ingresa tu nick: ");
            String nombre = consola.readLine();
            salida.println(nombre); // Enviar nick al servidor

            final boolean[] esperandoRespuesta = {false};

            // Hilo para recibir mensajes del servidor
            new Thread(() -> {
                try {
                    String mensajeServidor;
                    while ((mensajeServidor = entrada.readLine()) != null) {
                        if (mensajeServidor.equals("ENVIADO")) {
                            System.out.println("✅ Se envió el mensaje.");
                        } else if (mensajeServidor.equals("RECHAZADO")) {
                            System.out.println("❌ Tu mensaje fue rechazado por el moderador.");
                        } else {
                            System.out.println(mensajeServidor);
                        }

                        esperandoRespuesta[0] = false;
                        System.out.print("Escribe el mensaje que desea enviar: ");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.print("Escribe el mensaje que desea enviar: ");

            while (true) {
                while (esperandoRespuesta[0]) {
                    Thread.sleep(100);
                }

                String mensajeUsuario = consola.readLine();

                if (mensajeUsuario != null && !mensajeUsuario.isEmpty()) {
                    salida.println(mensajeUsuario);
                    esperandoRespuesta[0] = true;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
