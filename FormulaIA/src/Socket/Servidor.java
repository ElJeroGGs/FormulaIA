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
    

    public Servidor() throws IOException {
        super("servidor");
    }

    public Servidor(Socket socket) throws IOException {
        super("servidorhilo");
        this.cs = socket;
    }

    public void startServer() {
        try {
            System.out.println("Esperando...");

            while (true) {
                cs = ss.accept();
                System.out.println("Cliente en línea");

                // Crear un nuevo hilo para manejar la conexión del cliente
                new Thread(new Servidor(cs)).start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        } finally {
            try {
                if (ss != null && !ss.isClosed()) {
                    ss.close();
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el servidor: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
             DataOutputStream out = new DataOutputStream(cs.getOutputStream())) {

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                if (mensaje.equalsIgnoreCase("cambio de llantas")) {
                    System.out.println("Procesando solicitud de cambio de llantas...");
                    // Aquí puedes agregar la lógica para manejar la solicitud de cambio de llantas
                }
                out.writeBytes("Mensaje recibido\n");
            }
        } catch (IOException e) {
            System.out.println("Error en el manejo del cliente: " + e.getMessage());
        } finally {
            try {
                if (cs != null && !cs.isClosed()) {
                    cs.close();
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
    }
}