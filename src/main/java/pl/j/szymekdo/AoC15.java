package pl.j.szymekdo;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.*;

public class AoC15 extends AoC {
    IntcodeVM vm = new IntcodeVM(this);

    private enum Direction {
        UP(1), DOWN(2), LEFT(4), RIGHT(3);
        private int code;

        private Direction(int dir) {
            code = dir;
        }

        public int getCode() {
            return code;
        }

        public int getReverse() {
            switch (code) {
                case 1:
                    return 2;
                case 2:
                    return 1;
                case 3:
                    return 4;
                case 4:
                    return 3;
            }
            return 0;
        }

        public static Direction fromCode(int code) {
            switch (code) {
                case 1:
                    return UP;
                case 2:
                    return DOWN;
                case 3:
                    return RIGHT;
                case 4:
                    return LEFT;
            }
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) throws Exception {
        new AoC15();
    }

    public AoC15() throws Exception {
        long inputParam = 1;
        long outParam = 0;
        TreeMap<Pair<Integer, Integer>, ArrayList<Integer>> validPaths = new TreeMap<>();
        SwingTerminalFrame t = new DefaultTerminalFactory().createSwingTerminal();
        t.setSize(1024, 768);
        t.setCursorPosition(100, 100);
        t.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        t.setVisible(true);
        t.getCursorPosition();
        TerminalPosition zero=t.getCursorPosition();

        do {

            if (validPaths.size() > 0) {

                var path = validPaths.pollFirstEntry();
                if(isDeadEnd(path.getValue().get(0))){
                    continue;
                }
                switch (path.getValue().get(0)) {
                    case 1:
                        t.setCursorPosition(zero.withRelativeRow(1));
                        break;
                    case 2:
                        t.setCursorPosition(zero.withRelativeRow(-1));
                        break;
                    case 3:
                        t.setCursorPosition(zero.withRelativeColumn(1));
                        break;
                    case 4:
                        t.setCursorPosition(zero.withRelativeColumn(-1));
                        break;
                }
                System.out.println(vm.getOutput(new long[]{path.getValue().get(0)}));
            }
            zero = t.getCursorPosition();
            for (int i = 1; i < 5; i++) {
                Thread.sleep(1000);
                switch (i) {
                    case 1:
                        t.setCursorPosition(zero.withRelativeRow(1));
                        break;
                    case 2:
                        t.setCursorPosition(zero.withRelativeRow(-1));
                        break;
                    case 3:
                        t.setCursorPosition(zero.withRelativeColumn(1));
                        break;
                    case 4:
                        t.setCursorPosition(zero.withRelativeColumn(-1));
                        break;
                }
                char next = whatsNext(i);
                if (next == '.') {
                    Pair<Integer, Integer> currentPosition = Pair.of(t.getCursorPosition().getColumn(), t.getCursorPosition().getRow());
                    if (!validPaths.containsKey(currentPosition)) {
                        validPaths.put(currentPosition, new ArrayList<>(Collections.singletonList(i)));
                    }
                    else {
                        validPaths.get(currentPosition).add(i);
                    }
                }
                t.putCharacter(next);
                t.flush();
            }
            System.out.println(validPaths);
        } while (!validPaths.isEmpty());

    }

    private char whatsNext(int direction) throws Exception {
        long out = vm.getOutput(new long[]{direction});
        vm.getOutput(new long[]{Direction.fromCode(direction).getReverse()});
        if (out == 0)
            return '#';
        else if (out == 1)
            return '.';
        else
            return 'o';
    }

    private boolean isDeadEnd(int direction) throws Exception {
        ArrayList<Pair<Integer, Boolean>> isDirValid= new ArrayList<>();
        for(int i=1; i<5; i++){
            isDirValid.add(Pair.of(i, whatsNext(i) != '#'));
        }
        return isDirValid.stream().filter(Pair::getValue).count() == 1 && isDirValid.stream().filter(Pair::getValue).findAny().get().getKey() == direction;
    }


}
