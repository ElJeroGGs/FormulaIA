package Interfaz;

import javax.swing.*;
import javax.xml.crypto.Data;

import java.awt.event.MouseEvent;

import Agentes.Ingeniero_pista;
import Agentes.Wheel_set;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.DataOutputStream;
import java.io.IOException;
import jade.wrapper.AgentContainer;

public class InterfazIngeniero extends JFrame {
    private JTextArea mensajesPilotoArea;
    private JLabel vueltasRestantesLabel;
    private JLabel desgasteNeumaticosLabel;
    private JLabel pronosticoCarreraLabel;
    private DataOutputStream salidaServidor;
    private InterfazMecanico interfazMecanico;
    private AgentController ingeniero_pista;
    private String vueltasRestantes = "";
    private JButton prepararPitstop;
    private PanelNeumaticos panelNeumaticos;
    private AgentContainer mainContainer;
    private JButton confirmarPitstop;

    public void setMainContainer(AgentContainer mainContainer) {
        this.mainContainer = mainContainer;
    }


    public InterfazIngeniero(DataOutputStream ss) {

        Font font = new Font("Arial", Font.BOLD, 20);
        this.salidaServidor = ss;
        setTitle("Interfaz Ingeniero");
        setSize(750, 790);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panelPrincipal = new JPanel(new GridLayout(5, 1));
        panelPrincipal.setLayout(null);
        this.setLayout(null);

        // Panel para mensajes del piloto
        JPanel panelMensajesPiloto = new JPanel(new BorderLayout());
        mensajesPilotoArea = new JTextArea();
        mensajesPilotoArea.setEditable(false);
        mensajesPilotoArea.setFont(font);
        panelMensajesPiloto.add(new JScrollPane(mensajesPilotoArea), BorderLayout.CENTER);
        JLabel titulo = new JLabel("Mensajes de la carrera");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(font);
        panelMensajesPiloto.add(titulo, BorderLayout.NORTH);
        panelMensajesPiloto.setPreferredSize(new Dimension(1000, 200));
        panelMensajesPiloto.setBounds(0, 0, 250, 375);

        panelPrincipal.add(panelMensajesPiloto);
        // Etiqueta "Información de la carrera"
        JLabel infoCarrera = new JLabel("Información de la carrera");
        infoCarrera.setBounds(370, 320, 500, 50);
        infoCarrera.setFont(font);
        panelPrincipal.add(infoCarrera);

        //Etiqueta "estado del coche"
        JLabel estadoCoche = new JLabel("Estado del coche");
        estadoCoche.setBounds(15, 400, 500, 50);
        estadoCoche.setFont(font);
        panelPrincipal.add(estadoCoche);

        // Panel para estado del coche (imagen)
        Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_neutro.png");
        //achicamos la imagen un poco
        ImageIcon imageIcon = (ImageIcon) icon;
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
        JLabel imagenCoche = new JLabel(icon);
        

        imagenCoche.setBounds(0, 350, 200, 460);
    
        panelPrincipal.add(imagenCoche);

        // Panel para vueltas restantes
        JPanel panelVueltasRestantes = new JPanel(null);
        vueltasRestantesLabel = new JLabel("Vueltas restantes:"+vueltasRestantes);
        vueltasRestantesLabel.setAlignmentX(CENTER_ALIGNMENT);
        vueltasRestantesLabel.setFont(font);
        vueltasRestantesLabel.setBounds(30, 30, 200, 50);
        panelVueltasRestantes.add(vueltasRestantesLabel);
        panelVueltasRestantes.setBounds(250, 355, 450, 70);
        panelPrincipal.add(panelVueltasRestantes);

        // Panel para desgaste de los neumáticos
        JPanel panelDesgasteNeumaticos = new JPanel(null);
        desgasteNeumaticosLabel = new JLabel("Desgaste de los neumáticos:");
        desgasteNeumaticosLabel.setAlignmentX(CENTER_ALIGNMENT);
        desgasteNeumaticosLabel.setFont(font);
        desgasteNeumaticosLabel.setBounds(30, 30, 400, 50);
        panelDesgasteNeumaticos.add(desgasteNeumaticosLabel);
        panelDesgasteNeumaticos.setBounds(250, 425, 450, 70);
        panelPrincipal.add(panelDesgasteNeumaticos);

        // Panel de neumáticos para pits
        panelNeumaticos = new PanelNeumaticos();

        panelNeumaticos.setSize(500, 380);
        panelNeumaticos.setBounds(250, 15, 450, 180);
       
        panelNeumaticos.setInterfazIngeniero(this);
        panelPrincipal.add(panelNeumaticos);

        // Panel para pronóstico de la carrera
        JPanel panelPronosticoCarrera = new JPanel(null);
        pronosticoCarreraLabel = new JLabel("Pronóstico:");
        pronosticoCarreraLabel.setAlignmentX(CENTER_ALIGNMENT);
        pronosticoCarreraLabel.setFont(font);
        pronosticoCarreraLabel.setBounds(30, 30, 400, 50);
        panelPronosticoCarrera.add(pronosticoCarreraLabel);
        panelPronosticoCarrera.setBounds(250, 495, 450, 70);
        panelPrincipal.add(panelPronosticoCarrera);

        // Botón para preparar pitstop
        prepararPitstop = new JButton("Preparar Pitstop");
        prepararPitstop.setFont(font);
        prepararPitstop.setBackground(Color.BLACK);
        prepararPitstop.setForeground(Color.WHITE);
        prepararPitstop.setBounds(380, 200, 200, 50);
        panelPrincipal.add(prepararPitstop);
        prepararPitstop.addActionListener(e -> {
            llamarMecanicos();
        });
        //Desactivamos el boton hasta que se seleccione un neumático
        prepararPitstop.setEnabled(false);

        //Boton para confirmar pitstop
        confirmarPitstop = new JButton("Mandar a Boxes");
        confirmarPitstop.setFont(font);
        confirmarPitstop.setBackground(Color.BLACK);
        confirmarPitstop.setForeground(Color.WHITE);
        confirmarPitstop.setBounds(380, 270, 200, 50);
        panelPrincipal.add(confirmarPitstop);

    
        confirmarPitstop.addActionListener(e -> {
            interfazMecanico.Pits();
        });

//Hasta que no se active el boton de mandar a boxes no se podra confirmar el pitstop
        confirmarPitstop.setEnabled(false);

        // Añadimos un borde a toda la ventana
        // Agregar los paneles al contenedor principal

        panelPrincipal.setBounds(15, 15, 1000, 1000);
        add(panelPrincipal);

    }
    public void activarBoton(){
        prepararPitstop.setEnabled(true);
    }
    public void activarBoxes(){
        confirmarPitstop.setEnabled(true);
        
    }

    public void setInterfazMecanico(InterfazMecanico interfazMecanico) {
        this.interfazMecanico = interfazMecanico;
    }

    private void llamarMecanicos() {
        // Lógica para llamar a los mecánicos
        //Obtener el neumático seleccionado
        Wheel_set ws = panelNeumaticos.getNeumaticosSeleccionados();
        Object[] args = new Object[2];
        args[0] = ws;
        args[1] = this;
        
      

        try {
            AgentController ingeniero_pista = mainContainer.createNewAgent("Ingeniero", "Agentes.Ingeniero_pista", args);
        this.ingeniero_pista = ingeniero_pista;
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        try {
            this.ingeniero_pista.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
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

    public void setIngeniero(AgentController ingeniero_pista) {
        this.ingeniero_pista = ingeniero_pista;
    }

    // Método para probar la interfaz
    public static void main(String[] args) {
        InterfazIngeniero interfazIngeniero = new InterfazIngeniero(null);
        interfazIngeniero.setVisible(true);
    }
}
