package pl.j.szymekdo;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AoC {

    String readFile() throws Exception{
        return Files.readString(Paths.get("src/main/resources/"+this.getClass().getSimpleName()+".txt"), StandardCharsets.US_ASCII);
    }

    void fillContent(ArrayList<List<Long>> contentList){
        for(int i=0; i<Integer.MAX_VALUE/100-contentList.get(0).size(); i++){
            contentList.get(0).add(0L);
        }
    }
}
