package Socket;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.DataInputStream;
/**
 *
 * @author Eduardo
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import Agentes.piloto;
import Interfaz.InterfazPiloto;
import jade.domain.df;

public class Cliente extends Conexion implements Runnable {

    private InterfazPiloto interfazPiloto;


    public void setInterfazPiloto(InterfazPiloto interfazPiloto) {
        this.interfazPiloto = interfazPiloto;
    }

    public Cliente() throws IOException {
        super("cliente");
    }

    public void startClient() {
        try {
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            salidaCliente = new DataOutputStream(cs.getOutputStream());
            new Thread(this).start();
            

            // Esperar a que el servidor conteste el mensaje
            DataInputStream in = new DataInputStream(cs.getInputStream());

            String mensaje;
            while ((mensaje = in.readUTF()) != null) {
                
                if(mensaje.equals("desgaste")){
                    if(!interfazPiloto.getFinCarrera()){
                        this.interfazPiloto.setDesgaste();
                    }
                    
                }

                if(mensaje.equals("fuera")){
                   interfazPiloto.setFinalCarrera(true);
                   interfazPiloto.setFuera();
                    
                }

                if(mensaje.contains("boxes")){
                    if(!interfazPiloto.getFinCarrera()){
                        this.interfazPiloto.activarBotonBoxes();
                    }
                    
                }

                if(mensaje.contains("Neumaticos")){

                    if(!interfazPiloto.getFinCarrera()){
                        this.interfazPiloto.cambiarllantas(mensaje);
                    }

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    public void cerrarConexion() {
        try {
            if (cs != null && !cs.isClosed()) {
                cs.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {

        try {
    // Instanciamos la interfaz del cliente para instanciar las características de la carrera
    Interfaz.Cliente interfazCliente = new Interfaz.Cliente(salidaServidor, this);

    // Muestra la interfaz de Piloto

} catch (Exception e) {
    System.out.println(e.getMessage());
}
    }
}