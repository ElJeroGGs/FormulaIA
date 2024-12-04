package Socket;

import java.io.IOException;

import Agentes.piloto;


public class MainCliente
{
    public static void main(String[] args) throws IOException
    {
        //Instanciamos el cliente
        Cliente cli = new Cliente(); //Se crea el cliente

        System.out.println("Iniciando cliente\n");
        //Instanciamos el agente piloto
        piloto Pil = new piloto(cli.salidaServidor);
        Pil.setup();

        cli.startClient(Pil); //Se inicia el cliente

        //Instanciamos la GUI del agente

        //String[] args1 = {"-gui"};
        //jade.Boot.main(args1);

    }
}