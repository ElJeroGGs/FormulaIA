package Interfaz;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Agentes.Wheel_set;

import java.awt.Color;
import java.awt.Font;

import Socket.Cliente;

public class InterfazPiloto extends JFrame{
    private DataOutputStream salidaServidor;
    private Cliente cliente;
    private PanelPista pistaPanel;
    private Wheel_set juegoActual;
    private JButton btnSolicitarCambio;
    private JLabel lblNumeroVueltas;
    private JLabel lblTiempoPorVuelta;
    private List<String> TiemposVueltas = new ArrayList<String>();
    


    public List<String> getTiemposVueltas() {
        return pistaPanel.getLapTimes();
    }

    public void setDesgasteNeumaticos(){
        juegoActual.setDesgaste();
    }

    public InterfazPiloto(DataOutputStream salidaServidor, String circuito, Wheel_set jA) {

        this.juegoActual = jA;

        this.salidaServidor = salidaServidor;
        setTitle("Interfaz Piloto");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Font font = new Font("Arial", Font.PLAIN, 20);

        btnSolicitarCambio = new JButton("Solicitar cambio de gomas");
        btnSolicitarCambio.setFont( new Font("Arial", Font.BOLD, 24));
        btnSolicitarCambio.setBackground(Color.darkGray);
        btnSolicitarCambio.setForeground(Color.white);
        btnSolicitarCambio.setFocusPainted(false);
        btnSolicitarCambio.setBounds(50, 450, 350, 70);
        btnSolicitarCambio.addActionListener(e -> solicitarCambioLlantas());
        btnSolicitarCambio.setEnabled(false);
        add(btnSolicitarCambio);

        //Boton comenzar carrera
        JButton btnComenzarCarrera = new JButton("Comenzar carrera");
        btnComenzarCarrera.setFont( new Font("Arial", Font.BOLD, 24));
        btnComenzarCarrera.setBackground(Color.darkGray);
        btnComenzarCarrera.setForeground(Color.white);
        btnComenzarCarrera.setFocusPainted(false);
        btnComenzarCarrera.setBounds(450, 450, 300, 70);
        add(btnComenzarCarrera);
        btnComenzarCarrera.addActionListener(e -> {
            
            pistaPanel.startLap();
            btnComenzarCarrera.setEnabled(false);
            try {
                salidaServidor.writeUTF("comienzo");
            } catch (Exception f) {
                // TODO: handle exception
            }
        });

        //Etiqueta Mapa de la pista
        JLabel lblMapa = new JLabel("Mapa de la pista");
        lblMapa.setBounds(50, 20, 200, 30);
        lblMapa.setFont(font);
        add(lblMapa);

        //Incluimos la pista
        pistaPanel = new PanelPista(circuito);
        pistaPanel.setInterfazPiloto(this);
        pistaPanel.setBounds(50, 50, 500, 400);
       
        add(pistaPanel);

        lblTiempoPorVuelta = new JLabel("Tiempo de vuelta:"+"00:00:00");
        lblTiempoPorVuelta.setBounds(550, 50, 300, 30);
        lblTiempoPorVuelta.setFont(font);
        add(lblTiempoPorVuelta);

        lblNumeroVueltas = new JLabel("Vueltas:"+pistaPanel.getVueltasCompletadas()+"/"+pistaPanel.getNumeroVueltas());
        lblNumeroVueltas.setBounds(550, 100, 200, 30);
        lblNumeroVueltas.setFont(font);
        add(lblNumeroVueltas);

        JLabel lblLeaderboard = new JLabel("<html>Leaderboard:<br>1. Piloto A<br>2. Piloto B<br>3. Piloto C</html>");
        lblLeaderboard.setBounds(550, 150, 200, 100);
        lblLeaderboard.setFont(font);
        add(lblLeaderboard);

    }

    //Metodo para repintar el numero de vueltas
    public void repintarVueltas(){
        lblNumeroVueltas.setText("Vueltas:"+pistaPanel.getVueltasCompletadas()+"/"+pistaPanel.getNumeroVueltas());
    }

    // Método para iniciar el temporizador que actualiza el tiempo por vuelta
    private void startLapTimeUpdater() {
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblTiempoPorVuelta.setText("Tiempo de vuelta:\n" + pistaPanel.getCurrentLapTime());
            }
        });
        timer.start();
    }

    public void VueltaCompletada (){
        try {
            salidaServidor.writeUTF(getTiemposVueltas().get(getTiemposVueltas().size()-1));
        } catch (Exception e) {
        }
    }

    public void setDesgaste(){
        
        try {
            salidaServidor.writeUTF("desgaste "+juegoActual.getDesgaste());
            this.pistaPanel.setDesgasteNeumaticos(juegoActual.getDesgaste());
        } catch (Exception e) {
        }
    }
    

    public void solicitarCambioLlantas() {
        //Solicita cambio de llantas

        try {
            salidaServidor.writeUTF("El piloto quiere un cambio de llantas");
        } catch (IOException e) {
            System.out.println("Error al enviar la solicitud: " + e.getMessage());
        }
    
        
    }

    //Metodo para probar la interfaz
    public static void main(String[] args) {
        InterfazPiloto interfaz = new InterfazPiloto(null, "canada", null);
        interfaz.setVisible(true);
    }



    public void activarBoton() {
        btnSolicitarCambio.setEnabled(true);
        startLapTimeUpdater();
    }
}
