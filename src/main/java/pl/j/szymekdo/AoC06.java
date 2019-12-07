package pl.j.szymekdo;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

import java.util.*;
import java.util.stream.Collectors;

public class AoC06 extends AoC {
    DualLinkedHashBidiMap<String, Node<String>> nodes = new DualLinkedHashBidiMap<>();
    DualLinkedHashBidiMap<String, Set<String>> orbits = new DualLinkedHashBidiMap<>();

    HashMap<String, Integer> parentToStepsYou = new HashMap<>();
    HashMap<String, Integer> parentToStepsSan = new HashMap<>();
    ArrayList<String> visited = new ArrayList<>();
    ArrayList<String> edges = new ArrayList<>();
    int depth = 0;
    int currentDepth = 0;

    public static void main(String[] args) throws Exception {
        new AoC06(2);
    }

    public AoC06() throws Exception {

        String[] data = readFile().split("\\r\\n");
        for (String s : data) {
            String[] singleOrbit = s.split("\\)");
            if (orbits.containsKey(singleOrbit[0])) {
                orbits.get(singleOrbit[0]).add(singleOrbit[1]);
            }
            else {
                Set<String> ss = new HashSet<>();
                ss.add(singleOrbit[1]);
                orbits.put(singleOrbit[0], ss);
            }
        }
        int orbitsCount = 0;
        for (Map.Entry<String, Set<String>> e : orbits.entrySet()) {
            depth += follow(e.getValue());
        }
        System.out.println(depth - orbits.size());

    }

    public AoC06(int part2) throws Exception {

        String[] data = readFile().split("\\r\\n");
        for (String s : data) {
            String[] singleOrbit = s.split("\\)");
            if (orbits.containsKey(singleOrbit[0])) {
                orbits.get(singleOrbit[0]).add(singleOrbit[1]);
            }
            else {
                Set<String> ss = new HashSet<>();
                ss.add(singleOrbit[1]);
                orbits.put(singleOrbit[0], ss);
            }
        }

        findParentYOU("YOU", -1);
        findParentSAN("SAN", -1);

        List<Map.Entry<String, Integer>> commonPartYou = parentToStepsYou.entrySet()
                                                                      .stream()
                                                                      .filter(e -> parentToStepsSan.containsKey(e.getKey()))
                                                                      .sorted(Comparator.comparingInt(Map.Entry::getValue))
                                                                      .collect(Collectors.toList());
        List<Map.Entry<String, Integer>> commonPartSan = parentToStepsSan.entrySet()
                                                                          .stream()
                                                                          .filter(e -> parentToStepsYou.containsKey(e.getKey()))
                                                                          .sorted(Comparator.comparingInt(Map.Entry::getValue))
                                                                          .collect(Collectors.toList());
        System.out.println(commonPartYou.get(0).getValue()+commonPartSan.get(0).getValue());



    }

    private int findParentYOU(String child, int steps) {
        for (Map.Entry<String, Set<String>> ex : orbits.entrySet()) {
            if (ex.getValue().contains(child)) {
                parentToStepsYou.put(ex.getKey(), steps + 1);
                return findParentYOU(ex.getKey(), steps + 1);
            }
        }
        return steps;
    }

    private int findParentSAN(String child, int steps) {
        for (Map.Entry<String, Set<String>> ex : orbits.entrySet()) {
            if (ex.getValue().contains(child)) {
                parentToStepsSan.put(ex.getKey(), steps + 1);
                return findParentSAN(ex.getKey(), steps + 1);
            }
        }
        return steps;
    }

    private int follow(String e, String from) {
        currentDepth++;
        boolean followed = false;

        if (visited.contains(e) || edges.contains(e)) {
            return --currentDepth;
        }

        if (e.equals("YOU")) {
            return currentDepth - 1;
        }

        if (orbits.containsKey(e)) {
            for (String s : orbits.get(e)) {
                if (!from.equals(s)) {
                    return follow(s, e);
                }
            }
        }
        else {
            visited.add(e);
        }


        for (Map.Entry<String, Set<String>> ex : orbits.entrySet()) {
            if (ex.getValue().contains(e)) {
                for (String s : ex.getValue()) {
                    if (!s.equals(e) && !s.equals(from)) {
                        followed = true;
                        return follow(s, ex.getKey());
                    }
                }
            }
            return follow(ex.getKey(), e);
        }


        return --currentDepth;
    }

    private int follow(Set<String> e) {
        int retval = 1;
        for (String s : e) {
            if (orbits.containsKey(s)) {
                retval += follow(orbits.get(s));
            }
            else {
                retval++;
            }

        }
        return retval;
    }


    private class Node<T> {
        private String data;
        private Node<T> parent;

        public Node(String data, Node<T> parent) {
            this.data = data;
            this.parent = parent;
        }
    }
}
