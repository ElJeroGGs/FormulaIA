package Interfaz;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Agentes.Wheel_set;

import java.awt.event.MouseAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class PanelNeumaticos extends JPanel {
    private JPanel selectedPanel;
    private InterfazIngeniero iIng;
    private Interfaz.Cliente clt;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private boolean bloqueo = false;

    // Metodo para activar boton de preparar pitstop
    public void activarBotonPrepararPitstop() {
        iIng.activarBoton();
    }

    public void setCliente(Interfaz.Cliente clt) {
        this.clt = clt;
    }

    public void activarBoton() {
        clt.activarBoton();
    }

    public void setInterfazIngeniero(InterfazIngeniero iIng) {
        this.iIng = iIng;
    }

    // Creamos un panel con los 3 minipaneles
    public PanelNeumaticos() {
        setLayout(new GridLayout(1, 3));

        // Creamos neumaticos genericos

        Wheel_set ws;
        ws = new Wheel_set();
        ws.setNombre("Neumaticos Blandos");
        ws.setDuracion(20);
        panel1 = PanelNeumaticoSolo(ws);
        add(panel1);
        ws = new Wheel_set();
        ws.setNombre("Neumaticos Medios");
        ws.setDuracion(30);
        panel2 = PanelNeumaticoSolo(ws);
        add(panel2);
        ws = new Wheel_set();
        ws.setNombre("Neumaticos Duros");
        ws.setDuracion(40);
        panel3 = PanelNeumaticoSolo(ws);
        add(panel3);

        setSize(270, 150);
        ;

    }

    public JPanel PanelNeumaticoSolo(Wheel_set neumaticos) {

        Font font = new Font("Arial", Font.BOLD, 11);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(100, 100);

        // Colocoamos el nombre del neumatico

        panel.add(new JLabel(neumaticos.getNombre()));
        panel.getComponent(0).setFont(font);

        // Imagen del neumático
        String filename = "FormulaIA\\src\\Interfaz\\Images\\" + neumaticos.getNombre().replace(" ", "_") + ".png";

        Icon iconoBlando = new ImageIcon(filename);
        JLabel imagenBlando = new JLabel(iconoBlando);
        panel.add(imagenBlando);

        // Informacion de las vueltas
        panel.add(new JLabel("Vueltas estimadas: " + neumaticos.getDuracion()));
        panel.getComponent(2).setFont(font);
        // Informacion del desgaste

        // Ordenamos la disposicion de las cosas
        // Nombre
        panel.getComponent(0).setBounds(20, 10, 140, 25);
        // Imagen
        panel.getComponent(1).setBounds(30, 20, 90, 95);
        // Vueltas
        panel.getComponent(2).setBounds(10, 100, 130, 25);

        // Añadir MouseListener para permitir la selección del panel
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (panel.isEnabled()) {
                    seleccionarPanel(panel);
                    if (iIng != null) {
                        activarBotonPrepararPitstop();
                    }
                    if (clt != null) {
                        activarBoton();
                    }
                }
            }
        });

        return panel;

    }

    public void BloquearSeleccionNeumaticos() {
        bloqueo = true;
        // les quitamos los listeners a los paneles
        panel1.removeMouseListener(panel1.getMouseListeners()[0]);
        panel2.removeMouseListener(panel2.getMouseListeners()[0]);
        panel3.removeMouseListener(panel3.getMouseListeners()[0]);
        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);
        panel3.setBackground(Color.LIGHT_GRAY);
        repaint();
    }

    private void seleccionarPanel(JPanel panel) {
        if (selectedPanel != null) {
            selectedPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }
        selectedPanel = panel;
        selectedPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
    }

    public Wheel_set getNeumaticosSeleccionados() {
        if (selectedPanel == null) {
            return null;
        }

        JLabel label = (JLabel) selectedPanel.getComponent(0);
        String nombre = label.getText();
        int duracion = Integer.parseInt(((JLabel) selectedPanel.getComponent(2)).getText().split(" ")[2]);

        return new Wheel_set(nombre, duracion);
    }

    public void seleccionarNeumaticos(String neumaticos) {
        if (bloqueo) {
            return;
        }
        // Seleccionar panel
        for (Component component : getComponents()) {
            JPanel panel = (JPanel) component;
            JLabel label = (JLabel) panel.getComponent(0);
            if (label.getText().equals(neumaticos)) {
                seleccionarPanel(panel);
                break;
            }
        }
    }

    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear el marco principal
                JFrame frame = new JFrame("Prueba Panel Neumáticos");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Crear una instancia de PanelNeumaticos y agregarla al marco
                PanelNeumaticos panel = new PanelNeumaticos();
                frame.add(panel);
                // Ajustar el tamaño del marco según los componentes
                frame.setBounds(0, 0, 450, 180);

                // Centrar el marco en la pantalla
                frame.setLocationRelativeTo(null);

                // Hacer visible el marco
                frame.setVisible(true);
                panel.BloquearSeleccionNeumaticos();
            }

        });
    }

    public void DesbloquearSeleccionNeumaticos() {
        bloqueo = false;
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (panel1.isEnabled()) {
                    seleccionarPanel(panel1);
                    if (iIng != null) {
                        activarBotonPrepararPitstop();
                    }
                    if (clt != null) {
                        activarBoton();
                    }
                }
            }
        });
        panel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (panel2.isEnabled()) {
                    seleccionarPanel(panel2);
                    if (iIng != null) {
                        activarBotonPrepararPitstop();
                    }
                    if (clt != null) {
                        activarBoton();
                    }
                }
            }
        });
        panel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (panel3.isEnabled()) {
                    seleccionarPanel(panel3);
                    if (iIng != null) {
                        activarBotonPrepararPitstop();
                    }
                    if (clt != null) {
                        activarBoton();
                    }
                }
            }
        });
        panel1.setBackground(Color.WHITE);
        panel2.setBackground(Color.WHITE);
        panel3.setBackground(Color.WHITE);
        repaint();
    }

    // Ejecutar en el hilo de despacho de eventos de Swing

    // Ejecutar

}
