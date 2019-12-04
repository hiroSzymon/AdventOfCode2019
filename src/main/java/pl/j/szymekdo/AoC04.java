package pl.j.szymekdo;

import java.util.ArrayList;

public class AoC04 extends AoC {
    private String[] range = readFile().split("-");
    private int beggining  = Integer.parseInt(range[0]);
    private int end  = Integer.parseInt(range[1]);


    public static void main(String[] args) throws Exception{
        new AoC04(2);
    }


    public AoC04() throws Exception {
        int counter = 0;
        outer:
        for(int i = beggining; i<=end; i++){
            String value = Integer.toString(i);
            for(int c = 1; c<6; c++) {
                if (value.charAt(c - 1) > value.charAt(c))
                    continue outer;
            }
            if(value.matches(".*(\\d)(?=\\1).*"))
                counter++;
        }
        System.out.println(counter);
    }


    public AoC04(int part2) throws Exception{
        int counter = 0;
        outer:
        for(int i = beggining; i<=end; i++){
            String value = Integer.toString(i);
            for(int c = 1; c<6; c++) {
                if (value.charAt(c - 1) > value.charAt(c))
                    continue outer;
            }
            ArrayList<Integer> shortestSubstrings = new ArrayList<>();
            int shortestSubstring =0;
            for(int c = 1; c<6; c++) {
                if (value.charAt(c - 1) == value.charAt(c))
                    shortestSubstring++;
                else
                    shortestSubstrings.add(shortestSubstring);
            }

            if(shortestSubstrings.contains(2)){
                counter++;
            }
        }
        System.out.println(counter);
    }
}
