package Agentes;

import jade.content.Concept;

public class Wheel_set implements Concept{

    String nombre;
    int duracion;
    int duracion_actual;

    double duraciond;
    double duracion_actuald;

    public Wheel_set(String nombre, int duracion) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.duracion_actual = duracion;

        this.duraciond = duracion;
        this.duracion_actuald = duracion;
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

    public double getDuraciond(){
        return duraciond;
    }

    public double getDesgaste(){
    //Desgaste entre 0 y 100
        return 100 - (duracion_actuald*100)/duraciond;
        
    }

    public void setDesgaste(){
        this.duracion_actuald = duracion_actuald-0.01;
    }

}
