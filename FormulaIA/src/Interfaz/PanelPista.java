package Interfaz;

import javax.swing.*;

import Agentes.piloto;

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

    private java.util.List<Point> checkpoints = new ArrayList<>(); // Lista de puntos de control
    private int MOVE_SPEED = 9;
    private int currentCheckpointIndex = 0;
    private boolean enPista = false;private JLabel overlayLabel;
    private List<ImageIcon> overlayIcons;
    private int overlayIndex = 0;
    private InterfazPiloto interfazPiloto;
    private int NumeroVueltas = 30; // Número de vueltas
    private int vueltasCompletadas = 0; // Contador de vueltas completadas
    private long startTime; // Tiempo de inicio de la vuelta
    private List<Long> lapTimes = new ArrayList<>(); // Lista de tiempos de vuelta
    private double desgasteNeumaticos = 0.0;
private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
private boolean finCarrera = false;
private List<String> etiquetas = new ArrayList<>();
private boolean usarPits = false;
private boolean dentroPits = false;
private boolean enTaller = false;
private int contador = 0;
private piloto pilo;
private boolean saliendoPitLane = false;
private int posicion;

public void setVueltas(int vueltas){
    this.NumeroVueltas = vueltas;
}
public boolean getFinCarrera(){
    return finCarrera;
}
    public void setFinCarrera(boolean finCarrera){
        this.finCarrera = finCarrera;
    }

    public void setVelocidad(int velocidad) {
        this.MOVE_SPEED = velocidad;
    }

    public void EntrarBoxes() {
        usarPits = true;
    }

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

    private void solicitarDesgasteLlantas() {
        // Aquí debes implementar la lógica para solicitar el desgaste de las llantas
        System.out.println("Solicitando desgaste de llantas...");
        
        if (desgasteNeumaticos > 100) {
            desgasteNeumaticos = 100;
        }
    }

    public void iniciarMonitoreoDesgasteLlantas() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    solicitarDesgasteLlantas();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Reiniciar el scheduler si ocurre una excepción
                    reiniciarScheduler();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    public int getNumeroVueltas() {
        return this.NumeroVueltas ;
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

     public void iniciarCarrera() {
        if(interfazPiloto != null){
            //Tomamos un numero al azar entre 1 y 20 
           
            interfazPiloto.iniciarCarrera();
        }

        // Programar la tarea para ejecutar setDesgasteNeumaticos 
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(finCarrera){
                    scheduler.shutdown();
                    //deja de monitorear
                }else{
                    
                interfazPiloto.setDesgasteNeumaticos();
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
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
                System.out.println(clickX + "," + clickY + " pits");
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

      // Tomar un número al azar entre 5 y 20
        Random rand = new Random();
        int numero = rand.nextInt(16) + 5;
        setPosicion(numero);
        //Clasificacion
        interfazPiloto.setClasificacion();
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
                    if(interfazPiloto != null){
                    activarBoton(); } 
                    iniciarCarrera();      
                    startTime = System.currentTimeMillis(); 
                    // Iniciar el temporizador para comenzar la vuelta
                    timer = new Timer(20, PanelPista.this);
                    timer.start();
                }
                
            }
        });
        overlayTimer.setRepeats(true);
        overlayTimer.start();

        repaint();
    }

    public String getLapTime(){
        long lapTime = System.currentTimeMillis() - startTime;
        long minutes = (lapTime / 60000) % 60;
        long seconds = (lapTime / 1000) % 60;
        long milliseconds = lapTime % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
    }

    public void loadCheckpointsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String[] coords = parts[0].split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                checkpoints.add(new Point(x, y));
                if (parts.length > 1) {
                    etiquetas.add(parts[1]);
                } else {
                    etiquetas.add("");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsarPits(boolean usarPits) {
        this.usarPits = usarPits;
    }

    public boolean getSalidaPitLane() {
        return saliendoPitLane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
       
        if (desgasteNeumaticos > 100) {
            timer.stop();
            interfazPiloto.setFinalCarrera(true);
        }
    
        if (!checkpoints.isEmpty() && currentCheckpointIndex < checkpoints.size()) {

        
    
            Point target = checkpoints.get(currentCheckpointIndex);
            String etiqueta = etiquetas.get(currentCheckpointIndex);

            
            
            // Verificar si se debe tomar la ruta de los pits
    if (usarPits) {
        if (etiqueta.equals("pits")) {
            dentroPits = true;
        } else if (etiqueta.equals("taller")){
            if(contador < 1){
                try {
                    if(interfazPiloto != null){
                        interfazPiloto.EntrandoTaller();
                    }
                    
                    Thread.sleep(5000);
            
                } catch (InterruptedException j) {
                    j.printStackTrace();
                }
                contador++;
                if(interfazPiloto != null){
                    interfazPiloto.saliendoTaller();
                }
            }
            
        }else if (dentroPits) {
            // Ignorar puntos sin etiqueta hasta encontrar "salida"
            while (!etiqueta.equals("salida")) {
                currentCheckpointIndex++;
                if (currentCheckpointIndex == checkpoints.size() - 1) {
                    currentCheckpointIndex = 0;
                    // Se completó una vuelta
        vueltasCompletadas++;
        long lapTime = System.currentTimeMillis() - startTime; // Calcular el tiempo de la vuelta
        lapTimes.add(lapTime);
        startTime = System.currentTimeMillis();
        if(interfazPiloto != null){
        calculaPosicion();
        interfazPiloto.VueltaCompletada();
        interfazPiloto.repintarVueltas(); // Reiniciar el tiempo de inicio para la siguiente vuelta
        }
    
                }
                etiqueta = etiquetas.get(currentCheckpointIndex);
            }
            dentroPits = false;
            usarPits = false;
            //System.out.println("Saliendo de pits");
            if(interfazPiloto != null){
                interfazPiloto.saliendoPits();
            }
        } else if (etiqueta.equals("salida")) {
            currentCheckpointIndex++;
        }

        
    } else {
        // Ignorar puntos con etiquetas "pits" o "salida" si usarPits es false
        while (etiqueta.equals("pits") || etiqueta.equals("salida") || etiqueta.equals("taller")) {
            currentCheckpointIndex++;
            if (currentCheckpointIndex == checkpoints.size() - 1) {
                currentCheckpointIndex = 0;
            }
            etiqueta = etiquetas.get(currentCheckpointIndex);
        }
    }

    target = checkpoints.get(currentCheckpointIndex);
            // Calcular la dirección hacia el punto de control
            int dx = target.x - x;
            int dy = target.y - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
    
            if (distance > MOVE_SPEED) {
                // Mover en dirección al objetivo
                int movAux = MOVE_SPEED;
                int movPits = 2;
                if(dentroPits){
                    x += (int) (dx / distance * movPits);
                y += (int) (dy / distance * movPits);
                } else{
                    x += (int) (dx / distance * movAux);
                y += (int) (dy / distance * movAux);

                
                }
                
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
                    lapTimes.add(lapTime);
                    calculaPosicion();
                    startTime = System.currentTimeMillis(); // Reiniciar el tiempo de inicio para la siguiente vuelta
                }
            }
    // Si se completó una vuelta
    if (currentCheckpointIndex == checkpoints.size()-1) {
        currentCheckpointIndex = 0;
        vueltasCompletadas++;
        long lapTime = System.currentTimeMillis() - startTime; // Calcular el tiempo de la vuelta
        lapTimes.add(lapTime);
        startTime = System.currentTimeMillis();
        calculaPosicion();
        interfazPiloto.VueltaCompletada();
        interfazPiloto.repintarVueltas(); // Reiniciar el tiempo de inicio para la siguiente vuelta
    }

    //Terminar si se completaron todas las vueltas
    if (vueltasCompletadas == NumeroVueltas) {
        timer.stop();
        finCarrera = true;
        interfazPiloto.setFinalCarrera(true);
    }
    
            // Redibujar el panel para mostrar la nueva posición de la bolita
            repaint();
        }
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
    public void calculaPosicion() {
        if (lapTimes.size() < 2) {
            //Tomamos un numero random entre -5 y 5
            Random rand = new Random();
            int res = rand.nextInt(11) - 5;

            //sumamos a la posicion actual
            NuevaPosicion(res);
            return; // No hay suficientes vueltas para comparar
        }

        //Tomamos un promedio de todos los tiempos
        long promedio = 0;
        for (int i = 0; i < lapTimes.size(); i++) {
            promedio += lapTimes.get(i);
        }
        promedio /= lapTimes.size();
        //Comparamos el tiempo de la ultima vuelta con el promedio
        long ultimaVuelta = lapTimes.get(lapTimes.size() - 1);
        if (ultimaVuelta < promedio) {
            NuevaPosicion(-1);
        } else {
            NuevaPosicion(1);
        }
    }

    public void NuevaPosicion(int pos){
        
        if(this.posicion + pos >= 20){
            this.posicion = 20;
    } else if(this.posicion + pos <= 1){
        this.posicion = 1;
    } else {
        this.posicion += pos;
    }

    //Si la nueva posicion es menor a la actual, se sube de posicion
    if(pos < 0){
        interfazPiloto.subioPosicion();
    } else if (pos > 0){
        interfazPiloto.bajoPosicion();
    }
    

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

        /*// Dibujar los puntos de control
        g.setColor(Color.BLUE);
        for (Point checkpoint : checkpoints) {
            g.fillOval(checkpoint.x - 5, checkpoint.y - 5, 10, 10);
        }
        // Dibujar los puntos de control pits
        g.setColor(Color.GREEN);
        for (int i = 0; i < checkpoints.size(); i++) {
            if (etiquetas.get(i).equals("pits") || etiquetas.get(i).equals("taller") || etiquetas.get(i).equals("salida"))  {
                g.fillOval(checkpoints.get(i).x - 5, checkpoints.get(i).y - 5, 10, 10);
            }
        }*/

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
            PanelPista panel = new PanelPista( "china"); // Coordenadas iniciales
            panel.EntrarBoxes();
            frame.add(panel);
            
            frame.setVisible(true);
            panel.startLap();
            
        });
    }

    public void setDesgasteNeumaticos(double desgaste) {
        this.desgasteNeumaticos = desgaste;
    }

    public boolean getDentroPits() {
        return dentroPits;
    }
    public String getPosicion() {

        int num = this.posicion;
        String pos = num+"";

        switch (num) {
            case 1:
                pos+="ra";
                break;
            case 2:
                pos+="da";
                break;
            case 3:
                pos+="ra";
                break;
            case 4:
                pos+="ta";
                break;
            case 5:
                pos+="ta";
                break;
            case 6:
                pos+="ta";
                break;
            case 7:
                pos+="ma";
                break;
            case 8:
                pos+="va";
                break;
            case 9:
                pos+="na";
                break;
            case 10:
                pos+="ma";
                break;
            case 11:
                pos+="va";
                break;
            case 12:
                pos+="va";
                break;
            case 13:
                pos+="va";
                break;
            case 14:
                pos+="ta";
                break;
            case 15:
                pos+="ta";
                break;
            case 16:
                pos+="ta";
                break;
            case 17:
                pos+="ma";
                break;
            case 18:
                pos+="va";
                break;
            case 19:
                pos+="na";
                break;
            case 20:
                pos+="ma";
                break;
        }

        pos+=" posición";
        return pos;
    }

    public void setPosicion(int p){
        this.posicion = p;

    }
}
