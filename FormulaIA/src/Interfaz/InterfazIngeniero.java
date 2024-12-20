package Interfaz;

import javax.swing.*;
import javax.xml.crypto.Data;

import java.awt.event.MouseEvent;

import Agentes.Ingeniero_pista;
import Agentes.Wheel_set;
import Sonido.musica;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jade.wrapper.AgentContainer;
import Sonido.musica;

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
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private double desgasteNeumaticos = 0;
    private JLabel imagenCoche;
    ObjectOutputStream out;
    private int contadorAgente = 0;

    public void setMainContainer(AgentContainer mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setDesgasteNeumaticos(double desgasteNeumaticos) {
        this.desgasteNeumaticos = desgasteNeumaticos;
        
    }

    

    

    private void reiniciarScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
        scheduler = Executors.newScheduledThreadPool(1);
        iniciarMonitoreoDesgasteLlantas();
    }

    public void setVueltasRestantes(String vueltasRestantes) {
        vueltasRestantesLabel.setText(vueltasRestantes);
    }
    public void setLapsIniciales(String laps){
        vueltasRestantes = laps;
    }


    public InterfazIngeniero(DataOutputStream ss) throws Exception {
        this.salidaServidor = ss;

        Font font = new Font("Arial", Font.BOLD, 20);
        
        setTitle("Interfaz Ingeniero");
        setSize(1150, 790);
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
        panelMensajesPiloto.setBounds(0, 0, 550, 375);

        panelPrincipal.add(panelMensajesPiloto);
        // Etiqueta "Información de la carrera"
        JLabel infoCarrera = new JLabel("Información de la carrera");
        infoCarrera.setBounds(280, 380, 500, 50);
        infoCarrera.setFont(font);
        panelPrincipal.add(infoCarrera);

        //Etiqueta "estado del coche"
        JLabel estadoCoche = new JLabel("Estado del coche");
        estadoCoche.setBounds(15, 400, 500, 50);
        estadoCoche.setFont(font);
        panelPrincipal.add(estadoCoche);

        // Panel para estado del coche (imagen inicial)
        Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_neutro.png");
        //achicamos la imagen un poco
        ImageIcon imageIcon = (ImageIcon) icon;
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
        imagenCoche = new JLabel(icon);
        

        imagenCoche.setBounds(0, 350, 200, 460);
    
        panelPrincipal.add(imagenCoche);

        // Panel para vueltas restantes
        JPanel panelVueltasRestantes = new JPanel(null);
        vueltasRestantesLabel = new JLabel("Vueltas restantes:"+vueltasRestantes);
        vueltasRestantesLabel.setAlignmentX(CENTER_ALIGNMENT);
        vueltasRestantesLabel.setFont(font);
        vueltasRestantesLabel.setBounds(30, 30, 200, 50);
        panelVueltasRestantes.add(vueltasRestantesLabel);
        panelVueltasRestantes.setBounds(250, 400, 450, 70);
        panelPrincipal.add(panelVueltasRestantes);

        // Panel para desgaste de los neumáticos
        JPanel panelDesgasteNeumaticos = new JPanel(null);
        desgasteNeumaticosLabel = new JLabel("Desgaste de los neumáticos:");
        desgasteNeumaticosLabel.setAlignmentX(CENTER_ALIGNMENT);
        desgasteNeumaticosLabel.setFont(font);
        desgasteNeumaticosLabel.setBounds(30, 30, 400, 50);
        panelDesgasteNeumaticos.add(desgasteNeumaticosLabel);
        panelDesgasteNeumaticos.setBounds(250, 475, 450, 70);
        panelPrincipal.add(panelDesgasteNeumaticos);

        // Panel de neumáticos para pits
        panelNeumaticos = new PanelNeumaticos();

        panelNeumaticos.setSize(500, 380);
        panelNeumaticos.setBounds(600, 15, 400, 180);
       
        panelNeumaticos.setInterfazIngeniero(this);
        panelNeumaticos.BloquearSeleccionNeumaticos();
        panelPrincipal.add(panelNeumaticos);

       

        // Botón para preparar pitstop
        prepararPitstop = new JButton("Preparar Pitstop");
        prepararPitstop.setFont(font);
        prepararPitstop.setBackground(Color.BLACK);
        prepararPitstop.setForeground(Color.WHITE);
        prepararPitstop.setBounds(700, 220, 200, 50);
        panelPrincipal.add(prepararPitstop);
        prepararPitstop.addActionListener(e -> {
            musica.reproducirAudio("FormulaIA\\src\\Sonido\\sounds\\F1 Radio Notification Sound.wav");
            llamarMecanicos();
        });
        //Desactivamos el boton hasta que se seleccione un neumático
        prepararPitstop.setEnabled(false);

        //Boton para confirmar pitstop
        confirmarPitstop = new JButton("Mandar a Boxes");
        confirmarPitstop.setFont(font);
        confirmarPitstop.setBackground(Color.BLACK);
        confirmarPitstop.setForeground(Color.WHITE);
        confirmarPitstop.setBounds(700, 290, 200, 50);
        panelPrincipal.add(confirmarPitstop);

    
        confirmarPitstop.addActionListener(e -> {
            musica.reproducirAudio("FormulaIA\\src\\Sonido\\sounds\\F1 Radio Notification Sound.wav");
            confirmarPitstop.setEnabled(false);
            confirmarPitstop.setVisible(false);
            
           
            try {
                salidaServidor.writeUTF("Entra a boxes");
            } catch (Exception h) {
            }
        });

//Hasta que no se active el boton de mandar a boxes no se podra confirmar el pitstop
        confirmarPitstop.setEnabled(false);

        // Añadimos un borde a toda la ventana
        // Agregar los paneles al contenedor principal

        panelPrincipal.setBounds(15, 15, 1000, 1000);
        add(panelPrincipal);

    }

    

    public void desbloqueoPits(){
        panelNeumaticos.DesbloquearSeleccionNeumaticos();
        interfazMecanico.DesbloquearSeleccionNeumaticos();
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
        prepararPitstop.setEnabled(false);
        //Obtener el neumático seleccionado
        Wheel_set ws = panelNeumaticos.getNeumaticosSeleccionados();
        Object[] args = new Object[2];
        args[0] = ws;
        args[1] = this;
        
      this.panelNeumaticos.BloquearSeleccionNeumaticos();

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
        if(mensaje.contains("Vuelta")){
        }
        if(mensaje.contains("subió")){
            musica.reproducirAudio("FormulaIA\\src\\Sonido\\sounds\\Arcade-8-bit-jump-813.wav");
        }
        if(mensaje.contains("bajó")){
            musica.reproducirAudio("FormulaIA\\src\\Sonido\\sounds\\Arcade-8-bit-death-289.wav");
        }

        mensajesPilotoArea.append(mensaje + "\n");
    }

    // Método para actualizar las vueltas restantes
    public void actualizarVueltasRestantes(String vueltas) {
        vueltasRestantesLabel.setText(vueltas);
    }

    // Método para actualizar el desgaste de los neumáticos
    public void actualizarDesgasteNeumaticos(String desgaste) {
        try {
            // Eliminar la palabra "desgaste" del string de entrada
            String desgasteSinPalabra = desgaste.replace("desgaste ", "").trim();
            double desgasteDouble = Double.parseDouble(desgasteSinPalabra);
            String desgasteFormateado = decimalFormat.format(desgasteDouble);
            desgasteNeumaticosLabel.setText("Desgaste de los Neumaticos: "+desgasteFormateado + "%");
        } catch (NumberFormatException e) {
            // Manejar el error si el desgaste no es un número válido
            desgasteNeumaticosLabel.setText("Error");
            //en caso de error se reinicia el scheduler
            reiniciarScheduler();
        }
    }

    // Método para actualizar el pronóstico de la carrera
    public void actualizarPronosticoCarrera(String pronostico) {
        pronosticoCarreraLabel.setText(pronostico);
    }

    public void setIngeniero(AgentController ingeniero_pista) {
        this.ingeniero_pista = ingeniero_pista;
    }

    public void iniciarMonitoreoDesgasteLlantas() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(desgasteNeumaticos>100){
                    
                    try {
                        salidaServidor.writeUTF("fuera");
                    } catch (Exception e) {
                        System.out.println("Error al enviar la solicitud de preugabas: " + e.getMessage());
                        e.printStackTrace();
                        reiniciarScheduler();
                    }
                    scheduler.shutdown();
                }else{
                    try {
                        salidaServidor.writeUTF("desgaste");
                        solicitarDesgasteLlantas();
                    } catch (Exception e) {
                        System.out.println("Error al enviar la solicitud de preugabas: " + e.getMessage());
                        e.printStackTrace();
                        reiniciarScheduler();

                    }
                    

                }
                
                
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }


    private void solicitarDesgasteLlantas() {
        // Aquí debes implementar la lógica para solicitar el desgaste de las llantas
        //Dependiendo del valor del desgaste se sustituye la imagen de coche estado
        if(desgasteNeumaticos<15 && desgasteNeumaticos>0){
            Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_excelente.png");
            //achicamos la imagen un poco
            ImageIcon imageIcon = (ImageIcon) icon;
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
            imagenCoche.setIcon(icon);
        }
        if(desgasteNeumaticos>16 && desgasteNeumaticos<50){
            Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_bien.png");
            //achicamos la imagen un poco
            ImageIcon imageIcon = (ImageIcon) icon;
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
            imagenCoche.setIcon(icon);
        }
        if(desgasteNeumaticos>51 && desgasteNeumaticos<70){
            Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_cuidado.png");
            //achicamos la imagen un poco
            ImageIcon imageIcon = (ImageIcon) icon;
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
            imagenCoche.setIcon(icon);
        }
        if(desgasteNeumaticos>71 && desgasteNeumaticos<90){
            Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_precaucion.png");
            //achicamos la imagen un poco
            ImageIcon imageIcon = (ImageIcon) icon;
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
            imagenCoche.setIcon(icon);
        }
        if(desgasteNeumaticos>91 && desgasteNeumaticos<100){
            Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_moribundo.png");
            //achicamos la imagen un poco
            ImageIcon imageIcon = (ImageIcon) icon;
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
            imagenCoche.setIcon(icon);
        }

        if( desgasteNeumaticos>100){
            Icon icon = new ImageIcon("FormulaIA/src/Interfaz/Images/carro_fuera.png");
            //achicamos la imagen un poco
            ImageIcon imageIcon = (ImageIcon) icon;
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 250, Image.SCALE_DEFAULT));
            imagenCoche.setIcon(icon);
        }
       
    }

    // Método para probar la interfaz
    public static void main(String[] args) {
        InterfazIngeniero interfazIngeniero;
        try {
            interfazIngeniero = new InterfazIngeniero(null);
            
        interfazIngeniero.setVisible(true);
        interfazIngeniero.iniciarMonitoreoDesgasteLlantas();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void cambioLlantas() {

        //Obtenemos neumaticos seleccionados
        Wheel_set ws = panelNeumaticos.getNeumaticosSeleccionados();

        //Cambiamos las llantas de la interfaz cliente
        try {
            salidaServidor.writeUTF(ws.getNombre());
        } catch (Exception e) {
        }
    }
}
