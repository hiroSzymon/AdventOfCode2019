package pl.j.szymekdo;

import java.util.Map;
import java.util.TreeMap;

public class AoC06 extends AoC {
    TreeMap<String, StringBuilder> orbits = new TreeMap<>();
    public static void main(String[] args) throws Exception{
        new AoC06();
    }

    public AoC06() throws Exception{

        String[] data = readFile().split("\\r\\n");
        for(String s : data){
            String[] singleOrbit = s.split("\\)");
            if(orbits.containsKey(singleOrbit[0])){
                orbits.get(singleOrbit[0]).append(singleOrbit[1]);
            }else {
                orbits.put(singleOrbit[0], new StringBuilder(singleOrbit[1]));
            }
        }
        int orbitsCount = orbits.size();
        for(Map.Entry<String, StringBuilder> e : orbits.entrySet()){
            orbitsCount +=follow(e.getValue().toString());
        }
        System.out.println(orbitsCount);

    }

    private int follow(String value){
        int tmp =0;
        for(char c:value.toCharArray()){
            if(orbits.containsKey(""+c))
                tmp += 1 + follow(orbits.get(""+c).toString());
        }
        return tmp;
    }
}
