package pl.j.szymekdo;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class AoC08 extends AoC {
    ArrayList<String> layers = new ArrayList<>();
    private static final int WIDTH = 25;
    private static final int HEIGHT = 6;

    public static void main(String[] args) throws Exception {
        new AoC08(2);

    }

    public AoC08() throws Exception {
        String data = readFile();
        for (int i = 0; i + WIDTH * HEIGHT < data.length(); i += WIDTH * HEIGHT) {
            layers.add(data.substring(i, i + WIDTH * HEIGHT));
        }

        TreeMap<Integer, String> zeroesToLayers = new TreeMap<>(Comparator.comparingInt(e -> e));
        for (String s : layers) {
            zeroesToLayers.put(StringUtils.countMatches(s, '0'), s);
        }

        System.out.println(zeroesToLayers.firstEntry());
        System.out.println(
                StringUtils.countMatches(zeroesToLayers.firstEntry().getValue(), '1') * StringUtils.countMatches(zeroesToLayers.firstEntry().getValue(), '2'));

    }

    public AoC08(int part2) throws Exception {
        String data = readFile();
        for (int i = 0; i + WIDTH * HEIGHT <= data.length(); i += WIDTH * HEIGHT) {
            layers.add(data.substring(i, i + WIDTH * HEIGHT));
        }

        char[] output = new char[WIDTH * HEIGHT];
        Arrays.fill(output, 'X');
        for (String s : layers) {
            for (int i = 0; i < s.length(); i++) {
                char[] chars = s.toCharArray();
                if (output[i] == 'X')
                    switch (chars[i]) {
                        case '0':
                            output[i] = '0';
                            break;
                        case '1':
                            output[i] = '1';
                            break;
                    }
            }
        }

        char[][] out2 = new char[HEIGHT][WIDTH];
        for(int i=0; i<HEIGHT; i++){
            for(int j =0; j<WIDTH; j++){
                out2[i][j]=output[i*WIDTH+j]=='0'?' ':'*';
            }

        }

        for(char[] c : out2){
            System.out.println(c);
        }
    }

/*
 **** *  *  **  *  * *
 *    * *  *  * *  * *
 ***  **   *  * **** *
 *    * *  **** *  * *
 *    * *  *  * *  * *
 *    *  * *  * *  * ****
 */
}
