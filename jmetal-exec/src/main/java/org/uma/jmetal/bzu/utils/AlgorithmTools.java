package org.uma.jmetal.bzu.utils;

import java.util.*;

public class AlgorithmTools {
    public static void Clamp(int[] values){

        //convert it to map
        int[] clonedValues = values.clone();
        Arrays.sort(clonedValues);
        HashMap<Integer, Integer> mappedValues = new HashMap<>();
        for (int i = 0; i < clonedValues.length; i++) {
            mappedValues.put(clonedValues[i],i);
        }

        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < values.length; i++) {
            int indexInSortedArray = mappedValues.get(values[i]);
            int newValue = indexInSortedArray +1;
            if(hashSet.contains(newValue)){
                values[i] = -1;
            }else{
                hashSet.add(newValue);
                values[i] = newValue;
            }

        }

    }
}
