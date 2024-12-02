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

import Interfaz.InterfazPiloto;


public class Cliente extends Conexion {
    public Cliente() throws IOException {
        super("cliente");
    }
//prueba de commit
    public void startClient() {
        InterfazPiloto Ipiloto = new InterfazPiloto();
        Ipiloto.MenuPiloto();
        int ee = 0;
        try {
            while (ee < 10) {
                salidaServidor = new DataOutputStream(cs.getOutputStream());

                Scanner entrada = new Scanner(System.in);
                System.out.print("Ingrese un mensaje: ");
                String texto = entrada.nextLine();

                salidaServidor.writeUTF(texto);
                ee++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            cs.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}