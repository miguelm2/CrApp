package com.miguelmartinez.crapp.clases;

import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Miguel Martinez on 25/05/2017.
 */

public class Cifrados {

    public Cifrados() {
    }

    public static String GenerarClave() {

        String clavedf = new String();

        for (int i = 0; i < 16; i++) {
            Random r = new Random();
            int num = r.nextInt(90);
            num += 33;
            clavedf += (char) num;

        }

        return clavedf;
    }

    //-----------------------
    // CIFRADO AES 256
    //-----------------------

    //-----------------------
    // CIFRADO CESAR
    //-----------------------
    public static String Cifrar_Cesar(String mensaje, int adelantar) {
        //Texto a salir (cfrado)
        String cipher = "";
        //Posiciones a adelantar

        //Convertimos el mansaje en un array de caracteres
        char[] letras = mensaje.toCharArray();

        //Vamos por cada caracter sumandole 3
        for (int i = 0; i < letras.length; i++) {

            // de esta manera obtenemos el codigo ascii del caracter
            //  ((int) letras[i]) y luego a ese numero le sumamos 3
            // ( ((int) letras[i])+ adelantar) <- quedaria asi
            // y luego convertimos ese numero en la letra a la que hace
            // referencia en el codigo ascii solo casteando el numero a (char)
            cipher += (char) (((int) letras[i]) + adelantar);
        }
        //Texto cifrado
        return cipher;
    }
    //-----------------------
    // DESCIFRADO AES 256
    //-----------------------

    //-----------------------
    // DESCIFRADO CESAR
    //-----------------------
    public static String Descifrar_Cesar(String cipher, int adelantar) {

        String mensaje = "";
        //Caracteres del mensaje
        char[] letras = cipher.toCharArray();
        for (int i = 0; i < letras.length; i++) {
            mensaje += (char) (((int) letras[i]) - adelantar);
        }
        return mensaje;
    }

    public static String CompuertaXOR(String binario1, String binario2) {

        char[] operando1 = binario1.toCharArray();
        char[] operando2 = binario2.toCharArray();
        char[] r = new char[operando1.length];

        for (int i = 0; i < operando1.length; i++) {


            if (operando1[i] != operando2[i]) {
                r[i] = '1';
            } else {
                r[i] = '0';
            }
        }

        String re = "";
        for (int j = 0; j < r.length; j++) {
            re += r[j];
        }

        return re;
    }

    public static String Cifrado_Descifrado_XOR(String mensajes, String claves) {

        String[] mensaje = TextoABinario(mensajes);
        String[] clave = TextoABinario(claves);

        String[] resultado = new String[mensaje.length];
        int puntero = 0;

        for (int i = 0; i < mensaje.length; i++) {

            if (puntero >= clave.length) {
                puntero = 0;
            }
            resultado[i] = CompuertaXOR(mensaje[i], clave[puntero]);

            puntero++;
        }
        String salida = BinarioATexto(resultado);
        return salida;
    }

    public static String[] TextoABinario(String texto) {

        char caracter;
        int codigoASCII;
        String binario;
        String[] binarios = new String[texto.length()];

        for (int i = 0; i < texto.length(); i++) {

            caracter = texto.charAt(i);
            codigoASCII = (int) caracter;

            binario = "";


            for (int j = 7; j >= 0; j--) {
                if (codigoASCII >= Math.pow(2, j)) {
                    codigoASCII -= Math.pow(2, j);
                    binario += "1";
                } else {
                    binario += "0";
                }
            }

            binarios[i] = binario.toString();


        }
        return binarios;
    }

    public static String BinarioATexto(String[] binarios) {
        String texto = "";

        for (int j = 0; j < binarios.length; j++) {
            char[] bin = binarios[j].toCharArray();

            int decimal = 0;

            int contador = 0;
            for (int i = bin.length - 1; i > -1; i--) {

                if (bin[contador] == '1') {
                    decimal += Math.pow(2, i);
                }

                contador++;
            }

            texto += (char) decimal;

        }

        return texto;
    }

    public void AESencriptar(String clave, String direccion, String nombre, String carpetaPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        //Archivo de entrada

        System.out.println("Direccion del archivo a cifrar : " + direccion + nombre);
        System.out.println("SD : " + Environment.getExternalStorageDirectory());
        FileInputStream Entrada = new FileInputStream(direccion + nombre);

        System.out.println("es un archivo: " + Entrada.available());
        System.out.println("\n cifrando");

        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        OutputStream Salida = new FileOutputStream(carpetaPath + "/" + nombre);
        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec(clave.getBytes(), "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(Salida, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = Entrada.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        Entrada.close();

        //

    }


    //-----------------------
    // HASH MD5
    //-----------------------

    public void AESdesencriptar(String clave, String direccion, String nombre, String carpetaPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        FileInputStream Entrada = new FileInputStream(direccion + nombre);

        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        System.out.println("\n Descifrando");
        FileOutputStream Salida = new FileOutputStream(carpetaPath + "/decrypt_" + nombre);
        SecretKeySpec sks = new SecretKeySpec(clave.getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(Entrada, cipher);

        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            Salida.write(d, 0, b);
        }
        Salida.flush();
        Salida.close();
        cis.close();


    }

    //-----------------------
    // CIFRADO 3 DES
    //-----------------------
    public void TripleDesEncrypt(String clave, String direccion, String nombre, String carpetaPath)
            throws NoSuchAlgorithmException, InvalidKeyException,

            NoSuchPaddingException, IOException {


        FileInputStream in = new FileInputStream(direccion + nombre);

        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        System.out.println("\n Descifrando");

        FileOutputStream out = new FileOutputStream(carpetaPath + "/" + nombre);
        // Create and initialize the encryption engine


        Cipher cipher = Cipher.getInstance("DESede");
        SecretKeySpec key = new SecretKeySpec(clave.getBytes(),
                "DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Create a special output stream to do the work for us
        CipherOutputStream cos = new CipherOutputStream(out, cipher);

        // Read from the input and write to the encrypting output stream
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            cos.write(buffer, 0, bytesRead);
        }
        cos.close();

        // For extra security, don't leave any plaintext hanging around memory.
        java.util.Arrays.fill(buffer, (byte) 0);

    }


    //-----------------------
    // CIFRADO XOR
    //-----------------------

    //-----------------------
    // DESCIFRADO 3 DES
    //-----------------------
    public void TipleDesDecrypt(String clave, String direccion, String nombre,String carpetaPath)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException,
            IllegalBlockSizeException, NoSuchPaddingException,
            BadPaddingException {

        FileInputStream in = new FileInputStream(direccion + nombre);

        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        System.out.println("\n Descifrando");

        FileOutputStream Salida = new FileOutputStream(carpetaPath + "/decrypt_" + nombre);


        // Create and initialize the decryption engine
        Cipher cipher = Cipher.getInstance("DESede");
        SecretKeySpec key = new SecretKeySpec(clave.getBytes(),
                "DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Read bytes, decrypt, and write them out.
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            Salida.write(cipher.update(buffer, 0, bytesRead));
        }

        // Write out the final bunch of decrypted bytes
        Salida.write(cipher.doFinal());
        Salida.flush();

    }

    //-----------------------
    // HASH 256
    //-----------------------
    public String SHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();

        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public String MD5(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }


}
