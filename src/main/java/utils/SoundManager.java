package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the playback of sound effects and background music.
 */
public class SoundManager {

    /**
     * Cache for storing loaded sound clips.
     */
    private static final Map<String, AudioClip> soundCache = new HashMap<>();

    /**
     * Player for the background music.
     */
    private static MediaPlayer bgmPlayer;

    /**
     * Plays a short sound effect.
     * Loads the sound from cache or file system if necessary.
     *
     * @param fileName The name of the sound file to play
     */
    public static void playSound(String fileName) {
        String path = "/assets/sounds/" + fileName;

        try {
            AudioClip clip = soundCache.get(path);

            if (clip == null) {
                if (SoundManager.class.getResource(path) == null) {
                    System.err.println("Sound not found: " + path);
                    return;
                }

                String uri = Objects.requireNonNull(SoundManager.class.getResource(path)).toExternalForm();
                clip = new AudioClip(uri);
                soundCache.put(path, clip);
            }

            clip.setVolume(1.0);
            clip.play();

        } catch (Exception e) {
            System.err.println("Error playing sound: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Plays background music in a loop.
     * Stops any currently playing BGM before starting the new one.
     *
     * @param fileName The name of the music file to play
     * @param volume   The volume level (0.0 to 1.0)
     */
    public static void playBGM(String fileName, double volume) {
        String path = "/assets/backgrounds/" + fileName;

        try {
            if (bgmPlayer != null) {
                bgmPlayer.stop();
                bgmPlayer.dispose(); // คืน resource
            }

            if (SoundManager.class.getResource(path) == null) {
                System.err.println("BGM not found: " + path);
                return;
            }

            String uri = Objects.requireNonNull(SoundManager.class.getResource(path)).toExternalForm();
            Media media = new Media(uri);

            bgmPlayer = new MediaPlayer(media);

            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            bgmPlayer.setVolume(volume);

            bgmPlayer.play();

        } catch (Exception e) {
            System.err.println("Error playing BGM: " + fileName);
            e.printStackTrace();
        }
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }
}