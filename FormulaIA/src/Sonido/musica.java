package Sonido;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class musica {
    private static Clip clip;

    public static void reproducirAudio(String archivo) {
         // Detener cualquier música que esté sonando antes de reproducir un nuevo audio
        try {
            File musica = new File(archivo);
            AudioInputStream canalAudio = AudioSystem.getAudioInputStream(musica);
            clip = AudioSystem.getClip();
            clip.open(canalAudio);
            clip.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }

    public static void detenerMusica() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public static void loopMusica(String archivo) {
        detenerMusica(); // Detener cualquier música que esté sonando antes de reproducir un nuevo audio
        try {
            File musica = new File(archivo);
            AudioInputStream canalAudio = AudioSystem.getAudioInputStream(musica);
            clip = AudioSystem.getClip();
            clip.open(canalAudio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }

    public static void reproducirSonido(String archivo) {
        try {
            File sonido = new File(archivo);
            AudioInputStream canalAudio = AudioSystem.getAudioInputStream(sonido);
            Clip sonidoClip = AudioSystem.getClip();
            sonidoClip.open(canalAudio);
            sonidoClip.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }

    public static void ajustarVolumen(float nivel) {
        if (clip != null) {
            FloatControl controlVolumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            controlVolumen.setValue(nivel);
        }
    }
}
