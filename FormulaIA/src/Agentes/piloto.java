package Agentes;

import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
 
 
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
 
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
 
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class piloto extends Agent {
    private DataOutputStream salidaServidor;
    private Socket clienteSocket;

    private Codec c = new SLCodec();
    private Ontology ontologia = PItStopOntology.getInstance();

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
