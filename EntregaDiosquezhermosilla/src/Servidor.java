import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor
{
    private ServerSocket serverSocketClientes;
    private ServerSocket serverSocketModerador;
    private Socket moderadorSocket;
    private ArrayList<Socket> clientes = new ArrayList<>();
    private final Object lockModerador = new Object();
    private Map<Socket, String> nombresClientes = new HashMap<>();
    private PrintWriter salidaModerador;
    private BufferedReader entradaModerador;

    public Servidor(int puertoModerador, int puertoClientes) throws IOException
    {
        serverSocketModerador = new ServerSocket(puertoModerador);
        serverSocketClientes = new ServerSocket(puertoClientes);
        System.out.println("Servidor iniciado en puerto moderador: " + puertoModerador);
        System.out.println("Servidor iniciado en puerto clientes: " + puertoClientes);
    }

    public void esperarModerador() throws IOException
    {
        System.out.println("Esperando conexión del moderador...");
        moderadorSocket = serverSocketModerador.accept();
        System.out.println("Moderador conectado.");

        salidaModerador = new PrintWriter(moderadorSocket.getOutputStream(), true);
        entradaModerador = new BufferedReader(new InputStreamReader(moderadorSocket.getInputStream()));
    }

    public void esperarClientes()
    {
        new Thread(() ->
        {
            while (true)
            {
                try
                {
                    Socket cliente = serverSocketClientes.accept();
                    clientes.add(cliente);

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);

                    // lee el nombre del cliente
                    String nombre = entrada.readLine();
                    nombresClientes.put(cliente, nombre);
                    System.out.println("Cliente conectado: " + cliente.getInetAddress() + " como " + nombre);

                    manejarCliente(cliente, entrada, salida, nombre);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void manejarCliente(Socket cliente, BufferedReader entrada, PrintWriter salida, String nombre)
    {
        new Thread(() ->
        {
            try
            {
                String mensaje;
                while ((mensaje = entrada.readLine()) != null)
                {
                    String mensajeCompleto = nombre + ": " + mensaje;

                    String respuesta;
                    synchronized (lockModerador) // bloquea al moderador para que solo 1 cliente le envia informacion al mismo tiempo.
                    {
                        salidaModerador.println(mensajeCompleto);
                        respuesta = entradaModerador.readLine();  // espera la respuesta del moderador
                    }

                    if ("APROBADO".equalsIgnoreCase(respuesta))
                    {
                        System.out.println(mensajeCompleto);
                        salida.println("ENVIADO");  // se envio el mensaje correctamente
                    }
                    else
                    {
                        salida.println("RECHAZADO");
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException
    {
        Servidor servidor = new Servidor(50000, 50001);
        // atiende al moderador y al cliente en dos puertos distintos, el 50000 para el moderador y el 50001 para el cliente
        servidor.esperarModerador();

        servidor.esperarClientes();
    }
}
