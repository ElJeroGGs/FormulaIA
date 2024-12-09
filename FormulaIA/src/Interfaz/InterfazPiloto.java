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
    private boolean finCarrera = false;    
    private Timer timere;
    

    private JButton btnEntrarBoxes;


    public List<String> getTiemposVueltas() {
        return pistaPanel.getLapTimes();
    }

    public boolean getFinCarrera(){
        return finCarrera;
    }

    public void setDesgasteNeumaticos(){
        boolean enPits = pistaPanel.getDentroPits();
        if(!enPits){
            if(!finCarrera){
                juegoActual.setDesgaste();
                setVelocidad();
            }
        }


        
    }

    //Metodo que cambia la velocidad del piloto
    public void setVelocidad (){
        double desgaste = this.juegoActual.getDesgaste();
        int velocidad = 7;
        if(desgaste > 5 && desgaste < 15){
             velocidad = 14;
        }
        else if(desgaste > 15 && desgaste < 25){
            velocidad = 21;
        }
        else if(desgaste > 25 && desgaste < 35){
            velocidad = 28;
        } else if(desgaste > 35 && desgaste < 55){
            velocidad = 35;
        } 
          else if(desgaste > 55 && desgaste < 75){
            velocidad = 28; 
        } else if(desgaste > 75 && desgaste < 85){
            velocidad = 21;
        } else if(desgaste > 85 && desgaste < 95){
            velocidad = 14;
        } else if(desgaste > 95){
            velocidad = 7;
        }
        pistaPanel.setVelocidad(velocidad);
    }

    public void setFinalCarrera(boolean finCarrera){
        this.finCarrera = finCarrera;
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
                salidaServidor.writeUTF("Vueltas restantes: "+(pistaPanel.getNumeroVueltas()));
                
            } catch (Exception f) {
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

         //Boton para entrar a Boxes
        btnEntrarBoxes = new JButton("Entrar a Boxes");
        btnEntrarBoxes.setFont( new Font("Arial", Font.BOLD, 24));
        btnEntrarBoxes.setBackground(Color.darkGray);
        btnEntrarBoxes.setForeground(Color.white);
        btnEntrarBoxes.setFocusPainted(false);
        btnEntrarBoxes.setBounds(50, 450, 300, 70);
        btnEntrarBoxes.setVisible(false);
        btnEntrarBoxes.setEnabled(false);
        add(btnEntrarBoxes);

        btnEntrarBoxes.addActionListener(e -> {
            pistaPanel.EntrarBoxes();
            btnEntrarBoxes.setVisible(false);
            btnSolicitarCambio.setVisible(true);
        });

    }

    //Metodo para repintar el numero de vueltas
    public void repintarVueltas(){
        lblNumeroVueltas.setText("Vueltas:"+pistaPanel.getVueltasCompletadas()+"/"+pistaPanel.getNumeroVueltas());
        
    }

    public void activarBotonBoxes(){
        btnSolicitarCambio.setEnabled(false);
        btnSolicitarCambio.setVisible(false);
        btnEntrarBoxes.setEnabled(true);
        btnEntrarBoxes.setVisible(true);
    }

    // Método para iniciar el temporizador que actualiza el tiempo por vuelta
    private void startLapTimeUpdater() {
     timere = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(finCarrera){
                    timere.stop();
                }else{
                    lblTiempoPorVuelta.setText("Tiempo de vuelta:\n" + pistaPanel.getCurrentLapTime());
                }
                
            }
        });
        timere.start();
    }

    public void VueltaCompletada (){


        try {
            salidaServidor.writeUTF(getTiemposVueltas().get(getTiemposVueltas().size()-1));
            salidaServidor.writeUTF("Vueltas restantes: "+(pistaPanel.getNumeroVueltas()-pistaPanel.getVueltasCompletadas()));
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
btnSolicitarCambio.setVisible(false);
btnEntrarBoxes.setVisible(true);
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

    public void EntrandoTaller() {

     //Mandamos 2 veces el mensaje por si acaso
        try {
            salidaServidor.writeUTF("Entrando a Pitstop");
            salidaServidor.writeUTF("Entrando a Pitstop");
        } catch (Exception e) {
        }
        
    }


    public void saliendoTaller() {
        try {
            salidaServidor.writeUTF("Saliendo de Pitstop");
        } catch (Exception e) {
    }
}

    public void cambiarllantas(String llantas) {

        Wheel_set juegoNuevo = new Wheel_set();
        //Primero verificamos que compeusto es
        if(llantas.contains("Neumaticos Duros")){
            juegoNuevo = new Wheel_set("Neumaticos Duros", 40);
    }
    if (llantas.contains("Neumaticos Medios")) {
        juegoNuevo = new Wheel_set("Neumaticos Medios", 30);
    }
    if (llantas.contains("Neumaticos Blandos")) {
        juegoNuevo = new Wheel_set("Neumaticos Blandos", 20);
    }
    this.juegoActual = juegoNuevo;
    
}

    public void saliendoPits() {
        try {
            salidaServidor.writeUTF("Saliendo de Pits");
        } catch (Exception e) {
        }
    }

    public void iniciarCarrera() {
        try {
            salidaServidor.writeUTF("comienzo");
        } catch (Exception e) {
        }
    }
}