package Socket;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Eduardo
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import Agentes.piloto;

public class Cliente extends Conexion {
    public Cliente() throws IOException {
        super("cliente");
    }

    public void startClient(piloto p) {
        try {
            DataOutputStream salidaServidor = new DataOutputStream(cs.getOutputStream());
            piloto pilotoAgente = p;

            System.out.println("prueba1");
            // Ejecutar el comportamiento del piloto en un nuevo hilo
            
            pilotoAgente.setup();

            // Esperar un tiempo para permitir que el comportamiento se ejecute
            Thread.sleep(5000); // Ajusta el tiempo según sea necesario

        } catch (IOException | InterruptedException e) {
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
}