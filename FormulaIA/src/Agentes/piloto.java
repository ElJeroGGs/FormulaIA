package Agentes;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class piloto extends Agent {
    private DataOutputStream salidaServidor;
    private Socket clienteSocket;

    // Constructor para inicializar el DataOutputStream
    public piloto(DataOutputStream salidaServidor, Socket cS) {
        this.salidaServidor = salidaServidor;
        this.clienteSocket = cS;
        
    }

    // Comportamiento para solicitar un cambio de llantas al mecanico
    private class Comportamiento extends Behaviour {
        private boolean done = false;

        @Override
        public void action() {
            System.out.println("Solicitar cambio de llantas al mecanico");
            try {
                salidaServidor.writeUTF("solicitando cambio de llantas");
                //clienteSocket.close();

            } catch (IOException e) {
                System.out.println("Error al enviar la solicitud: " + e.getMessage());
            }
        }

        
        public boolean done() {
            return true;
        }
    }
    @Override
    protected void setup() {
        System.out.println("agente iniciado");
        addBehaviour(new Comportamiento());


    }
}
