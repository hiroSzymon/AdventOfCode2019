package pl.j.szymekdo;

import java.util.Arrays;

public class AoC02 extends AoC {
    public static void main(String[] args) throws Exception {
        new AoC02(2);
    }

    public AoC02() throws Exception {
        int[] content = Arrays.stream(readFile().split(",")).mapToInt(e -> Integer.parseInt(e.trim())).toArray();
        int currentPosition = 0;
        int opcode;
        content[1] = 12;
        content[2] = 2;
        outer:
        while ((opcode = content[currentPosition]) != 99) {
            switch (opcode) {
                case 1:
                    content[content[currentPosition + 3]] = content[content[currentPosition + 1]] + content[content[currentPosition + 2]];
                    currentPosition += 4;
                    break;
                case 2:
                    content[content[currentPosition + 3]] = content[content[currentPosition + 1]] * content[content[currentPosition + 2]];
                    currentPosition += 4;
                    break;
                default:
                    break outer;
            }
        }

        System.out.println(content[0]);
    }

    public AoC02(int i) throws Exception {
        final int[] original = Arrays.stream(readFile().split(",")).mapToInt(e -> Integer.parseInt(e.trim())).toArray();
        int[] content = Arrays.copyOf(original, original.length);
        int opcode;
        content[1] = 12;
        content[2] = 2;
        for(int c1=0; c1<100; c1++) {
            for (int c2 = 0; c2 < 100; c2++) {
                int currentPosition = 0;
                content = Arrays.copyOf(original, original.length);
                content[1] = c1;
                content[2] = c2;
                outer:
                while ((opcode = content[currentPosition]) != 99) {

                    switch (opcode) {
                        case 1:
                            content[content[currentPosition + 3]] = content[content[currentPosition + 1]] + content[content[currentPosition + 2]];
                            currentPosition += 4;
                            break;
                        case 2:
                            content[content[currentPosition + 3]] = content[content[currentPosition + 1]] * content[content[currentPosition + 2]];
                            currentPosition += 4;
                            break;
                        default:
                            break outer;
                    }
                }
                if(content[0] == 19690720)
                    System.out.println(100*c1+c2);

            }
        }
        //System.out.println(content[0]);
    }
}
