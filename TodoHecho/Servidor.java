package MirkoMaxi;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private ServerSocket serverSocket;
    private Socket moderadorSocket;
    private List<Socket> clientes = new ArrayList<>();

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en puerto " + puerto);
    }

    public void esperarModerador() throws IOException {
        System.out.println("Esperando conexión del moderador...");
        moderadorSocket = serverSocket.accept();
        System.out.println("Moderador conectado.");
    }

    public void esperarClientes() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket cliente = serverSocket.accept();
                    clientes.add(cliente);
                    System.out.println("Cliente conectado: " + cliente.getInetAddress());
                    manejarCliente(cliente);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void manejarCliente(Socket cliente) {
        new Thread(() -> {
            try {
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    System.out.println("Mensaje recibido del cliente: " + mensaje);

                    // 1. Enviar mensaje al moderador
                    PrintWriter salidaMod = new PrintWriter(moderadorSocket.getOutputStream(), true);
                    BufferedReader entradaMod = new BufferedReader(new InputStreamReader(moderadorSocket.getInputStream()));

                    salidaMod.println(mensaje);

                    // 2. Esperar respuesta del moderador
                    String respuesta = entradaMod.readLine();

                    if ("APROBADO".equalsIgnoreCase(respuesta)) {
                        // 3. Enviar mensaje a todos los clientes
                        for (Socket c : clientes) {
                            PrintWriter out = new PrintWriter(c.getOutputStream(), true);
                            out.println("Mensaje del chat: " + mensaje);
                        }
                    } else {
                        // 4. Avisar al cliente que fue rechazado
                        salida.println("Tu mensaje fue rechazado por el moderador.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Servidor servidor = new Servidor(1234);
        servidor.esperarModerador();
        servidor.esperarClientes();
    }
}
