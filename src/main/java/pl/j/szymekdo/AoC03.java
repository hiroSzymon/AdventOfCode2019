package pl.j.szymekdo;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AoC03 extends AoC {
    ArrayList<ArrayList<Pair<Integer, Integer>>> paths = new ArrayList<>();
    ArrayList<Pair<Integer, Integer>> crossPoints = new ArrayList<>();
    String[] wires = readFile().split("\\r\\n");
    String[] wire1path = wires[0].split(",");
    String[] wire2path = wires[1].split(",");


    public static void main(String[] args) throws Exception {
        new AoC03();
        new AoC03(2);
    }

    public AoC03() throws Exception {
        ArrayList<Pair<Integer, Integer>> path1 = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> path2 = new ArrayList<>();
        for (String s : wire1path) {
            int value = Integer.parseInt(s.substring(1));
            Pair<Integer, Integer> previousValue = path1.isEmpty() ? Pair.of(0, 0) : path1.get(path1.size() - 1);
            switch (s.charAt(0)) {
                case 'U':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(previousValue.getLeft(), previousValue.getRight() + i));
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(previousValue.getLeft(), previousValue.getRight() - i));
                    }
                    break;
                case 'R':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(previousValue.getLeft() + i, previousValue.getRight()));
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(previousValue.getLeft() - i, previousValue.getRight()));
                    }
                    break;
            }

        }
        paths.add(path1);

        for (String s : wire2path) {
            int value = Integer.parseInt(s.substring(1));
            Pair<Integer, Integer> previousValue = path2.isEmpty() ? Pair.of(0, 0) : path2.get(path2.size() - 1);
            switch (s.charAt(0)) {
                case 'U':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(previousValue.getLeft(), previousValue.getRight() + i));
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(previousValue.getLeft(), previousValue.getRight() - i));
                    }
                    break;
                case 'R':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(previousValue.getLeft() + i, previousValue.getRight()));
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(previousValue.getLeft() - i, previousValue.getRight()));
                    }
                    break;
            }

        }
        paths.add(path2);
        //System.out.println(paths);

        Runnable r1 = () -> {
            for (int i = 0; i < path1.size() / 4; i++) {
                if (path2.contains(path1.get(i)))
                    crossPoints.add(path1.get(i));
            }
        };

        Runnable r2 = () -> {
            for (int i = path1.size() / 4; i < 2 * path1.size() / 4; i++) {
                if (path2.contains(path1.get(i)))
                    crossPoints.add(path1.get(i));
            }
        };

        Runnable r3 = () -> {
            for (int i = 2 * path1.size() / 4; i < 3 * path1.size() / 4; i++) {
                if (path2.contains(path1.get(i)))
                    crossPoints.add(path1.get(i));
            }
        };

        Runnable r4 = () -> {
            for (int i = 3 * path1.size() / 4; i < path1.size(); i++) {
                if (path2.contains(path1.get(i)))
                    crossPoints.add(path1.get(i));
            }
        };

        ExecutorService ex = Executors.newCachedThreadPool();
        ArrayList<Future<?>> futures = new ArrayList<>();
        futures.add(ex.submit(r1));
        futures.add(ex.submit(r2));
        futures.add(ex.submit(r3));
        futures.add(ex.submit(r4));


        while (!futures.stream().allMatch(Future::isDone)) {
            System.out.println("Running");
            Thread.sleep(1000);
        }

        ex.shutdown();


        crossPoints.sort(Comparator.comparingInt(e -> +Math.abs(e.getLeft()) + Math.abs(e.getRight())));
        System.out.println(crossPoints.get(0));
        System.out.println(Math.abs(crossPoints.get(0).getLeft()) + Math.abs(crossPoints.get(0).getRight()));
        System.out.println(crossPoints);
    }


    public AoC03(int part2) throws Exception {
        ArrayList<Pair<Pair<Integer, Integer>, Integer>> path1 = new ArrayList<>();
        ArrayList<Pair<Pair<Integer, Integer>, Integer>> path2 = new ArrayList<>();
        int step = 1;
        for (String s : wire1path) {
            int value = Integer.parseInt(s.substring(1));
            Pair<Integer, Integer> previousValue = path1.isEmpty() ? Pair.of(0, 0) : path1.get(path1.size() - 1).getKey();
            switch (s.charAt(0)) {
                case 'U':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(Pair.of(previousValue.getLeft(), previousValue.getRight() + i), step++));
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(Pair.of(previousValue.getLeft(), previousValue.getRight() - i), step++));
                    }
                    break;
                case 'R':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(Pair.of(previousValue.getLeft() + i, previousValue.getRight()), step++));
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= value; i++) {
                        path1.add(Pair.of(Pair.of(previousValue.getLeft() - i, previousValue.getRight()), step++));
                    }
                    break;
            }

        }
        //paths.add(path1);
        step = 1;
        for (String s : wire2path) {
            int value = Integer.parseInt(s.substring(1));
            Pair<Integer, Integer> previousValue = path2.isEmpty() ? Pair.of(0, 0) : path2.get(path2.size() - 1).getKey();
            switch (s.charAt(0)) {
                case 'U':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(Pair.of(previousValue.getLeft(), previousValue.getRight() + i), step++));
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(Pair.of(previousValue.getLeft(), previousValue.getRight() - i), step++));
                    }
                    break;
                case 'R':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(Pair.of(previousValue.getLeft() + i, previousValue.getRight()), step++));
                    }
                    break;
                case 'L':
                    for (int i = 1; i <= value; i++) {
                        path2.add(Pair.of(Pair.of(previousValue.getLeft() - i, previousValue.getRight()), step++));
                    }
                    break;
            }

        }
        //paths.add(path2);
        //System.out.println(paths);

        Runnable r1 = () -> {
            for (int i = 0; i < path1.size() / 4; i++) {
                for(Pair<Pair<Integer, Integer>, Integer> p : path2){
                    if(p.getKey().equals(path1.get(i).getKey())){
                        crossPoints.add(Pair.of(p.getValue(), path1.get(i).getValue()));
                    }
                }
            }
        };

        Runnable r2 = () -> {
            for (int i = path1.size() / 4; i < 2 * path1.size() / 4; i++) {
                for(Pair<Pair<Integer, Integer>, Integer> p : path2){
                    if(p.getKey().equals(path1.get(i).getKey())){
                        crossPoints.add(Pair.of(p.getValue(), path1.get(i).getValue()));
                    }
                }
            }
        };

        Runnable r3 = () -> {
            for (int i = 2 * path1.size() / 4; i < 3 * path1.size() / 4; i++) {
                for(Pair<Pair<Integer, Integer>, Integer> p : path2){
                    if(p.getKey().equals(path1.get(i).getKey())){
                        crossPoints.add(Pair.of(p.getValue(), path1.get(i).getValue()));
                    }
                }
            }
        };

        Runnable r4 = () -> {
            for (int i = 3 * path1.size() / 4; i < path1.size(); i++) {
                for(Pair<Pair<Integer, Integer>, Integer> p : path2){
                    if(p.getKey().equals(path1.get(i).getKey())){
                        crossPoints.add(Pair.of(p.getValue(), path1.get(i).getValue()));
                    }
                }
            }
        };

        ExecutorService ex = Executors.newCachedThreadPool();
        ArrayList<Future<?>> futures = new ArrayList<>();
        futures.add(ex.submit(r1));
        futures.add(ex.submit(r2));
        futures.add(ex.submit(r3));
        futures.add(ex.submit(r4));


        while (!futures.stream().allMatch(Future::isDone)) {
            System.out.println("Running");
            Thread.sleep(1000);
        }

        ex.shutdown();


        crossPoints.sort(Comparator.comparingInt(e -> e.getKey()+e.getValue()));
        System.out.println(crossPoints.get(0));
        System.out.println(Math.abs(crossPoints.get(0).getLeft()) + Math.abs(crossPoints.get(0).getRight()));
        System.out.println(crossPoints);


    }

}
