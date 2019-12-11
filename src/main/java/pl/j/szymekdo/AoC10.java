package pl.j.szymekdo;

import org.apache.commons.lang3.tuple.Pair;

import javax.print.DocFlavor;
import java.util.*;
import java.util.stream.Collectors;

public class AoC10 extends AoC {
    ArrayList<Pair<Integer, Integer>> asteroidsCoords = new ArrayList<>();
    TreeMap<Pair<Integer, Integer>, HashSet<Pair<Integer, Integer>>> visibles = new TreeMap<>();
    ArrayList<ArrayList<Character>> file = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new AoC10(2);
    }

    AoC10() throws Exception {

        String[] f = readFile().split("\\r*\\n");
        for (String s : f) {
            ArrayList<Character> tmp = new ArrayList<>();
            for (char c : s.toCharArray()) {
                tmp.add(c);
            }
            file.add(tmp);
        }

        for (int h = 0; h < file.size(); h++) {
            for (int w = 0; w < file.get(h).size(); w++) {
                if (file.get(h).get(w).equals('#'))
                    asteroidsCoords.add(Pair.of(w, h));
            }
        }

        for (Pair<Integer, Integer> p : asteroidsCoords) {
            visibles.put(p, new HashSet<>());
            for (Pair<Integer, Integer> pt : asteroidsCoords) {
                if (pt.equals(p))
                    continue;
                else if (asteroidsCoords.stream().filter(e -> !(e.equals(p) || e.equals(pt))).noneMatch(e -> isBetween(p, pt, e)))
                    visibles.get(p).add(pt);

            }
        }
        //System.out.println(asteroidsCoords);

        TreeMap<Integer, Pair<Integer, Integer>> visibleCountForPoint = new TreeMap<>();
        visibles.forEach((e, v) -> visibleCountForPoint.put(v.size(), e));
        System.out.println(visibleCountForPoint.lastEntry());


    }

    AoC10(int part2) throws Exception {

        String[] f = readFile().split("\\r*\\n");
        for (String s : f) {
            ArrayList<Character> tmp = new ArrayList<>();
            for (char c : s.toCharArray()) {
                tmp.add(c);
            }
            file.add(tmp);
        }

        for (int h = 0; h < file.size(); h++) {
            for (int w = 0; w < file.get(h).size(); w++) {
                if (file.get(h).get(w).equals('#'))
                    asteroidsCoords.add(Pair.of(w, h));
            }
        }

        for (Pair<Integer, Integer> p : asteroidsCoords) {
            visibles.put(p, new HashSet<>());
            for (Pair<Integer, Integer> pt : asteroidsCoords) {
                if (pt.equals(p))
                    continue;
                else if (asteroidsCoords.stream().filter(e -> !(e.equals(p) || e.equals(pt))).noneMatch(e -> isBetween(p, pt, e)))
                    visibles.get(p).add(pt);

            }
        }
        //System.out.println(asteroidsCoords);

        TreeMap<Integer, Pair<Integer, Integer>> visibleCountForPoint = new TreeMap<>();
        visibles.forEach((e, v) -> visibleCountForPoint.put(v.size(), e));
        Pair<Integer, Integer> best = visibleCountForPoint.lastEntry().getValue();


        ArrayList<Pair<Integer, Integer>> ordered = visibles.get(best).stream()
                                                                   .filter(e -> !(e.equals(best)))
                                                                   .sorted(Comparator.comparingDouble(e -> angle(best, e)-squareTargetSource(best, e)))
                                                                   .collect(Collectors.toCollection(ArrayList::new));


        System.out.println(best + " " + ordered.get(199));
    }


    private double angle(Pair<Integer, Integer> from, Pair<Integer, Integer> to){
        double angle = -(Math.toDegrees(Math.atan2(to.getValue() - from.getValue(), to.getKey()-from.getKey())) - 270);
        return angle%360;

    }



    private boolean isBetween(Pair<Integer, Integer> source, Pair<Integer, Integer> target, Pair<Integer, Integer> pointToCheck) {
        int crossProduct = (pointToCheck.getValue() - source.getValue()) * (target.getKey() - source.getKey()) -
                           (pointToCheck.getKey() - source.getKey()) * (target.getValue() - source.getValue());
        if (Math.abs(crossProduct) != 0)
            return false;

        int dotProduct = (pointToCheck.getKey() - source.getKey()) * (target.getKey() - source.getKey()) +
                         (pointToCheck.getValue() - source.getValue()) * (target.getValue() - source.getValue());
        if (dotProduct < 0)
            return false;

        int squaredLengthTargetSource = squareTargetSource(source, target);
        return dotProduct <= squaredLengthTargetSource;
    }

    private int squareTargetSource(Pair<Integer, Integer> source, Pair<Integer, Integer> target) {
        return (int) (Math.pow(target.getKey() - source.getKey(), 2) +
                      Math.pow(target.getValue() - source.getValue(), 2));
    }
}
