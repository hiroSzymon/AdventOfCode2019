package pl.j.szymekdo;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class AoC14 extends AoC {
    HashMap<Pair<Double, String>, ArrayList<Pair<Double, String>>> input = new HashMap<>();

    public static void main(String[] args) throws Exception {
        AoC14 aoC14 = new AoC14();
        //System.out.println(aoC14.input);
        aoC14.part1And2();
    }

    void part1And2() throws Exception {
        Set<Integer> processedIndexes = new HashSet<>();
        //Value found experimentally :)
        var fuelValues = input.get(Pair.of(2_074_844.0, "FUEL"));

        for(int i=0; i<fuelValues.size(); i++){
            fuelValues.set(i, Pair.of(fuelValues.get(i).getKey()*2_074_844, fuelValues.get(i).getValue()));
        }

        HashMap<String, Integer> leftovers = new HashMap<>();

        int i = 0;
        while (!fuelValues.stream().allMatch(e -> e.getValue().equals("ORE"))) {
            System.out.println("BEFORE: "+fuelValues);
            i %=fuelValues.size();
            var toReplace = fuelValues.get(i);
            var matchingEntry = findEntry(toReplace);
            if(matchingEntry == null) {
                i++;
                continue;
            }

            if(leftovers.containsKey(toReplace.getValue())){
                int left = leftovers.get(toReplace.getValue());
                if(left>toReplace.getKey()){
                    leftovers.put(toReplace.getValue(), (int)(left-toReplace.getKey()));
                    fuelValues.remove(i);
                    i++;
                    continue;
                }else {
                    toReplace = Pair.of(toReplace.getKey() - left, toReplace.getValue());
                    leftovers.remove(toReplace.getValue());
                }
            }

            if (toReplace.getKey() >= matchingEntry.getKey().getKey()) {
                double count = Math.ceil(toReplace.getKey() / matchingEntry.getKey().getKey());
                double floating= toReplace.getKey() / matchingEntry.getKey().getKey();
                if(floating != count){
                    if(leftovers.containsKey(toReplace.getValue())){
                        leftovers.replace(toReplace.getValue(), (int)(leftovers.get(toReplace.getValue())+matchingEntry.getKey().getKey()*count-toReplace.getKey()));
                    }else {
                        leftovers.put(toReplace.getValue(), (int)(matchingEntry.getKey().getKey()*count-toReplace.getKey()));
                    }
                }
                for (int e = 0; e < matchingEntry.getValue().size(); e++) {
                    matchingEntry.getValue().set(e, Pair.of(matchingEntry.getValue().get(e).getKey() * count, matchingEntry.getValue().get(e).getValue()));
                }
            }else if(toReplace.getKey()<matchingEntry.getKey().getKey()){
                if(leftovers.containsKey(toReplace.getValue())){
                    leftovers.replace(toReplace.getValue(), (int)(leftovers.get(toReplace.getValue())+matchingEntry.getKey().getKey()-toReplace.getKey()));
                }else {
                    leftovers.put(toReplace.getValue(), (int)(matchingEntry.getKey().getKey()-toReplace.getKey()));
                }
            }else{
                throw new Exception();
            }

            fuelValues.remove(i);
            for (int e = 0; e < matchingEntry.getValue().size(); e++) {
                fuelValues.add(i + e, matchingEntry.getValue().get(e));
            }

            for (int e = 0; e < fuelValues.size(); e++) {
                for (int f = 0; f < fuelValues.size(); f++) {
                    if (e == f)
                        continue;
                    if (pairMatchValue(fuelValues.get(e), fuelValues.get(f))) {
                        fuelValues.set(e, Pair.of(fuelValues.get(e).getKey() + fuelValues.get(f).getKey(), fuelValues.get(e).getValue()));
                        fuelValues.remove(fuelValues.get(f));
                    }

                }
            }
            System.out.println("LEFTOVERS: "+leftovers);
            System.out.println("AFTER: "+fuelValues);
            i++;//=matchingEntry.getValue().size()-1;
        }


        System.out.println(fuelValues);
        System.out.println((long) fuelValues.stream().mapToDouble(Pair::getKey).sum());
        System.out.println(fuelValues.stream().mapToDouble(Pair::getKey).sum() > 1000000000000d);
    }

    private Pair<Double, String> replaceLast(Pair<Double, String> notReplaced) {
        var entryWithReplacementOptional = Optional.ofNullable(findEntry(notReplaced));
        if (entryWithReplacementOptional.isPresent()) {
            if (notReplaced.getKey() < entryWithReplacementOptional.get().getKey().getKey()) {
                double count = (double) (notReplaced.getKey()) / entryWithReplacementOptional.get().getValue().get(0).getKey();
                return Pair.of(Math.ceil(count) * entryWithReplacementOptional.get().getValue().get(0).getKey(), notReplaced.getValue());
            }
            else {
                double count = (double) (notReplaced.getKey()) / entryWithReplacementOptional.get().getKey().getKey();
                return Pair.of(Math.ceil(count) * entryWithReplacementOptional.get().getValue().get(0).getKey(), notReplaced.getValue());
            }
        }
        return notReplaced;
    }

    AoC14() throws Exception {
        String[] lines = readFile().split("\\r*\\n");
        Arrays.stream(lines).forEach(e -> {
            String[] leftSideTokensWithCount = e.trim().split("=>")[0].trim().split(",\\s");
            String[] rightSideTokens = e.trim().split("=>")[1].trim().split("\\s");
            Pair<Double, String> key = Pair.of(Integer.valueOf(rightSideTokens[0].trim()).doubleValue(), rightSideTokens[1]);
            ArrayList<Pair<Double, String>> value = new ArrayList<>();
            for (String leftSideTokenWithCount : leftSideTokensWithCount) {
                String[] leftSideTokens = leftSideTokenWithCount.split("\\s");
                value.add(Pair.of(Integer.valueOf(leftSideTokens[0].trim()).doubleValue(), leftSideTokens[1]));
            }
            input.put(key, value);
        });

    }

    private Map.Entry<Pair<Double, String>, ArrayList<Pair<Double, String>>> findEntry(Pair<Double, String> valueToReplace) {
        for (var entry : input.entrySet()) {
            if (pairMatchValue(entry.getKey(), valueToReplace)) {
                return Map.entry(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
        }
        return null;
    }

    private boolean pairMatchValue(Pair<Double, String> pair1, Pair<Double, String> pair2) {
        return pair1.getValue().equals(pair2.getValue());

    }


}

