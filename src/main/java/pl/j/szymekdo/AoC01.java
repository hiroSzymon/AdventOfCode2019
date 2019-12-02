package pl.j.szymekdo;

import java.util.Arrays;

public class AoC01 extends AoC{
    public static void main(String[] args) throws Exception{
        new AoC01();
    }

    public AoC01() throws Exception{
        String[] content = readFile().split("\\r\\n");
        System.out.println(Arrays.toString(content));
        System.out.println(Arrays.stream(content).mapToInt(e->Integer.parseInt(e.trim())/3-2).sum());
        int sumFuel=0;
        for(String s : content){
            int fuel=calculateFuel(Integer.parseInt(s.trim()));
            sumFuel+=fuel;
            while((fuel = calculateFuel(fuel))>0)
                sumFuel+=fuel;
        }
        System.out.println(sumFuel);
    }

    private static int calculateFuel(int mass){
        if(mass<=0)
            return 0;
        return mass/3-2;
    }
}
