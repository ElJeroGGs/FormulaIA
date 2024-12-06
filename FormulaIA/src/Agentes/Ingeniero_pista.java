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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

//Haciendo uso de la ontología, escoge un set de llantas (lo crea) y lo envía al piloto
public class Ingeniero_pista extends Agent {
    private Codec codec = new SLCodec();
    private Ontology ontologia = PItStopOntology.getInstance();
    private AID idmecanico;
    

    protected void setup() {
        System.out.println("Agente ingeniero_pista iniciado");

        

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);

        addBehaviour(new EnviarParadaBehaviour());
    }

    private class EnviarParadaBehaviour extends Behaviour {
        private boolean finished = false;

        public void action() {
            // Crear un nuevo set de llantas
            Wheel_set juego = new Wheel_set();
            juego.setDuracion(100);
            juego.setNombre("duros");

           

            // Enviar el set de llantas al piloto
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAID());
            msg.addReceiver(new AID("Mecanico", AID.ISLOCALNAME));
            msg.setLanguage(codec.getName());
            msg.setOntology(ontologia.getName());

        // Crear una parada y añadir el set de llantas
        Parada parada = new Parada();
        parada.set_juego(juego);

        

            try {
                getContentManager().fillContent(msg, parada);
                send(msg);
                finished = true;
            } catch (Codec.CodecException | OntologyException e) {
                e.printStackTrace();
            }
        }

        public boolean done() {
            return finished;
        }
    }

    public void setMecanico(AID idmecanico) {
        this.idmecanico = idmecanico;
    }
    
}
