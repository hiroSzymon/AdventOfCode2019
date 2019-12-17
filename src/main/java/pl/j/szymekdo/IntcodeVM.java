package pl.j.szymekdo;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntcodeVM{
    ArrayList<List<Long>> contentList = new ArrayList<>();
    int[] thrusterYieldIP = new int[5];
    int[] thrusterYieldInputPosition = new int[5];
    boolean[] thrusterHalted = new boolean[5];
    Boolean[] thrustedFinished = new Boolean[]{false, false, false, false, false};
    long[] params = new long[]{0};
    ArrayList<Long> out = new ArrayList<>();
    int relativeBase =0;
    private JPanel panel1;

    public IntcodeVM(AoC day) throws Exception{
        contentList.add(Arrays.stream(day.readFile().split(",")).map(e -> Long.parseLong(e.trim())).collect(Collectors.toList()));
        day.fillContent(contentList);
    }

    public long getOutput(long[] input) throws Exception {
        thrusterYieldInputPosition[0]=0;
        return getOutput(input, 0);
    }

    public long getOutput(long[] input, int thruster) throws Exception {
        thrusterHalted[thruster] = false;
        List<Long> content = contentList.get(thruster);

        int currentPosition = thrusterYieldIP[thruster];
        int inputPosition = thrusterYieldInputPosition[0];
        int opcode;
        try {
            outer:
            while ((opcode = content.get(currentPosition).intValue()) != 99) {
                if (opcode < 100) {
                    Long parameter1 = content.get(content.get(currentPosition + 1).intValue());
                    Long parameter2;
                    Long parameter3;
                    switch (opcode) {
                        case 1:
                            parameter2 = content.get(content.get(currentPosition + 2).intValue());
                            parameter3 = content.get(currentPosition + 3);
                            content.set(parameter3.intValue(), parameter1 + parameter2);
                            currentPosition += 4;
                            break;
                        case 2:
                            parameter2 = content.get(content.get(currentPosition + 2).intValue());
                            parameter3 = content.get(currentPosition + 3);
                            content.set(parameter3.intValue(), parameter1 * parameter2);
                            currentPosition += 4;
                            break;
                        case 3:
                            if (inputPosition >= input.length) {
                                thrusterYieldIP[thruster] = currentPosition;
                                thrusterYieldInputPosition[thruster]=++inputPosition;
                                thrusterHalted[thruster] = true;
                                return 0;
                            }
                            content.set(content.get(currentPosition + 1).intValue(), input[thrusterYieldInputPosition[0]++]);
                            currentPosition += 2;
                            break;
                        case 4:
                            thrusterYieldIP[thruster]=currentPosition+2;
                            //thrusterYieldInputPosition[thruster]=inputPosition;
                            //out.add(content.get(content.get(currentPosition + 1).intValue()));
                            //System.out.println(out);
                            //currentPosition += 2;
                            return content.get(content.get(currentPosition + 1).intValue());
                        //break;
                        case 5:
                            if (parameter1 != 0) {
                                parameter2 = content.get(content.get(currentPosition + 2).intValue());
                                currentPosition = parameter2.intValue();
                            }
                            else
                                currentPosition += 3;
                            break;
                        case 6:
                            if (parameter1 == 0) {
                                parameter2 = content.get(content.get(currentPosition + 2).intValue());
                                currentPosition = parameter2.intValue();
                            }
                            else
                                currentPosition += 3;
                            break;
                        case 7:
                            parameter3 = content.get(currentPosition + 3);
                            parameter2 = content.get(content.get(currentPosition + 2).intValue());
                            if (parameter1 < parameter2)
                                content.set(parameter3.intValue(), 1L);
                            else
                                content.set(parameter3.intValue(), 0L);
                            currentPosition += 4;
                            break;
                        case 8:
                            parameter3 = content.get(currentPosition + 3);
                            parameter2 = content.get(content.get(currentPosition + 2).intValue());
                            if (parameter1.equals(parameter2))
                                content.set(parameter3.intValue(), 1L);
                            else
                                content.set(parameter3.intValue(), 0L);
                            currentPosition += 4;
                            break;
                        case 9:
                            //parameter2 = content.get(content.get(currentPosition + 2));
                            relativeBase += parameter1;
                            currentPosition += 2;
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


                    long parameter1 = getParameter(parameterModes[0], content, currentPosition, 1);

                    long parameter2;
                    long parameter3;

                    switch (opcode2) {
                        case 1:
                            parameter3 = getOutputAddressForParameter(parameterModes[2], content, currentPosition, 3);
                            parameter2 = getParameter(parameterModes[1], content, currentPosition, 2);
                            content.set((int) parameter3, parameter1 + parameter2);
                            currentPosition += 4;
                            break;
                        case 2:
                            parameter2 = getParameter(parameterModes[1], content, currentPosition, 2);
                            parameter3 = getOutputAddressForParameter(parameterModes[2], content, currentPosition, 3);
                            content.set((int) parameter3, parameter1 * parameter2);
                            currentPosition += 4;
                            break;
                        case 3:
                            parameter1 = getOutputAddressForParameter(parameterModes[0], content, currentPosition, 1);
                            if (inputPosition == input.length) {
                                thrusterYieldIP[thruster] = currentPosition;
                                thrusterYieldInputPosition[thruster]=++inputPosition;
                                thrusterHalted[thruster] = true;
                                return 0;
                            }
                            content.set((int) parameter1, input[inputPosition++]);
                            currentPosition += 2;
                            break;
                        case 4:
                            thrusterYieldIP[thruster]=currentPosition+2;
                            //thrusterYieldInputPosition[thruster]=inputPosition;
                            //out.add(parameter1);
                            return parameter1;
                        //break;
                        case 5:
                            if (parameter1 != 0) {
                                parameter2 = getParameter(parameterModes[1], content, currentPosition, 2);
                                currentPosition = (int) parameter2;
                            }
                            else
                                currentPosition += 3;
                            break;
                        case 6:
                            if (parameter1 == 0) {
                                parameter2 = getParameter(parameterModes[1], content, currentPosition, 2);
                                currentPosition = (int) parameter2;
                            }
                            else
                                currentPosition += 3;
                            break;
                        case 7:
                            parameter3 = getOutputAddressForParameter(parameterModes[2], content, currentPosition, 3);
                            parameter2 = getParameter(parameterModes[1], content, currentPosition, 2);
                            if (parameter1 < parameter2)
                                content.set((int) parameter3, 1L);
                            else
                                content.set((int) parameter3, 0L);
                            currentPosition += 4;
                            break;
                        case 8:
                            parameter3 = getOutputAddressForParameter(parameterModes[2], content, currentPosition, 3);
                            parameter2 = getParameter(parameterModes[1], content, currentPosition, 2);
                            if (parameter1 == parameter2)
                                content.set((int) parameter3, 1L);
                            else
                                content.set((int) parameter3, 0L);
                            currentPosition += 4;
                            break;
                        case 9:
                            //parameter2 = getParameter(parameterModes[0], content, currentPosition,  2);
                            relativeBase += parameter1;
                            currentPosition += 2;
                            break;
                        default:
                            break outer;
                    }


                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(currentPosition);
        }
        thrustedFinished[thruster] = true;
        //System.out.println(content[4]);
        return Long.MIN_VALUE;
    }

    long getParameter(int parameterMode, List<Long> content, int currentPosition, int parameterOrder){
        switch (parameterMode){
            case 0:
                return content.get(content.get(currentPosition + parameterOrder).intValue());
            case 1:
                return content.get(currentPosition + parameterOrder);
            case 2:
                return content.get((int)(relativeBase + content.get(currentPosition+parameterOrder)));

        }
        throw new UnsupportedOperationException("Wrong parameter!");
    }

    int getOutputAddressForParameter(int parameterMode, List<Long> content, int currentPosition, int parameterOrder){
        switch (parameterMode){
            case 0:
                return content.get(currentPosition + parameterOrder).intValue();
            case 1:
                return content.get(currentPosition + parameterOrder).intValue();
            case 2:
                return (int)(relativeBase + content.get(currentPosition + parameterOrder));

        }
        throw new UnsupportedOperationException("Wrong parameter!");
    }

}
