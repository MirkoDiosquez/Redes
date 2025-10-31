import com.sun.jdi.event.ExceptionEvent;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;

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
    private PublicKey clavePublicaServidor;
    private PrivateKey clavePrivadaServidor;




    public Servidor(int puertoModerador, int puertoClientes) throws IOException
    {
        serverSocketModerador = new ServerSocket(puertoModerador);
        serverSocketClientes = new ServerSocket(puertoClientes);
        System.out.println("Servidor iniciado en puerto moderador: " + puertoModerador);
        System.out.println("Servidor iniciado en puerto clientes: " + puertoClientes);
    }


    /*  El servidor aca genera las claves Publicas y Privadas para poder comunicarse
        con los clientes.
    */

    public void generarClaves_Pub_Priv() throws Exception{
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA"); // Definimos que utilizamos Encriptacion Asimetrica
            generator.initialize(2050);
            KeyPair parClaves = generator.generateKeyPair();
            clavePublicaServidor = parClaves.getPublic();
            clavePrivadaServidor = parClaves.getPrivate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    private String getClavePublica() {
        return Base64.getEncoder().encodeToString(clavePublicaServidor.getEncoded());
        /* Es como el ejemplo que puso pruchi, pasa a texto todos los bytes binarios que se generaron
        Ej : 0101010101110101010  ->  MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A
    * */
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

                    // le enviamos la clave Publica al cliente
                    salida.println(getClavePublica());

                    // Recibir la clave AES cifrada (en texto Base64)
                    String claveAESCifradaBase64 = entrada.readLine();
                    byte[] claveAESCifrada = Base64.getDecoder().decode(claveAESCifradaBase64); // clave en bytes


                    // Descifrar la clave AES usando la clave privada del servidor
                    Cipher rsaCipher = Cipher.getInstance("RSA");
                    rsaCipher.init(Cipher.DECRYPT_MODE, clavePrivadaServidor); // desencripta con clave privada
                    byte[] claveAESBytes = rsaCipher.doFinal(claveAESCifrada);

                    // Reconstruir la clave AES a partir de los bytes originales
                    SecretKey claveAESCliente = new SecretKeySpec(claveAESBytes, 0, claveAESBytes.length, "AES");

                    // lee el nombre del cliente
                    String nombre = entrada.readLine();
                    nombresClientes.put(cliente, nombre);
                    System.out.println("Cliente conectado: " + cliente.getInetAddress() + " como " + nombre);

                    manejarCliente(cliente, entrada, salida, nombre);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
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

    public static void main(String[] args) throws Exception
    {
        Servidor servidor = new Servidor(50000, 50001);

        servidor.generarClaves_Pub_Priv();



        // atiende al moderador y al cliente en dos puertos distintos, el 50000 para el moderador y el 50001 para el cliente
        servidor.esperarModerador();

        servidor.esperarClientes();
    }
}
