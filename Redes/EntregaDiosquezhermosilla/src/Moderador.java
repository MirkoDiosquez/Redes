import java.io.*;
import java.net.*;

public class Moderador
{
    public static void main(String[] args)
    {
        try {
            Socket socket = new Socket("192.168.1.7", 50000);
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            String mensaje;
            while ((mensaje = entradaServidor.readLine()) != null)
            {
                System.out.println("Mensaje recibido para moderar de: " + mensaje);
                System.out.print("¿Aprobar mensaje? (sí/no): ");
                String decision = consola.readLine();

                // envia al servidor la decicion que tomo el moderador
                if ("si".equalsIgnoreCase(decision))
                {
                    salidaServidor.println("APROBADO");
                }
                else
                {
                    salidaServidor.println("RECHAZADO");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
