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

public class mecanico extends Agent {

    String nombre;
    Wheel_set[] juegos;

    private Codec c = new SLCodec();
    private Ontology ontologia = PItStopOntology.getInstance();



}
