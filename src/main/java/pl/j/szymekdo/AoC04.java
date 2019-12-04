package pl.j.szymekdo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AoC04 extends AoC {
    private String[] range = readFile().split("-");
    private int beggining = Integer.parseInt(range[0]);
    private int end = Integer.parseInt(range[1]);


    public static void main(String[] args) throws Exception {
        new AoC04(2);
    }


    public AoC04() throws Exception {
        int counter = 0;
        outer:
        for (int i = beggining; i <= end; i++) {
            String value = Integer.toString(i);
            for (int c = 1; c < 6; c++) {
                if (value.charAt(c - 1) > value.charAt(c))
                    continue outer;
            }
            if (value.matches(".*(\\d)(?=\\1).*"))
                counter++;
        }
        System.out.println(counter);
    }


    public AoC04(int part2) throws Exception {
        AtomicInteger counter = new AtomicInteger();
        ArrayList<String> partiallyCorrect = new ArrayList<>();
        outer:
        for (int i = beggining; i <= end; i++) {
            String value = Integer.toString(i);
            for (int c = 1; c < 6; c++) {
                if (value.charAt(c - 1) > value.charAt(c))
                    continue outer;
            }
            if (value.matches(".*(\\d)(?=\\1).*"))
                partiallyCorrect.add(value);
        }

        partiallyCorrect.forEach(value->{
            Pattern pattern = Pattern.compile("(\\d)\\1");
            Matcher matcher = pattern.matcher(value);
            HashMap<String, Integer> tokens = new HashMap<>();

            matcher.results().forEach(e -> {
                if (tokens.containsKey(e.group())) {
                    tokens.replace(e.group(), tokens.get(e.group()) + 1);
                }
                else
                    tokens.put(e.group(), 1);
            });


            tokens.entrySet().removeIf(e->e.getValue()>1);
            tokens.entrySet().removeIf(e->value.contains(e.getKey().substring(0,1).repeat(3)));

            if(tokens.containsValue(1)){
                counter.getAndIncrement();
            }
            System.out.println(value + " " + tokens);
        });
        System.out.println(counter.get());

    }
}
