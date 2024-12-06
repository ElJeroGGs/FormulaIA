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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import Interfaz.InterfazIngeniero;
import Interfaz.InterfazMecanico;

public class Servidor extends Conexion implements Runnable {
    private InterfazIngeniero interfazIng;
    

    public Servidor() throws IOException {
        super("servidor");
    }

    

    public void startServer() {
        try {
            System.out.println("Esperando...");

            while (true) {
                cs = ss.accept();
                System.out.println("Cliente en línea");

                salidaServidor = new DataOutputStream(cs.getOutputStream());
                new Thread(this).start();
                // Esperar a que el cliente conteste el mensaje
                DataInputStream in = new DataInputStream(cs.getInputStream());


            String mensaje;
            while ((mensaje = in.readUTF()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);

                if(mensaje.equals("cambio de llantas")){
                    
                interfazIng.agregarMensajePiloto(mensaje);
                }
            
                
            }

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

        InterfazIngeniero interfazIng = new InterfazIngeniero(this.salidaCliente);
        interfazIng.setVisible(true);
        InterfazMecanico interfazMec = new InterfazMecanico(interfazIng);
        interfazMec.setVisible(true);
        interfazIng.setInterfazMecanico(interfazMec);
        this.interfazIng = interfazIng;

       
    }
}