package co.axelrod.mygirlfriends.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 31.01.2018.
 */
public class FileUtil {
    public static final String USERS_DIR_PATH = "users";
    public static final String PHOTOS_DIR_PATH = "open_nsfw/photo";
    public static final String UNSORTED_RESULTS_FILE_PATH = "open_nsfw/results.txt";
    public static final String SORTED_RESULTS_FILE_PATH = "sortedResults.txt";

    public static String getStringFromFile(String pathToFile) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(pathToFile));
        return new String(encoded, "UTF-8");
    }
}
