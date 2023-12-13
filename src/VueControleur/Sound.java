package VueControleur;
import javax.sound.sampled.*;
import java.io.File;

import java.io.IOException;

public class Sound {
    private Clip clip;
    private Clip clip2;

    private boolean son_clear = false;
    private boolean son_jeu = false;

    //String projectRoot = System.getProperty("user.dir");

    public String filePath ="/Users/janeaziz/Documents/L3/LIFAPOO/tetris2/tetris/src/VueControleur/Sons/clear.wav";
    public String filepath2 = "/Users/janeaziz/Documents/L3/LIFAPOO/tetris2/tetris/src/VueControleur/Sons/fullgame.wav";

    public Sound(String filePath) {
        try {
            // Open an audio input stream.

            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !son_clear) {
            son_clear = true;
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
            // Add a listener to reset isPlaying when playback completes
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    son_clear = false;
                }
            });
        }
    }

    public void stop() {
        clip.stop();
    }


    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    public void son_de_fond(String filepath2) {
        try {
            File soundFile2 = new File(filepath2);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile2.getAbsoluteFile());

            // Get a sound clip resource.
            clip2 = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip2.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void jouer_boucle(){
        if(clip2 != null && !son_jeu){
            son_jeu = true;
            clip2.setFramePosition(0);
            clip2.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void arreter_son2(){
        clip2.stop();
        son_jeu = false;
    }
}



