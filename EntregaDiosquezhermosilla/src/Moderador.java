import com.sun.jdi.event.ExceptionEvent;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.security.*;

public class Moderador
{
    public static void main(String[] args)
    {
        try {
            Socket socket = new Socket("192.168.1.7", 50000);
            PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            // --- Paso 1: recibir la clave pública del servidor (en texto Base64)
            String clavePublicaBase64 = entradaServidor.readLine();
            byte[] bytesClavePublica = Base64.getDecoder().decode(clavePublicaBase64);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey clavePublicaServidor = keyFactory.generatePublic(new X509EncodedKeySpec(bytesClavePublica));

            // --- Paso 2: generar la clave AES (simétrica)
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey claveAES = keyGen.generateKey();

            // --- Paso 3: cifrar la clave AES con la clave pública del servidor (RSA)
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, clavePublicaServidor);
            byte[] claveAESCifrada = rsaCipher.doFinal(claveAES.getEncoded());

            // --- Paso 4: enviar la clave AES cifrada al servidor (en Base64)
            salidaServidor.println(Base64.getEncoder().encodeToString(claveAESCifrada));

            System.out.println("✅ Clave AES enviada al servidor correctamente.");

            // --- Paso 5: ahora recibir mensajes cifrados del servidor
            String mensajeCifradoBase64;
            while ((mensajeCifradoBase64 = entradaServidor.readLine()) != null) {
                // Descifrar el mensaje con AES

                Cipher aesDescifrar = Cipher.getInstance("AES");
                aesDescifrar.init(Cipher.DECRYPT_MODE, claveAES);
                byte[] mensajeBytes = Base64.getDecoder().decode(mensajeCifradoBase64);
                String mensajeDescifrado = new String(aesDescifrar.doFinal(mensajeBytes));

                System.out.println(" Mensaje recibido: " + mensajeDescifrado);
                System.out.print("¿Aprobar mensaje? (si/no): ");
                String decision = consola.readLine();

                // envia al servidor la decicion que tomo el moderador
                String respuesta;
                if (decision.equalsIgnoreCase("si")) {
                    respuesta = "APROBADO";
                } else {
                    respuesta = "RECHAZADO";
                }

                // Cifrar respuesta
                Cipher aesCifrar = Cipher.getInstance("AES");
                aesCifrar.init(Cipher.ENCRYPT_MODE, claveAES);
                byte[] respuestaCifrada = aesCifrar.doFinal(respuesta.getBytes());
                String respuestaCifradaBase64 = Base64.getEncoder().encodeToString(respuestaCifrada);

                // Enviar al servidor
                salidaServidor.println(respuestaCifradaBase64);
            }
        }
        catch (IOException | InvalidKeySpecException e)
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
}
