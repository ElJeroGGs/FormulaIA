package Agentes;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class piloto extends Agent {

    String nombre;
//Comportamiento para solicitar un cambio de llantas al mecanico
    class Comportamiento extends Behaviour {
        public void action() {
            System.out.println("Solicitar cambio de llantas al mecanico");
        }

        public boolean done() {
            return true;
        }
    }

    protected void setup() {
        addBehaviour(new Comportamiento());
    }

}
