package Socket;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Eduardo
 */
import java.io.IOException;

import Agentes.Ingeniero_pista;
import Agentes.mecanico;



//Clase principal que hará uso del servidor (Mecanicos e Ingeniero de pista)
public class MainServidor

{
    
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        
        Servidor serv = new Servidor(); //Se crea el servidor
        //Instanciamos los agentes
       

        System.out.println("Iniciando servidor\n");

//Instanciamos la GUI de los agentes
//String[] args1 = {"-gui"};
//jade.Boot.main(args1);

        //Se inicia el servidor
        serv.startServer();

        
        
    }
}