import javax.crypto.*;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Cliente
{
    public static void main(String[] args)
    {
        try {
            Socket socket = new Socket("192.168.1.7", 50001);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));



            // Pasarlo a metodo
            // Este metodo se llamaria conectarClavePublicaDelServidorConCliente ---> Obviamente no pero para entender como funciona esto
            // Recibimos la clave publica del Servidor


            String clavePublicaEnTexto = entrada.readLine(); // Esta es la clave pública recibida pero en texto, lo cual la computadora no entiende
            byte[] bytesClavePublica = Base64.getDecoder().decode(clavePublicaEnTexto); // Aca almacenamos los datos binarios originales  { MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A -> 0101010101110101010 }
            KeyFactory crearLlaves = KeyFactory.getInstance("RSA"); // Este metodo sabe cómo transformar bytes en objetos PublicKey o PrivateKey.
            PublicKey clavePublicaDelServidor = crearLlaves.generatePublic(new X509EncodedKeySpec(bytesClavePublica));


            // ACA TENEMOS COMPLETAMENTE ALINEADO LA CLAVE PUBLICA QUE SE GENERO EN EL SERVIDOR PERO EN EL CLIENTE.



            //  Creamos la clave aleatoria, la cual se va a utilizar para cifrar y descifrar las cosas.

            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey claveAES_ServidorCLiente = keyGen.generateKey();

            //  Despues la encriptamos Asimetricamente para hacerla llegar al servidor

            Cipher rsaCipher = Cipher.getInstance("RSA"); // es la herramienta de Java para cifrar o descifrar datos con distintos algoritmos (RSA, AES, etc).
            rsaCipher.init(Cipher.ENCRYPT_MODE, clavePublicaDelServidor); // Esto dice que todo lo que yo le pase por parametro, se encriptará con la clave publica del servidor
            byte[] claveAESCifrada = rsaCipher.doFinal(claveAES_ServidorCLiente.getEncoded()); // Aca encripta, lo pasa a Bytes
            String claveAESCifradaBase64 = Base64.getEncoder().encodeToString(claveAESCifrada); // Aca lo pasa a texto



            salida.println(claveAESCifradaBase64);




            System.out.print("Ingresa tu nombre: ");
            String nombre = consola.readLine();
            salida.println(nombre); 
            final boolean[] esperandoRespuesta = {false};

            new Thread(() -> // hilo para recibir mensajes del servidor
            {
                try
                {
                    String mensajeServidor;
                    while ((mensajeServidor = entrada.readLine()) != null)
                    {
                        if (mensajeServidor.equals("ENVIADO"))
                        {
                            System.out.println("✅ Se envió el mensaje.");
                        }
                        else if (mensajeServidor.equals("RECHAZADO"))
                        {
                            System.out.println("❌ Tu mensaje fue rechazado por el moderador.");
                        }
                        else
                        {
                            System.out.println(mensajeServidor);
                        }
                        esperandoRespuesta[0] = false;
                        System.out.print("Escribe el mensaje que desea enviar: ");
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }).start();

            System.out.print("Escribe el mensaje que desea enviar: ");

            while (true)
            {
                while (esperandoRespuesta[0])
                {
                    Thread.sleep(100);
                }
                String mensajeUsuario = consola.readLine();
                if (mensajeUsuario != null && !mensajeUsuario.isEmpty())
                {
                    salida.println(mensajeUsuario);
                    esperandoRespuesta[0] = true;
                }
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)  {
            // Tira el error porque está comentado
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
