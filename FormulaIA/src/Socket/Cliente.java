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
import java.util.Scanner;

import Agentes.piloto;
import jade.domain.df;

public class Cliente extends Conexion implements Runnable {
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
                System.out.println("Mensaje recibido: " + mensaje);
            
                salidaCliente.writeUTF("Mensaje recibido\n");
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
            // Instanciamos la interfaz del piloto
            Interfaz.InterfazPiloto interfazPiloto = new Interfaz.InterfazPiloto(salidaServidor);
            //Muestra la interfaz de Piloto
            interfazPiloto.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}