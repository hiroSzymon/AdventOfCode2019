package pl.j.szymekdo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class AoC07 extends AoC {

    int[] thrusterYieldIP = new int[5];
    boolean[] thrusterHalted = new boolean[5];
    Boolean[] thrustedFinished = new Boolean[]{false, false, false, false, false};
    int[] params = new int[]{9, 0, 8, 0, 7, 0, 6, 0, 5, 0, 0};
    ArrayList<int[]> contentList = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        new AoC07().part2();

    }

    private void part1() throws Exception {
        ArrayList<Integer> outputs = new ArrayList<>();

        for (int n = 0; n <= 4; n++) {
            params[0] = n;
            for (int m = 0; m <= 4; m++) {
                if (m == n)
                    continue;
                params[2] = m;
                for (int l = 0; l <= 4; l++) {
                    if (l == n || l == m)
                        continue;
                    params[4] = l;
                    for (int j = 0; j <= 4; j++) {
                        if (j == n || j == m || j == l)
                            continue;
                        params[6] = j;
                        for (int k = 0; k <= 4; k++) {
                            if (k == n || k == m || k == l || k == j)
                                continue;
                            params[8] = k;
                            for (int i = 0, p = 0; i < params.length; i += 2, p++) {
                                if (i + 3 == params.length) {
                                    outputs.add(getOutput(new int[]{params[i], params[i + 1]}, p));
                                    break;
                                }
                                params[i + 3] = getOutput(new int[]{params[i], params[i + 1]}, p);
                            }
                        }
                    }
                }
            }
        }
        outputs.sort(Integer::compareTo);
        System.out.println(outputs.get(outputs.size() - 1));
    }

    private void part2() throws Exception {
        ArrayList<Integer> outputs = new ArrayList<>();


        for (int n = 5; n <= 9; n++) {
            params[0] = n;
            for (int m = 5; m <= 9; m++) {
                if (m == n)
                    continue;
                params[2] = m;
                for (int l = 5; l <= 9; l++) {
                    if (l == n || l == m)
                        continue;
                    params[4] = l;
                    for (int j = 5; j <= 9; j++) {
                        if (j == n || j == m || j == l)
                            continue;
                        params[6] = j;
                        for (int k = 5; k <= 9; k++) {
                            if (k == n || k == m || k == l || k == j)
                                continue;
                            params[8] = k;
                            params[1] = 0;
                            params[3] = 0;
                            params[5] = 0;
                            params[7] = 0;
                            params[9] = 0;
                            contentList=new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                contentList.add(Arrays.stream(readFile().split(",")).mapToInt(e -> Integer.parseInt(e.trim())).toArray());
                            }
                            thrusterHalted = new boolean[5];
                            thrusterYieldIP = new int[5];
                            thrustedFinished = new Boolean[]{false, false, false, false, false};
                            for (int i = 0, p = 0; i < params.length; i += 2, p++) {
                                if (i + 3 == params.length) {
                                    params[1] = getOutput(new int[]{params[i], params[i + 1]}, p);
                                    i = -2;
                                    p = -1;
                                    if (Arrays.stream(thrustedFinished).allMatch(e -> e)) {
                                        outputs.add(params[1]);
                                        break;
                                    }
                                    continue;
                                }
                                params[i + 3] = getOutput(new int[]{params[i], params[i + 1]}, p);
                                if (Arrays.stream(thrustedFinished).allMatch(e -> e)) {
                                    outputs.add(params[1]);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        outputs.sort(Integer::compareTo);
        System.out.println(outputs.get(outputs.size() - 1));
    }

    public int getOutput(int[] input, int thruster) throws Exception {
        thrusterHalted[thruster] = false;
        int[] content = contentList.get(thruster);

        int currentPosition = thrusterYieldIP[thruster];
        int inputPosition = currentPosition == 0 ? 0 : 1;
        int opcode;
        int out = 0;
        outer:
        while ((opcode = content[currentPosition]) != 99) {
            if (opcode < 100) {
                int parameter1 = content[content[currentPosition + 1]];
                int parameter2;
                int parameter3;
                switch (opcode) {
                    case 1:
                        parameter2 = content[content[currentPosition + 2]];
                        parameter3 = content[currentPosition + 3];
                        content[parameter3] = parameter1 + parameter2;
                        currentPosition += 4;
                        break;
                    case 2:
                        parameter2 = content[content[currentPosition + 2]];
                        parameter3 = content[currentPosition + 3];
                        content[parameter3] = parameter1 * parameter2;
                        currentPosition += 4;
                        break;
                    case 3:
                        if (inputPosition == input.length) {
                            thrusterYieldIP[thruster] = currentPosition;
                            thrusterHalted[thruster] = true;
                            return out;
                        }
                        content[content[currentPosition + 1]] = input[inputPosition++];
                        currentPosition += 2;
                        break;
                    case 4:
                        out = content[content[currentPosition + 1]];
                        //System.out.println(out);
                        currentPosition += 2;
                        break;
                    case 5:
                        if (parameter1 != 0) {
                            parameter2 = content[content[currentPosition + 2]];
                            currentPosition = parameter2;
                        }
                        else
                            currentPosition += 3;
                        break;
                    case 6:
                        if (parameter1 == 0) {
                            parameter2 = content[content[currentPosition + 2]];
                            currentPosition = parameter2;
                        }
                        else
                            currentPosition += 3;
                        break;
                    case 7:
                        parameter3 = content[currentPosition + 3];
                        parameter2 = content[content[currentPosition + 2]];
                        if (parameter1 < parameter2)
                            content[parameter3] = 1;
                        else
                            content[parameter3] = 0;
                        currentPosition += 4;
                        break;
                    case 8:
                        parameter3 = content[currentPosition + 3];
                        parameter2 = content[content[currentPosition + 2]];
                        if (parameter1 == parameter2)
                            content[parameter3] = 1;
                        else
                            content[parameter3] = 0;
                        currentPosition += 4;
                        break;
                    default:
                        break outer;
                }
            }
            else {
                char[] chars = StringUtils.leftPad(String.valueOf(opcode), 5, '0').toCharArray();
                int[] parameterModes = new int[3];
                parameterModes[2] = chars[0] - 0x30;
                parameterModes[1] = chars[1] - 0x30;
                parameterModes[0] = chars[2] - 0x30;
                int opcode2 = (chars[3] - 0x30) * 10 + chars[4] - 0x30;
                int parameter3 = content[currentPosition + 3];

                int parameter1 = parameterModes[0] == 0 ? content[content[currentPosition + 1]] : content[currentPosition + 1];

                int parameter2;

                switch (opcode2) {
                    case 1:
                        parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                        content[parameter3] = parameter1 + parameter2;
                        currentPosition += 4;
                        break;
                    case 2:
                        parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                        content[parameter3] = parameter1 * parameter2;
                        currentPosition += 4;
                        break;
                    case 3:
                        if (inputPosition == input.length) {
                            thrusterYieldIP[thruster] = currentPosition;
                            return out;
                        }
                        content[parameter1] = input[inputPosition++];
                        currentPosition += 2;
                        break;
                    case 4:
                        out = parameter1;
                        //System.out.println(out);
                        currentPosition += 2;
                        break;
                    case 5:
                        if (parameter1 != 0) {
                            parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                            currentPosition = parameter2;
                        }
                        else
                            currentPosition += 3;
                        break;
                    case 6:
                        if (parameter1 == 0) {
                            parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                            currentPosition = parameter2;
                        }
                        else
                            currentPosition += 3;
                        break;
                    case 7:
                        parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                        if (parameter1 < parameter2)
                            content[parameter3] = 1;
                        else
                            content[parameter3] = 0;
                        currentPosition += 4;
                        break;
                    case 8:
                        parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                        if (parameter1 == parameter2)
                            content[parameter3] = 1;
                        else
                            content[parameter3] = 0;
                        currentPosition += 4;
                        break;
                    default:
                        break outer;
                }


            }
        }
        thrustedFinished[thruster] = true;
        //System.out.println(content[4]);
        return out;
    }
}
