/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
/**
 *
 * @author CorneliusDavid
 */
public class BagOfWordVSM extends VectorSpaceModel {

    public BagOfWordVSM(HashMap<String, Integer> wordCount, String distanceMode) {
        super(wordCount, distanceMode);
    }

    @Override
    public double getWeight(String term) {
        if(!termsWeight.containsKey(term)) return 0.0;
        return termsWeight.get(term);
    }


    @Override
    protected void generateVector(HashMap<String, Integer> wordCount) {
        Set<Entry<String,Integer>> entrySet=wordCount.entrySet();
        for(Entry<String,Integer> entry:entrySet){
            this.termsWeight.put(entry.getKey(), entry.getValue().doubleValue());
        }
    }

}
