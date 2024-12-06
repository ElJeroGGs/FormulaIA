package Agentes;

import jade.content.Concept;

public class Wheel_set implements Concept{

    String nombre;
    int duracion;

    public Wheel_set(String nombre, int duracion) {
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public Wheel_set() {
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setDuracion(int duracion){
        this.duracion = duracion;
    }


    public int getDuracion(){
        return duracion;
    }

}
