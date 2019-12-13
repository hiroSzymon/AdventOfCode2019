package pl.j.szymekdo;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.IntBuffer;
import java.util.HashSet;


public class AoC13 extends AoC {
    IntcodeVM intcodeVM = new IntcodeVM(this);
    private long window;


    public static void main(String[] args) throws Exception {
        new AoC13(2);
    }

    public AoC13() throws Exception {
        long out = 0;
        intcodeVM.thrusterYieldIP[0] = 0;

        HashSet<Triple<Long, Long, Long>> outputs = new HashSet<>();
        while ((out = intcodeVM.getOutput(new long[1], 0)) != Long.MIN_VALUE) {
            long out2 = intcodeVM.getOutput(new long[1], 0);
            long out3 = intcodeVM.getOutput(new long[1], 0);
            if (out3 == 0)
                outputs.add(Triple.of(out, out2, out3));
        }

        System.out.println(outputs.size());
    }

    public AoC13(int part2) throws Exception {
        SwingTerminalFrame term = new DefaultTerminalFactory().createSwingTerminal();
        term.setVisible(true);
        TextGraphics tx = term.newTextGraphics();
        TextGraphics paddle = term.newTextGraphics();
        final TerminalPosition[] initPos = {term.getCursorPosition().withColumn(5).withRow(2)};

        tx.drawRectangle(initPos[0], term.getTerminalSize().withColumns(5).withRows(5), 'c' );
        KeyStroke keyStroke = term.readInput();
        term.clearScreen();

        long[] input =new long[1];
        while (true) {
            long out = intcodeVM.getOutput(input, 0);
            if(out==Long.MIN_VALUE)
                break;
            long out2 = intcodeVM.getOutput(input, 0);
            long out3 = intcodeVM.getOutput(input, 0);
            input[0]=0;
            switch ((int)out3){
                case 1:
                    tx.drawRectangle(initPos[0].withRow((int)out2).withColumn((int)out), term.getTerminalSize().withRows(1).withColumns(1), 'w');
                    term.flush();
                    break;
                case 2:
                    tx.drawRectangle(initPos[0].withRow((int)out2).withColumn((int)out), term.getTerminalSize().withRows(1).withColumns(1), 'B');
                    term.flush();
                    break;
                case 3:
                    paddle.drawRectangle(initPos[0].withRow((int)out2).withColumn((int)out), term.getTerminalSize().withRows(1).withColumns(1), 'p');
                    term.flush();
                    break;
                case 4:
                    Thread.sleep(1000);
                    tx.drawRectangle(initPos[0].withRow((int)out2).withColumn((int)out), term.getTerminalSize().withRows(1).withColumns(1), 'o');
                    term.flush();
                    keyStroke = term.readInput();
                    switch (keyStroke.getKeyType()){
                        case ArrowLeft:
                            input[0]=-1;
                        case ArrowRight:
                            input[0]=1;
                        default:
                            input[0]=0;
                    }
                    break;
            }
        }
    }

    private void initAoc() {
        try {
            long out = 0;
            //contentList.add(Arrays.stream(readFile().split(",")).map(e -> Long.parseLong(e.trim())).collect(Collectors.toList()));
            //fillContent();
            intcodeVM.thrusterYieldIP[0] = 0;
        } catch (Exception e) {
            System.exit(-9);
        }
    }

    private void mainLoop() {
        long out;
        try {
            while ((out = intcodeVM.getOutput(new long[1], 0)) != Long.MIN_VALUE) {
                long out2 = intcodeVM.getOutput(new long[1], 0);
                long out3 = intcodeVM.getOutput(new long[1], 0);
                if (out == -1 && out2 == 0) {
                    System.out.println("Score: " + out3);
                    continue;
                }



                //outputs.add(Triple.of(out, out2, out3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
