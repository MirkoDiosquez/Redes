import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 50000); //"192.168.1.7"
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            // Thread para escuchar mensajes del servidor
            new Thread(() -> {
                try {
                    String mensajeServidor;
                    while ((mensajeServidor = entrada.readLine()) != null) {
                        System.out.println(mensajeServidor);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String mensajeUsuario;
            while ((mensajeUsuario = consola.readLine()) != null) {
                salida.println(mensajeUsuario);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
