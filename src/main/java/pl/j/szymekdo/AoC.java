package pl.j.szymekdo;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AoC {
    String readFile() throws Exception{
        return Files.readString(Paths.get("src/main/resources/"+this.getClass().getSimpleName()+".txt"), StandardCharsets.US_ASCII);
    }
}
