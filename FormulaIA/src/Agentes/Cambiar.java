package Agentes;

import jade.content.AgentAction;

public class Cambiar implements AgentAction{

    private Wheel_set juego;

    public void set_juego(Wheel_set juego){
        this.juego = juego;
    }

    public Wheel_set get_juego(){
        return juego;
    }

}
