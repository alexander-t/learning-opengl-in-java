package se.tarlinder.opengl;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ResourceUtils {
    public static String readFileAsString(String path) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(ResourceUtils.class.getResource(path).toURI()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return contentBuilder.toString();
    }
}
