package utils;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the loading and caching of image resources.
 */
public class ResourceManager {

    /**
     * Cache for storing loaded images to avoid reloading.
     */
    private static final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Retrieves an image from the cache or loads it from the file system if not present.
     *
     * @param path The relative path to the image file within the assets directory
     * @return The requested Image object, or null if loading fails
     */
    public static Image getImage(String path) {
        String fullPath = "/assets/" + path;
        if (imageCache.containsKey(fullPath)) {
            return imageCache.get(fullPath);
        }

        try {
            Image image = new Image(Objects.requireNonNull(ResourceManager.class.getResourceAsStream(fullPath)));
            if (image.isError()) return null;
            imageCache.put(fullPath, image);
            return image;
        } catch (Exception e) {
            return null;
        }
    }
}