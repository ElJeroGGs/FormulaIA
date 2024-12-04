package Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Eduardo
 */

public class Conexion
{
    private final int PUERTO = 8001; // Puerto para la conexión
    private final String HOST = "192.168.1.72"; // Host para ;a conexión
    protected ServerSocket ss; // Socket del servidor
    protected Socket cs; // Socket del cliente
    protected DataOutputStream salidaServidor, salidaCliente; // Flujo de datos de salida

    public Conexion(String tipo) throws IOException //Constructor
    {
        if(tipo.equalsIgnoreCase("servidor"))
        {
            ss = new ServerSocket(PUERTO); // Se crea el socket para el servidor en el puerto 8001
            cs = new Socket(); // Socket para el cliente
        }
        else if(tipo.equalsIgnoreCase("cliente"))
        {
            cs = new Socket(HOST, PUERTO); // Socket para el cliente en la dirección y puerto especificados
        } else if(tipo.equalsIgnoreCase("servidor1")){
            ss = new ServerSocket(PUERTO+1);
            cs = new Socket();

        }
    }
}