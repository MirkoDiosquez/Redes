import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor
{
    private ServerSocket serverSocket;
    private Socket moderadorSocket;
    private ArrayList<Socket> clientes = new ArrayList<>();
    private Map<Socket, String> nombresClientes = new HashMap<>();

    private PrintWriter salidaModerador;
    private BufferedReader entradaModerador;

    public Servidor(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en puerto " + puerto);
    }

    public void esperarModerador() throws IOException {
        System.out.println("Esperando conexión del moderador...");
        moderadorSocket = serverSocket.accept();
        System.out.println("Moderador conectado.");

        salidaModerador = new PrintWriter(moderadorSocket.getOutputStream(), true);
        entradaModerador = new BufferedReader(new InputStreamReader(moderadorSocket.getInputStream()));
    }

    public void esperarClientes() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket cliente = serverSocket.accept();
                    clientes.add(cliente);

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);

                    // Leer nombre de usuario al inicio
                    String nombre = entrada.readLine();
                    nombresClientes.put(cliente, nombre);
                    System.out.println("Cliente conectado: " + cliente.getInetAddress() + " como " + nombre);

                    manejarCliente(cliente, entrada, salida, nombre);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void manejarCliente(Socket cliente, BufferedReader entrada, PrintWriter salida, String nombre) {
        new Thread(() -> {
            try {
                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    String mensajeCompleto = nombre + ": " + mensaje;

                    // Enviar al moderador
                    salidaModerador.println(mensajeCompleto);

                    // Esperar respuesta del moderador
                    String respuesta = entradaModerador.readLine();

                    if ("APROBADO".equalsIgnoreCase(respuesta)) {
                        // Mostrar en consola del servidor (visible para todos si están usando el servidor)
                        System.out.println(mensajeCompleto);

                        // Confirmar al remitente
                        salida.println("ENVIADO");
                    } else {
                        // Rechazado: solo se notifica al cliente remitente
                        salida.println("RECHAZADO");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException
    {
        Servidor servidor = new Servidor(50000);
        servidor.esperarModerador();
        servidor.esperarClientes();
    }
}

