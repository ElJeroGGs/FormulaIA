package Interfaz;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Socket.Cliente;

public class InterfazPiloto extends JFrame{
    private DataOutputStream salidaServidor;
    private Cliente cliente;

    public InterfazPiloto(DataOutputStream salidaServidor) {
        this.salidaServidor = salidaServidor;
        setTitle("Interfaz Piloto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton btnSolicitarCambio = new JButton("Solicitar cambio de gomas");
        btnSolicitarCambio.setBounds(50, 500, 200, 30);
        btnSolicitarCambio.addActionListener(e -> solicitarCambioLlantas());
        add(btnSolicitarCambio);

        JPanel panelMapa = new JPanel();
        panelMapa.setBounds(50, 50, 300, 300);
        panelMapa.setBorder(BorderFactory.createTitledBorder("Mapa de la pista"));
        add(panelMapa);

        JLabel lblTiempoPorVuelta = new JLabel("Tiempo por vuelta: 00:00");
        lblTiempoPorVuelta.setBounds(400, 50, 200, 30);
        add(lblTiempoPorVuelta);

        JLabel lblNumeroVueltas = new JLabel("Vueltas: 2/72");
        lblNumeroVueltas.setBounds(400, 100, 200, 30);
        add(lblNumeroVueltas);

        JLabel lblLeaderboard = new JLabel("<html>Leaderboard:<br>1. Piloto A<br>2. Piloto B<br>3. Piloto C</html>");
        lblLeaderboard.setBounds(400, 150, 200, 100);
        add(lblLeaderboard);

    }

    

    public void solicitarCambioLlantas() {
        //Solicita cambio de llantas

        try {
            salidaServidor.writeUTF("cambio de llantas");
        } catch (IOException e) {
            System.out.println("Error al enviar la solicitud: " + e.getMessage());
        }
    
        
    }
}
