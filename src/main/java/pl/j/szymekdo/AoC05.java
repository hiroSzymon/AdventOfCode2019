package pl.j.szymekdo;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class AoC05 extends AoC {
    public static void main(String[] args) throws Exception {
        new AoC05();
    }

    public AoC05() throws Exception {
        int[] content = Arrays.stream(readFile().split(",")).mapToInt(e -> Integer.parseInt(e.trim())).toArray();
        int[] input = new int[]{5};
        int currentPosition = 0;
        int inputPosition = 0;
        int opcode;
        outer:
        while ((opcode = content[currentPosition]) != 99) {
            if (opcode < 100) {
                int parameter1 = content[content[currentPosition + 1]];
                int parameter2;
                int parameter3 = content[currentPosition + 3];
                switch (opcode) {
                    case 1:
                        parameter2 = content[content[currentPosition + 2]];
                        content[parameter3] = parameter1 + parameter2;
                        currentPosition += 4;
                        break;
                    case 2:
                        parameter2 = content[content[currentPosition + 2]];
                        content[parameter3] = parameter1 * parameter2;
                        currentPosition += 4;
                        break;
                    case 3:
                        content[content[currentPosition + 1]] = input[inputPosition];
                        currentPosition += 2;
                        break;
                    case 4:
                        System.out.println(content[content[currentPosition + 1]]);
                        currentPosition += 2;
                        break;
                    case 5:
                        if (parameter1 != 0) {
                            parameter2 = content[content[currentPosition + 2]];
                            currentPosition = parameter2;
                        }else
                            currentPosition+=3;
                        break;
                    case 6:
                        if (parameter1 == 0) {
                            parameter2 = content[content[currentPosition + 2]];
                            currentPosition = parameter2;
                        }else
                            currentPosition+=3;
                        break;
                    case 7:
                        parameter2 = content[content[currentPosition + 2]];
                        if (parameter1 < parameter2)
                            content[parameter3] = 1;
                        else
                            content[parameter3] = 0;
                        currentPosition += 4;
                        break;
                    case 8:
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
                        content[parameter1] = input[inputPosition];
                        currentPosition += 2;
                        break;
                    case 4:
                        System.out.println(parameter1);
                        currentPosition += 2;
                        break;
                    case 5:
                        if (parameter1 != 0) {
                            parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                            currentPosition = parameter2;
                        }else
                            currentPosition+=3;
                        break;
                    case 6:
                        if (parameter1 == 0) {
                            parameter2 = parameterModes[1] == 0 ? content[content[currentPosition + 2]] : content[currentPosition + 2];
                            currentPosition = parameter2;
                        }else
                            currentPosition+=3;
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
        //System.out.println(content[4]);

    }
}
