package Agentes;
import jade.content.Concept;

public class Wheel_set implements Concept{

    String nombre;
    int duracion;

    public Wheel_set(String nombre, int duracion) {
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public String get_nombre(){
        return nombre;
    }

    public int get_duracion(){
        return duracion;
    }

}
