package Interfaz;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import Socket.Cliente;

public class InterfazPiloto {
    private DataOutputStream salidaServidor;
    private Cliente cliente;

    public InterfazPiloto(DataOutputStream salidaServidor) {
        this.salidaServidor = salidaServidor;
    }

    public void MenuPiloto() {
        System.out.println("Presiona cualquier tecla para solicitar un cambio de llantas");

        Scanner sc = new Scanner(System.in);
        sc.nextLine(); // Espera a que el usuario presione una tecla

        solicitarCambioLlantas();
    }

    public void solicitarCambioLlantas() {
        //Solicita cambio de llantas

        try {
            salidaServidor.writeUTF("cambio de llantas");
        } catch (IOException e) {
            System.out.println("Error al enviar la solicitud: " + e.getMessage());
        }
    
        
    }
}
