package Agentes;

import Interfaz.InterfazMecanico;

public class mecanicoC {

    private InterfazMecanico interfazMecanico;

    public mecanicoC(){

    }
    //Constructor personalizado
    public mecanicoC(InterfazMecanico interfazMecanico){
        this.interfazMecanico = interfazMecanico;
    }
    //Getters y setters
    public InterfazMecanico getInterfazMecanico() {
        return interfazMecanico;
    }

    public void setInterfazMecanico(InterfazMecanico interfazMecanico) {
        this.interfazMecanico = interfazMecanico;
    }

    


}
