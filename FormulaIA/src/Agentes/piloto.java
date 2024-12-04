package Agentes;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.io.DataOutputStream;
import java.io.IOException;

public class piloto extends Agent {
    private DataOutputStream salidaServidor;

    // Constructor para inicializar el DataOutputStream
    public piloto(DataOutputStream salidaServidor) {
        this.salidaServidor = salidaServidor;
    }

    // Comportamiento para solicitar un cambio de llantas al mecanico
    public class Comportamiento extends Behaviour {
        
        public void action() {
            System.out.println("Solicitar cambio de llantas al mecanico");
            try {
                salidaServidor.writeUTF("solicitando cambio de llantas");
            } catch (IOException e) {
                System.out.println("Error al enviar la solicitud: " + e.getMessage());
            }
        }

        public boolean done() {
            return true;
        }
    }

    public void setup() {
        System.out.println("agente iniciado");
        addBehaviour(new Comportamiento());


    }
}
