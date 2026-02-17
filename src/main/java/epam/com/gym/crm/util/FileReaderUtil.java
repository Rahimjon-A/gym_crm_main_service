package epam.com.gym.crm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReaderUtil {
    public static List<String> readFromCsv(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read initial data! Path: " + path);
        }
    }
}
