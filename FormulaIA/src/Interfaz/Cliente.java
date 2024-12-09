package Interfaz;

import java.awt.Color;
import java.awt.Font;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Agentes.Wheel_set;

//Interfaz que sirve para configurar la conexion desde el servidor
public class Cliente extends JFrame{

    JPanel panelCircuito;
    int circuitoActual = 0;
    PanelNeumaticos panelNeumaticos;
    JButton btnComenzarCarrera;
    DataOutputStream ss;
    Socket.Cliente cliente;


    public Cliente(DataOutputStream salidaServidor, Socket.Cliente cliente){ 
        this.cliente = cliente;
        this.ss = salidaServidor;
        Font font = new Font("Arial", Font.BOLD, 20);
        setTitle("Interfaz Preparación");
        setSize(800, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        //Etiqueta escoger gomas
        JLabel lblEscogerGomas = new JLabel("Escoge los neumáticos para la carrera");
        lblEscogerGomas.setBounds(190, 20, 390, 30);
        lblEscogerGomas.setFont(font);
        add(lblEscogerGomas);
        panelNeumaticos = new PanelNeumaticos();
        panelNeumaticos.setCliente(this);
        panelNeumaticos.setBounds(150, 50, 410, 150);
        add(panelNeumaticos);
        //etiqueta escoger circuito
        JLabel lblEscogerCircuito = new JLabel("Escoge el circuito para la carrera");
        lblEscogerCircuito.setBounds(190, 200, 390, 30);
        lblEscogerCircuito.setFont(font);
        add(lblEscogerCircuito);

        //Panel para escoger circuito
        seleccionCircuito(circuitoActual);
        //Fuente para las flechas
        Font fontFlechas = new Font("Arial", Font.BOLD, 50);

        //Agregamos dos labels con flechas para cambiar de circuito
        JButton lblFlechaIzq = new JButton("<");
        lblFlechaIzq.setBounds(200, 650, 70, 70);
        lblFlechaIzq.setFont(fontFlechas);
        lblFlechaIzq.setFocusPainted(false);
        lblFlechaIzq.setForeground(Color.BLACK);
        lblFlechaIzq.setBackground(Color.WHITE);
        add(lblFlechaIzq);
        lblFlechaIzq.addActionListener(e -> cambiarCircuito("izquierda"));

        JButton lblFlechaDer = new JButton(">");
        lblFlechaDer.setBounds(450, 650, 70, 70);
        lblFlechaDer.setFont(fontFlechas);
        lblFlechaDer.setFocusPainted(false);
        lblFlechaDer.setForeground(Color.BLACK);
        lblFlechaDer.setBackground(Color.WHITE);
        add(lblFlechaDer);
        lblFlechaDer.addActionListener(e -> cambiarCircuito("derecha"));

        //Boton para comenzar la carrera
        btnComenzarCarrera = new JButton("Comenzar carrera");
        btnComenzarCarrera.setFont( new Font("Arial", Font.BOLD, 24));
        btnComenzarCarrera.setBackground(Color.darkGray);
        btnComenzarCarrera.setForeground(Color.white);
        btnComenzarCarrera.setFocusPainted(false);
        btnComenzarCarrera.setBounds(220, 750, 290, 70);
        add(btnComenzarCarrera);
        btnComenzarCarrera.setEnabled(false);
        btnComenzarCarrera.addActionListener(e -> {
            comenzarCarrera();
        });

        this.setVisible(true);
    
    }
    public String getNombreCircuito(){
        switch (circuitoActual) {
            case 0:
                return "canada";
            case 1:

                return "china";
            case 2:
                return "germany";
            case 3:
                return "spain";
            case 4:
                return "monaco";
            case 5:
                return "usa";
            case 6:
                return "italia";
            case 7:
                return "japan";
            case 8:
                return "malaysia";
            default:
                return "";
        }
    }

    //Metodo para la seleccion de circuito
    public void seleccionCircuito(int circuito){
       
        if (panelCircuito != null){
    this.remove(panelCircuito);
    
    }
        
        PanelPista Canada = new PanelPista("canada");
        PanelPista China = new PanelPista("china");
        PanelPista Alemania = new PanelPista("germany");
        PanelPista Espana = new PanelPista("spain");
        PanelPista Monaco = new PanelPista("monaco");
        PanelPista Usa = new PanelPista("usa");
        PanelPista Italia = new PanelPista("italy");
        PanelPista Japon = new PanelPista("japan");
        PanelPista Malasia = new PanelPista("malaysia");
      
      
        switch(circuito){
            case 0:
                panelCircuito = Canada;
                break;
            case 1:
                panelCircuito = China;
                break;
            case 2:
                panelCircuito = Alemania;
                break;
            case 3:
                panelCircuito = Espana;
                break;
            case 4:
                panelCircuito = Monaco;
                break;
            case 5:
                panelCircuito = Usa;
                break;
            case 6:
                panelCircuito = Italia;
                break;
            case 7:
                panelCircuito = Japon;
                break;
            case 8:
                panelCircuito = Malasia;
                break;
            default:
                panelCircuito = Canada;
                break;
        }
        panelCircuito.setBounds(150, 250, 500, 360);
        
        add(panelCircuito);
        this.repaint();
        
        
    }

    //Metodo que se encarga de el comienzo de la carrera
    public void comenzarCarrera(){
        //Aqui se debe de mandar la informacion al servidor para comenzar la carrera
        Wheel_set ws = panelNeumaticos.getNeumaticosSeleccionados();
        String circuito = getNombreCircuito();

        InterfazPiloto interfaz = new InterfazPiloto(ss, circuito, ws);
        interfaz.setVisible(true);
        cliente.setInterfazPiloto(interfaz);
        
        this.dispose();
    }

    //Metodo para cambiar valor de circuito
    public void cambiarCircuito(String direccion){
        int suma = 0;
        if(direccion.equals("derecha")){
            suma = 1;
        }else{
            suma = -1;
        }

        circuitoActual += suma;

    // Asegurarse de que el índice esté dentro de los límites
    if (circuitoActual < 0) {
        circuitoActual = 8; // Suponiendo que hay 9 circuitos, índices de 0 a 8
    } else if (circuitoActual > 8) {
        circuitoActual = 0;
    }

    seleccionCircuito(circuitoActual);
    panelCircuito.repaint();
    }


    public static void main(String[] args) {
        Cliente cliente = new Cliente(null,null);
        cliente.setVisible(true);
    }

    public void activarBoton() {
        btnComenzarCarrera.setEnabled(true);

    }
}
