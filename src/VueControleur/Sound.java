package VueControleur;
import javax.sound.sampled.*;
import java.io.File;

import java.io.IOException;

public class Sound {
    private Clip clip;
    private boolean isPlaying = false;

    String projectRoot = System.getProperty("user.dir");

    public String filePath= projectRoot+"/src/VueControleur/clear.wav";
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
        if (clip != null && !isPlaying) {
            isPlaying = true;
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
            // Add a listener to reset isPlaying when playback completes
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    isPlaying = false;
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
}
