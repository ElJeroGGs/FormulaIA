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
//prueba de commiting
    public void startClient() {
        InterfazPiloto Ipiloto = new InterfazPiloto(this.salidaServidor);
        
        Ipiloto.MenuPiloto();
    }
    //Metodo para comprobar que no se ha solicitado cambio de llantas
    
    

    public void cerrarConexion() {
        try {
            cs.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}