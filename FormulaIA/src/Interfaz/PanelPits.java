
package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Sonido.musica;

public class PanelPits extends JPanel {

    
    private JLabel gifLabel;
    private ImageIcon gifIcon;
    private Timer timer;
    private String estado = "";

    public PanelPits() {
        setSize(300, 300);
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar el GIF en el panel
        if (gifIcon != null){
            
        gifIcon.paintIcon(this, g, 0, 0);
        }
    }

    // Metodo para estado inicial
    public void setImagenInicial() {
        JLabel label = new JLabel("Mecánicos preparandose");
        label.setBounds(30, 100, 300, 100);
        label.setAlignmentY(CENTER_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        repaint();

        //Despues de 8 segundos corre setImagenPreparacion
        Timer time = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Mecánicos listos");
                //Esperar un segundo
                Timer time2 = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setImagenPreparacion();
            
                    }
                });
                time2.start();
                
            }
            
        });
        time.start();
        
    }

    //Metodo para imagen inicial
    public void setImagenPreparacion() {
        this.removeAll();
        JLabel label = new JLabel();
        Icon icon = new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\pitstop_inicio.png");
        label.setIcon(icon);
        label.setBounds(000, 000, 300, 300);
        label.setAlignmentY(CENTER_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        repaint();
        this.estado = "preparado";
    
    }

     // Metodo para correr el gif
    public void correrGif() {

        //Removemos todo
        this.removeAll();
    
        // Cargar el GIF
    
        gifIcon = new ImageIcon("FormulaIA\\src\\Interfaz\\Images\\pitstop_redux.gif");
        // Duración del GIF en milisegundos (ajusta según sea necesario)
        int gifDuration = 4270; // 5 segundos

        // Iniciar el temporizador para ejecutar una acción después de que el GIF termine
        musica.reproducirAudio("FormulaIA\\src\\Sonido\\sounds\\Ferrari's 1.97-Second Pit Stop _ 2018 Brazilian Grand Prix [OAd_t4wibM0].wav");
        timer = new Timer(gifDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setImagenFinal();
                timer.stop();
            }
        });
        timer.setRepeats(false); // Asegurarse de que el temporizador no se repita
        timer.start();
    }

    // Metodo para poner imagen final
    public void setImagenFinal() {
        this.removeAll();
        gifIcon = null;
        JLabel label = new JLabel("Pitstop completado");
        label.setBounds(100, 100, 300, 100);
        label.setAlignmentY(CENTER_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        repaint();
    }

    // Prueba del panel
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        PanelPits panel = new PanelPits();
        panel.setImagenPreparacion();
        panel.correrGif();
        frame.add(panel);
        panel.setVisible(true);
        frame.setVisible(true);
    }

    public String getEstado() {
        return this.estado;
    }

}
