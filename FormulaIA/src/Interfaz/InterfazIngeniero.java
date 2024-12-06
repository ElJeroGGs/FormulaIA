package Interfaz;

import javax.swing.*;
import javax.xml.crypto.Data;

import Agentes.Ingeniero_pista;
import jade.core.AID;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;

public class InterfazIngeniero extends JFrame {
    private JTextArea mensajesPilotoArea;
    private JLabel vueltasRestantesLabel;
    private JLabel desgasteNeumaticosLabel;
    private JLabel pronosticoCarreraLabel;
    private DataOutputStream salidaServidor;
    private InterfazMecanico interfazMecanico;
    private Ingeniero_pista ingeniero_pista;
    

    public InterfazIngeniero(DataOutputStream ss) {
    
       
        
        this.salidaServidor = ss;
        setTitle("Interfaz Ingeniero");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para mensajes del piloto
        JPanel panelMensajesPiloto = new JPanel(new BorderLayout());
        mensajesPilotoArea = new JTextArea();
        mensajesPilotoArea.setEditable(false);
        panelMensajesPiloto.add(new JScrollPane(mensajesPilotoArea), BorderLayout.CENTER);
        panelMensajesPiloto.setBorder(BorderFactory.createTitledBorder("Mensajes del Piloto"));

        // Panel para vueltas restantes
        JPanel panelVueltasRestantes = new JPanel(new BorderLayout());
        vueltasRestantesLabel = new JLabel("Vueltas restantes: vuelta 2/41");
        panelVueltasRestantes.add(vueltasRestantesLabel, BorderLayout.CENTER);
        panelVueltasRestantes.setBorder(BorderFactory.createTitledBorder("Vueltas Restantes"));

        // Panel para desgaste de los neumáticos
        JPanel panelDesgasteNeumaticos = new JPanel(new BorderLayout());
        desgasteNeumaticosLabel = new JLabel("Desgaste de los neumáticos: 38%");
        panelDesgasteNeumaticos.add(desgasteNeumaticosLabel, BorderLayout.CENTER);
        panelDesgasteNeumaticos.setBorder(BorderFactory.createTitledBorder("Desgaste de los Neumáticos"));

        // Panel para pronóstico de la carrera
        JPanel panelPronosticoCarrera = new JPanel(new BorderLayout());
        pronosticoCarreraLabel = new JLabel("Pronóstico de la carrera: se prevee lluvia ligera");
        panelPronosticoCarrera.add(pronosticoCarreraLabel, BorderLayout.CENTER);
        panelPronosticoCarrera.setBorder(BorderFactory.createTitledBorder("Pronóstico de la Carrera"));

        // Botón para preparar pitstop
        JButton prepararPitstop = new JButton("Preparar Pitstop");
        prepararPitstop.addActionListener(e -> {
           llamarMecanicos();});

        // Agregar los paneles al contenedor principal
        JPanel panelPrincipal = new JPanel(new GridLayout(5, 1));
        panelPrincipal.add(panelMensajesPiloto);
        panelPrincipal.add(panelVueltasRestantes);
        panelPrincipal.add(panelDesgasteNeumaticos);
        panelPrincipal.add(panelPronosticoCarrera);
        panelPrincipal.add(prepararPitstop);

        add(panelPrincipal, BorderLayout.CENTER);

        
    }

    public void setInterfazMecanico(InterfazMecanico interfazMecanico) {
        this.interfazMecanico = interfazMecanico;
    }

    private void llamarMecanicos()  {
        // Lógica para llamar a los mecánicos
       
        System.out.println("Llamando a los mecánicos");
        interfazMecanico.agregarSolicitud("solicitud de pitstop");
      
        //this.ingeniero_pista.start();
    }

    // Método para actualizar los mensajes del piloto
    public void agregarMensajePiloto(String mensaje) {
        mensajesPilotoArea.append(mensaje + "\n");
    }

    // Método para actualizar las vueltas restantes
    public void actualizarVueltasRestantes(String vueltas) {
        vueltasRestantesLabel.setText(vueltas);
    }

    // Método para actualizar el desgaste de los neumáticos
    public void actualizarDesgasteNeumaticos(String desgaste) {
        desgasteNeumaticosLabel.setText(desgaste);
    }

    // Método para actualizar el pronóstico de la carrera
    public void actualizarPronosticoCarrera(String pronostico) {
        pronosticoCarreraLabel.setText(pronostico);
    }

    public void setIngeniero (Ingeniero_pista ingeniero_pista){
        this.ingeniero_pista = ingeniero_pista;
    }
}
