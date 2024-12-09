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
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
    private ObjectOutputStream out;

    public void setInterfazIngeniero(InterfazIngeniero interfazIn) {
        this.interfazIn = interfazIn;
    }

    
    

    public Servidor() throws IOException {
        super("servidor");
    }

    

    public void startServer() throws ClassNotFoundException {
        try {
            System.out.println("Esperando...");

            while (true) {
                cs = ss.accept();
                System.out.println("Cliente en línea");

                


                salidaServidor = new DataOutputStream(cs.getOutputStream()); 
                new Thread(this).run();
//Creamos los agentes
try {
    Runtime rt = Runtime.instance();
    Profile p = new ProfileImpl();
    AgentContainer mainContainer = rt.createMainContainer(p);
    
    
   
    //le pasamos el argumento de la interfaz InterfazMecanico
    Object[] args = new Object[1];
    args[0] = this.interfazMec;
    
    AgentController Mecanico = mainContainer.createNewAgent("Mecanico", "Agentes.mecanico", args);
    
       
   
    //le pasamos el argumento de la interfaz

    //Inyectamos los agentes en las interfaces
    this.interfazMec.setMecanico(Mecanico);
    Mecanico.start();
    this.interfazIn.setMainContainer(mainContainer);
    
    } catch (Exception e) {
    }
        
                // Esperar a que el cliente conteste el mensaje
                DataInputStream in = new DataInputStream(cs.getInputStream());

                


            String mensaje;
            while ((mensaje = in.readUTF()) != null) {

                if(mensaje.contains("desgaste")){
                    this.interfazIn.actualizarDesgasteNeumaticos(mensaje);
                    //Obtener el desgaste de los neumáticos
                    double desgaste = Double.parseDouble(mensaje.split(" ")[1]);
                    this.interfazIn.setDesgasteNeumaticos(desgaste);
                }else{

                    if(mensaje.equals("comienzo")){
                        this.interfazIn.iniciarMonitoreoDesgasteLlantas();
                        interfazIn.agregarMensajePiloto("Comenzó la carrera");
                        interfazIn.desbloqueoPits();
                       
                    } 
                    
                
                }
                if(mensaje.contains("cambio")){
                    interfazIn.agregarMensajePiloto(mensaje);
                }

               

                if(mensaje.contains("Vueltas restantes:")){
                    interfazIn.setVueltasRestantes(mensaje);
                }

                if(mensaje.contains("Vuelta ")){
                    interfazIn.agregarMensajePiloto(mensaje);
                }
                
                if(mensaje.contains("boxes")){
                    interfazIn.agregarMensajePiloto(mensaje);
                }

                if(mensaje.equals("Saliendo de Pitstop")){
                    interfazIn.agregarMensajePiloto(mensaje);
                    interfazIn.desbloqueoPits();
                }

                if(mensaje.equals("Entrando a Pitstop")){
                    interfazIn.agregarMensajePiloto(mensaje);

                    interfazIn.cambioLlantas();

                    interfazMec.Pits();

                    
                }

                if (mensaje.equals("Saliendo de Pits")){
                    interfazIn.agregarMensajePiloto(mensaje);
                    interfazIn.desbloqueoPits();
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
        InterfazIngeniero interfazIng;
        try {
            interfazIng = new InterfazIngeniero(this.salidaServidor);
            interfazIng.setVisible(true);

        interfazMec.setInterfazIngeniero(interfazIng);

        interfazIng.setInterfazMecanico(interfazMec);

        this.interfazIn = interfazIng;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.interfazMec = interfazMec;

       
    }

    public void IngRun(){

    }
}