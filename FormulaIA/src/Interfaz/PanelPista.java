package Interfaz;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class PanelPista extends JPanel implements ActionListener {
    private int x;
    private int y;
    private Timer timer;
    private final int BALL_SIZE = 10;
    private BufferedImage pistaImage;
    private static final int JUMP_SIZE = 1;

    private java.util.List<Point> checkpoints = new ArrayList<>(); // Lista de puntos de control
    private static final int MOVE_SPEED = 4;
    private int currentCheckpointIndex = 0;
    private boolean enPista = false;private JLabel overlayLabel;
    private List<ImageIcon> overlayIcons;
    private int overlayIndex = 0;
    private InterfazPiloto interfazPiloto;
    private int NumeroVueltas = 5; // Número de vueltas
    private int vueltasCompletadas = 0; // Contador de vueltas completadas
    private long startTime; // Tiempo de inicio de la vuelta
    private List<Long> lapTimes = new ArrayList<>(); // Lista de tiempos de vuelta
    private double desgasteNeumaticos = 0.0;
private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    

    public List<String> getLapTimes(){
        List<String> lapTimesStr = new ArrayList<>();
        for (int i = 0; i < lapTimes.size(); i++) {
            long lapTime = lapTimes.get(i);
            long minutes = (lapTime / 60000) % 60;
            long seconds = (lapTime / 1000) % 60;
            long milliseconds = lapTime % 1000;
            lapTimesStr.add(String.format("Vuelta %d: %02d:%02d.%03d\n", i + 1, minutes, seconds, milliseconds));
        }
        return lapTimesStr;
    }

    public int getNumeroVueltas() {
        return this.NumeroVueltas ;
    }

     public void iniciarCarrera() {
        // Lógica para iniciar la carrera
        System.out.println("Carrera iniciada");

        // Programar la tarea para ejecutar setDesgasteNeumaticos cada 5 segundos
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                interfazPiloto.setDesgasteNeumaticos();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public int getVueltasCompletadas() {
        return vueltasCompletadas;
    }


    public void setInterfazPiloto(InterfazPiloto interfazPiloto) {
        this.interfazPiloto = interfazPiloto;
    }

    public void activarBoton() {
        interfazPiloto.activarBoton();
    }

    public PanelPista(String circuito) {
        String checkpointsFile = "FormulaIA\\src\\Interfaz\\Circuitos\\" + circuito + ".txt";
        String fileImage = "FormulaIA\\src\\Interfaz\\Images\\circuito_" + circuito + ".png";
      
        this.setBounds(getVisibleRect());
        try {
            pistaImage = ImageIO.read(new File(fileImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Cargar puntos de control desde el archivo
        loadCheckpointsFromFile(checkpointsFile);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickX = e.getX();
                int clickY = e.getY();
                System.out.println(clickX + "," + clickY);
            }
        });

         // Crear el JLabel para la imagen superpuesta
         overlayLabel = new JLabel();
         overlayLabel.setBounds(0, 0, getWidth(), getHeight());
         overlayLabel.setVisible(false);
         add(overlayLabel);

         // Cargar las imágenes de la secuencia
        overlayIcons = new ArrayList<>();
        overlayIcons.add(new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\lights_out_1.png"));
        overlayIcons.add(new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\lights_out_2.png"));
        overlayIcons.add(new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\lights_out_3.png"));
        overlayIcons.add(new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\lights_out_4.png"));
        overlayIcons.add(new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\lights_out_5.png"));
        overlayIcons.add(new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\lights_out_1.png"));
    }

    public void startLap() {
        enPista = true;
        currentCheckpointIndex = 0;
        x = checkpoints.get(0).x;
        y = checkpoints.get(0).y;// Iniciar el cronómetro

        // Mostrar la primera imagen de la secuencia
        overlayIndex = 0;
        overlayLabel.setIcon(overlayIcons.get(overlayIndex));
        overlayLabel.setVisible(true);

        // Crear un temporizador para cambiar la imagen cada 1 segundo
        Timer overlayTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overlayIndex++;
                if (overlayIndex < overlayIcons.size()) {
                    overlayLabel.setIcon(overlayIcons.get(overlayIndex));
                    // Si es la última iteración, ajustar el temporizador para que dure menos tiempo
                    if (overlayIndex == overlayIcons.size() - 1) {
                        ((Timer) e.getSource()).setDelay(500); // Duración de la última imagen
                    }
                } else {
                    ((Timer) e.getSource()).stop();
                    overlayLabel.setVisible(false);
                    activarBoton();  
                    iniciarCarrera();      
                    startTime = System.currentTimeMillis(); 
                    // Iniciar el temporizador para comenzar la vuelta
                    timer = new Timer(30, PanelPista.this);
                    timer.start();
                }
                
            }
        });
        overlayTimer.setRepeats(true);
        overlayTimer.start();

        repaint();
    }
    private void loadCheckpointsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int checkpointX = Integer.parseInt(parts[0].trim());
                    int checkpointY = Integer.parseInt(parts[1].trim());
                    checkpoints.add(new Point(checkpointX, checkpointY));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(desgasteNeumaticos > 100){
            timer.stop();
        }

        if (!checkpoints.isEmpty() && currentCheckpointIndex < checkpoints.size()) {
            Point target = checkpoints.get(currentCheckpointIndex);

            // Calcular la dirección hacia el punto de control
            int dx = target.x - x;
            int dy = target.y - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > MOVE_SPEED) {
                // Mover en dirección al objetivo
                x += (int) (dx / distance * MOVE_SPEED);
                y += (int) (dy / distance * MOVE_SPEED);
            } else {
                // Llegar al punto de control
                x = target.x;
                y = target.y;
                currentCheckpointIndex++; // Avanzar al siguiente punto

                // Si se completó una vuelta
                if (currentCheckpointIndex >= checkpoints.size()) {
                    currentCheckpointIndex = 0;
                    vueltasCompletadas++;
                    long lapTime = System.currentTimeMillis() - startTime; // Calcular el tiempo de la vuelta
                    lapTimes.add(lapTime); // Almacenar el tiempo de la vuelta
                    startTime = System.currentTimeMillis(); // Reiniciar el cronómetro
                    interfazPiloto.repintarVueltas();
                    interfazPiloto.VueltaCompletada();
                    if (vueltasCompletadas >= NumeroVueltas) {
                        timer.stop(); // Detener el temporizador cuando se completen todas las vueltas
                        enPista = false;
                    }
                }
            }
        }

        repaint();
    }

    public String getTimePerLap() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lapTimes.size(); i++) {
            long lapTime = lapTimes.get(i);
            long minutes = (lapTime / 60000) % 60;
            long seconds = (lapTime / 1000) % 60;
            long milliseconds = lapTime % 1000;
            sb.append(String.format("Vuelta %d: %02d:%02d.%03d\n", i + 1, minutes, seconds, milliseconds));
        }
        return sb.toString();
    }

    public String getCurrentLapTime() {
        long currentTime = System.currentTimeMillis();
        long currentLapTime = currentTime - startTime;
        long minutes = (currentLapTime / 60000) % 60;
        long seconds = (currentLapTime / 1000) % 60;
        long milliseconds = currentLapTime % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar la imagen de fondo (si se carga una)
        if (pistaImage != null) {
            g.drawImage(pistaImage, 0, 0, this);
        }

       

        // Dibujar la "bolita" si está en pista
        if (enPista) {
            g.setColor(Color.RED);
            g.fillOval(x - BALL_SIZE / 2, y - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE);
        }
    }

    



    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prueba de Puntos de Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 350);

            // Ruta del archivo con los puntos de control
            String checkpointsFile = "FormulaIA\\src\\Interfaz\\Circuitos\\usa.txt";
            PanelPista panel = new PanelPista( "usa"); // Coordenadas iniciales
            frame.add(panel);
            
            frame.setVisible(true);
            panel.startLap();
            
        });
    }

    public void setDesgasteNeumaticos(double desgaste) {
        this.desgasteNeumaticos = desgaste;
    }
}
