package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Time;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.crypto.Data;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Agentes.mecanico;
import jade.wrapper.AgentController;

public class InterfazMecanico extends JFrame {
    JTextArea solicitudesArea;
    DataOutputStream salidaServidor;
    InterfazIngeniero iIng;
    PanelPits pPits;
    AgentController mecanico;
    PanelNeumaticos panelNeumaticos;
    JButton confirmarPitstopButton;
    boolean confirmado = false;

    public void setMecanico(AgentController mecanico) {
        this.mecanico = mecanico;
    }

    public InterfazMecanico() {
//Creamos una fuente
        Font font = new Font("Arial", Font.BOLD, 20);
        setTitle("Interfaz Mecánico");
        setSize(790, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel solicitudesLabel = new JLabel("Solicitudes:");
        solicitudesLabel.setFont(font);
        solicitudesLabel.setBounds(145, 10, 150, 25);
        add(solicitudesLabel);

        solicitudesArea = new JTextArea();
        solicitudesArea.setBounds(30, 40, 360, 150);
        solicitudesArea.setFont(font);
        solicitudesArea.setEditable(false);
        
        //Agregamos un scroll a solicitudesArea

        JPanel panelSolicitudes = new JPanel(new BorderLayout());
        panelSolicitudes.setBounds(30, 40, 400, 180);
        panelSolicitudes.add(new JScrollPane(solicitudesArea));

        add(panelSolicitudes);
        

        JButton prepararPitstopButton = new JButton("Preparar Pitstop");
        prepararPitstopButton.setFont(font);
        prepararPitstopButton.setForeground(Color.WHITE);
        prepararPitstopButton.setBackground(Color.BLACK);
        prepararPitstopButton.setBounds(450, 80, 250, 25);
        add(prepararPitstopButton);

        confirmarPitstopButton = new JButton("Confirmar Pitstop");
        confirmarPitstopButton.setFont(font);
        confirmarPitstopButton.setForeground(Color.WHITE);
        confirmarPitstopButton.setBackground(Color.BLACK);
        confirmarPitstopButton.setBounds(450, 140, 250, 25);
        add(confirmarPitstopButton);
        //Desactivado hasta que se prepare el pitstop
        confirmarPitstopButton.setEnabled(false);
        // Agregar acción al botón de confirmar pitstop
        confirmarPitstopButton.addActionListener(e -> {
            try {
                confirmarPitstop();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        prepararPitstopButton.addActionListener(e -> {
            //iIng.agregarMensajePiloto("Pitstop en preparación");
            pPits.setImagenInicial();

        });

        //Etiqueta "selección de neumáticos"
        JLabel seleccionNeumaticos = new JLabel("Selección de neumáticos");
        seleccionNeumaticos.setBounds(450, 200, 500, 50);
        seleccionNeumaticos.setFont(font);
        add(seleccionNeumaticos);
        //panel de neumáticos
        panelNeumaticos = new PanelNeumaticos();
        panelNeumaticos.setBounds(350, 250, 410, 150);
        add(panelNeumaticos);

        //Agregamos panel de Pitstop
        pPits = new PanelPits();
        pPits.setBounds(50, 250, 300, 300);
        add(pPits);

        

    }

    //Metodo para seleccionar neumáticos
    public void seleccionarNeumaticos(String nombre) {
        //Seleccionar neumáticos
        panelNeumaticos.seleccionarNeumaticos(nombre);
        //Después de 5 segundos se limpia el solicitudesArea
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                solicitudesArea.setText("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para agregar una solicitud a la lista de solicitudes
    public void agregarSolicitud(String solicitud) {
        // Agregar la solicitud a la lista de solicitudes
        //Si ya esta escrito "Esperando solicitudes" no lo agrega
        if (solicitudesArea.getText().equals("Esperando solicitudes\n")) {
            solicitudesArea.setText("");
        }
        
        solicitudesArea.append(solicitud + "\n");

    }

    // Metodo para confirmar pitstop con Ingeniero
    public void confirmarPitstop() throws IOException {
        // Confirmar pitstop con el ingeniero
        this.confirmado = true;
    }

    public void setInterfazIngeniero(InterfazIngeniero interfazIng) {
        iIng = interfazIng;
    }

    // Método para probar la interfaz
    public static void main(String[] args) {
        InterfazMecanico interfazMecanico = new InterfazMecanico();
        interfazMecanico.setVisible(true);
    }

    public void confirmarParada() {
        //Verifica que los mecanicos estén listos
        while (!this.pPits.getEstado().equals("preparado")) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.confirmarPitstopButton.setEnabled(true);
        confirmar();
    }

    public void confirmar(){

        //Mientras no se pulse el botón de confirmar pitstop, esperar
        while (!confirmado) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void Pits() {
        //Esperamos 3 segundos y corremos gif pits
        remove(pPits);
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pPits = null;
                repaint();
                pPits = new PanelPits();
                pPits.setBounds(50, 250, 300, 300);
                add(pPits);
                pPits.correrGif();
            }
        });
        timer.setRepeats(false);
        timer.start();
            
                
                
      
    }
}
