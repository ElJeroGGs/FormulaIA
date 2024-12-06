package Agentes;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
 
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
 
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Interfaz.InterfazMecanico;

public class mecanico extends Agent {

    private mecanicoC mecanicoC;

    InterfazMecanico interfazMec;
    String nombre;
    Wheel_set[] juegos;

    private Codec codec = new SLCodec();
    private Ontology ontologia = PItStopOntology.getInstance();

    public void setIntMecanico(InterfazMecanico interfazMec){
        this.interfazMec = interfazMec;
    }
    //Clase que describe el comportamiento que permite recibir un mensaje y contestarlo
    class WaitPingAndReplyBehaviour extends SimpleBehaviour {
      private boolean finished = false;
 
      public WaitPingAndReplyBehaviour(Agent a) {
        super(a);
      }

       

    @Override
    public void action() {
        //Ponemos en "espera al mecanico"
        interfazMec.agregarSolicitud("Esperando solicitudes");

        //Esperamos a recibir un mensaje
        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchLanguage(codec.getName()),
            MessageTemplate.MatchOntology(ontologia.getName()));
        ACLMessage  msg = blockingReceive(mt);

        try {
 
            if(msg != null){
            if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
                    interfazMec.agregarSolicitud("Mensaje NOT UNDERSTOOD recibido");
                }
            else{
                if(msg.getPerformative()== ACLMessage.INFORM){
     
                ContentElement ec = getContentManager().extractContent(msg);
                if (ec instanceof Parada){
                    // Recibido un INFORM con contenido correcto
                    Parada p = (Parada) ec;
                    Wheel_set ws = p.get_juego();
                    interfazMec.agregarSolicitud("Mensaje recibido:");
                    interfazMec.agregarSolicitud("Compuesto: " + ws.getNombre());
                    interfazMec.agregarSolicitud("Duracion: " + ws.getDuracion());
     
                    //Hacemnos una parada
                    Cambiar cambiar = new Cambiar();
                    cambiar.set_juego(ws);
                    ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                    msg2.setLanguage(codec.getName());
                    msg2.setOntology(ontologia.getName());
                    msg2.setSender(getAID());
                  
                    
                    try {
                        getContentManager().fillContent(msg2,cambiar);
                    send(msg2);
                    } catch (Exception e) {
                    }                    
                    System.out.println("Cambio realizado");
                }
                else{
                    // Recibido un INFORM con contenido incorrecto
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("( UnexpectedContent (expected ping))");
                    send(reply);
                }
                }
                else {
                    // Recibida una performativa incorrecta
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+")( expected (inform)))");
                    send(reply);
                }
            }
            }else{
            //System.out.println("No message received");
            }
     
             }
             catch (jade.content.lang.Codec.CodecException ce) {
                   System.out.println(ce);
            }
            catch (jade.content.onto.OntologyException oe) {
                System.out.println(oe);
            }


    }

    @Override
    public boolean done() {
        return finished;}
    }

    @Override
  protected void setup() {

    //Inicializamos el agente con el argumento pasado
    Object[] args = getArguments();
    if (args != null && args.length > 0) {
        interfazMec = (InterfazMecanico) args[0];
    }
 
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontologia);
    WaitPingAndReplyBehaviour PingBehaviour;
    PingBehaviour = new  WaitPingAndReplyBehaviour(this);
    addBehaviour(PingBehaviour);
 }


}
