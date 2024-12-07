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

import Interfaz.InterfazIngeniero;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

//Haciendo uso de la ontología, escoge un set de llantas (lo crea) y lo envía al piloto
public class Ingeniero_pista extends Agent {
    private Codec codec = new SLCodec();
    private Ontology ontologia = PItStopOntology.getInstance();
    private AID idmecanico;
    private Wheel_set juego;
    private InterfazIngeniero   interfazIn;

    public void setJuego(Wheel_set juego) {
        this.juego = juego;
    }
    

    protected void setup() {
        System.out.println("Agente ingeniero_pista iniciado");

        //Buscamos dentro de los argumentos el wheel_set
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            juego = (Wheel_set) args[0];
            interfazIn = (InterfazIngeniero) args[1];
        }

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);

        addBehaviour(new EnviarParadaBehaviour());
       
    }

    private class EnviarParadaBehaviour extends Behaviour {
        private boolean finished = false;

        public void action() {
            

            // Enviar el set de llantas al piloto
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAID());
            msg.addReceiver(new AID("Mecanico", AID.ISLOCALNAME));
            msg.setLanguage(codec.getName());
            msg.setOntology(ontologia.getName());

        // Crear una parada y añadir el set de llantas
        Parada parada = new Parada();
        parada.set_juego(juego);

        System.out.println(juego.getNombre());
        

            try {
                getContentManager().fillContent(msg, parada);
                send(msg);

                 //Una vez que se manda el mensaje, se espera una respuesta
            
                MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchLanguage(codec.getName()),
                MessageTemplate.MatchOntology(ontologia.getName()));
                ACLMessage msg2 = blockingReceive(mt); 
                
                if (msg2.getPerformative() == ACLMessage.CONFIRM) {
                    interfazIn.agregarMensajePiloto("Parada confirmada, manda a boxes");
                    interfazIn.activarBoxes();
                } else {
                    block();
                }
           
                finished = true;
            } catch (Codec.CodecException | OntologyException e) {
                e.printStackTrace();
            }
        }

        public boolean done() {
            return finished;
            
        }

        //Matamos al agente
        public int onEnd() {
            myAgent.doDelete();
            return super.onEnd();
    }
}

    public void setMecanico(AID idmecanico) {
        this.idmecanico = idmecanico;
    }
    
}
