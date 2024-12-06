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
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.core.*;
import jade.core.Runtime;

public class Servidor extends Conexion implements Runnable {
    private InterfazIngeniero interfazIn;
    private InterfazMecanico interfazMec;
    

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
//Creamos los agentes
try {
    Runtime rt = Runtime.instance();
    Profile p = new ProfileImpl();
    AgentContainer mainContainer = rt.createMainContainer(p);
    
   
    //le pasamos el argumento de la interfaz InterfazMecanico
    Object[] args = new Object[1];
    args[0] = this.interfazMec;
    
    AgentController Mecanico = mainContainer.createNewAgent("Mecanico", "Agentes.mecanico", args);
    
       
    Mecanico.start();
    AgentController Ingeniero = mainContainer.createNewAgent("Ingeniero", "Agentes.Ingeniero_pista", null);
    //le pasamos el argumento de la interfaz

    Ingeniero.start();
    
    } catch (Exception e) {
    }

                // Esperar a que el cliente conteste el mensaje
                DataInputStream in = new DataInputStream(cs.getInputStream());


            String mensaje;
            while ((mensaje = in.readUTF()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);

                if(mensaje.equals("cambio de llantas")){
                    
                interfazIn.agregarMensajePiloto(mensaje);
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

       
        InterfazMecanico interfazMec = new InterfazMecanico();
        interfazMec.setVisible(true);
        InterfazIngeniero interfazIng = new InterfazIngeniero(this.salidaCliente);
        interfazIng.setVisible(true);

        interfazMec.setInterfazIngeniero(interfazIng);

        interfazIng.setInterfazMecanico(interfazMec);

        this.interfazIn = interfazIng;
        this.interfazMec = interfazMec;

       
    }

    public void IngRun(){

    }
}