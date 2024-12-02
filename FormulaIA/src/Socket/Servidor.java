package Socket;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Eduardo
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Servidor extends Conexion implements Runnable {
    private Socket clientSocket;

    public Servidor() throws IOException {
        super("servidor");
    }

    // Constructor para manejar la conexión del cliente
    public Servidor(Socket socket) throws IOException {
        super("servidor");
        this.clientSocket = socket;
    }

    public void startServer() {
        try {
            System.out.println("Esperando...");

            while (true) {
                clientSocket = ss.accept();
                System.out.println("Cliente en línea");

                // Crear un nuevo hilo para manejar la conexión del cliente
                new Thread(new Servidor(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                if (mensaje.equalsIgnoreCase("solicitando cambio de llantas")) {
                    System.out.println("Procesando solicitud de cambio de llantas...");
                    // Aquí puedes agregar la lógica para manejar la solicitud de cambio de llantas
                }
                out.writeBytes("Mensaje recibido\n");
            }

            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error en el manejo del cliente: " + e.getMessage());
        }
    }
}