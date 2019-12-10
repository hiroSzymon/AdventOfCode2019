package pl.j.szymekdo;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AoC10 extends AoC {
    public static void main(String[] args) throws Exception{
        new AoC10();
    }

    AoC10() throws Exception{
        ArrayList<Pair<Integer, Integer>> asteroidsCoords = new ArrayList<>();
        HashMap<Pair<Integer, Integer>, HashSet<Pair<Integer, Integer>>> visibles = new HashMap<>();
        ArrayList<ArrayList<Character>> file = new ArrayList<>();
        String[] f = readFile().split("\\r*\\n");
        for(String s : f){
            ArrayList<Character> tmp = new ArrayList<>();
            for(char c: s.toCharArray()){
                tmp.add(c);
            }
            file.add(tmp);
        }

        for(int h=0; h<file.size(); h++){
            for(int w=0; w<file.get(h).size(); w++){
                if(file.get(h).get(w).equals('#'))
                    asteroidsCoords.add(Pair.of(h, w));
            }
        }

        for(Pair<Integer, Integer> p : asteroidsCoords){
            visibles.put(p, new HashSet<>());
            for(int h=0; h<file.size(); h++){
                for(int w=0; w<file.get(h).size(); w++){
                    if((h==p.getKey() && w==p.getValue()) || !file.get(h).get(w).equals('#'))
                        continue;

                    if(visibles.get(p).isEmpty()){
                        if(h==p.getKey() || w == p.getValue()){
                            visibles.get(p).add(Pair.of(h,w));
                            continue;
                        }
                    }
                    if((h==p.getKey()+1 || h==p.getKey()-1 || h==p.getKey())
                       && (w == p.getValue()+1 || w == p.getValue()-1 || w == p.getValue()) ){
                        visibles.get(p).add(Pair.of(h, w));
                        continue;
                    }
                    HashSet<Pair<Integer, Integer>> toAdd=new HashSet<>();
                    for(Pair<Integer, Integer> px : visibles.get(p)){
                        if(px.getKey()==0 || px.getValue() == 0 || px.getValue()==file.get(0).size()
                        || px.getKey() == file.size())
                            continue;
                        if((h%px.getKey()!=0 && w%px.getValue()!=0)){
                            if(!visibles.get(p).contains(Pair.of(h, w)))
                                toAdd.add(Pair.of(h, w));
                        }
                    }
                    visibles.get(p).addAll(toAdd);
                }
            }
        }
        //System.out.println(asteroidsCoords);

        visibles.forEach((e, v)-> System.out.println(e+"="+v.size()));

    }
}
