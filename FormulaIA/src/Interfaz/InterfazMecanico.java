package Interfaz;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.xml.crypto.Data;

import Agentes.mecanico;

public class InterfazMecanico extends JFrame {
    JTextArea solicitudesArea;
    DataOutputStream salidaServidor;
    InterfazIngeniero iIng;
   
    public InterfazMecanico() {
     
        
        setTitle("Interfaz Mecánico");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel solicitudesLabel = new JLabel("Solicitudes:");
        solicitudesLabel.setBounds(10, 10, 100, 25);
        add(solicitudesLabel);

         solicitudesArea = new JTextArea();
        solicitudesArea.setBounds(10, 40, 360, 150);
        solicitudesArea.setEditable(false);
        add(solicitudesArea);

        JButton prepararPitstopButton = new JButton("Preparar Pitstop");
        prepararPitstopButton.setBounds(10, 200, 150, 25);
        add(prepararPitstopButton);

        JButton confirmarPitstopButton = new JButton("Confirmar Pitstop");
        confirmarPitstopButton.setBounds(200, 200, 150, 25);
        add(confirmarPitstopButton);
        //Agregar acción al botón de confirmar pitstop
        confirmarPitstopButton.addActionListener(e -> {
            try {
                confirmarPitstop();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    
        
    }

    // Método para agregar una solicitud a la lista de solicitudes
    public void agregarSolicitud(String solicitud) {
        // Agregar la solicitud a la lista de solicitudes
        solicitudesArea.append(solicitud + "\n");

    }

    //Metodo para confirmar pitstop con Ingeniero
    public void confirmarPitstop() throws IOException {
        // Confirmar pitstop con el ingeniero
        iIng.agregarMensajePiloto("Pitstop confirmado");
}

    public void setInterfazIngeniero(InterfazIngeniero interfazIng) {
        iIng = interfazIng;
    }

    
}

  
