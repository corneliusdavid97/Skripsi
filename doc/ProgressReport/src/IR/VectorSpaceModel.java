/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author CorneliusDavid
 */
public abstract class VectorSpaceModel {

    protected HashMap<String, Double> termsWeight;
    protected DistanceCalculator distanceCalculator;

    protected VectorSpaceModel(HashMap<String, Integer> wordCount, String distanceMode) {
        termsWeight = new HashMap<>();

        switch (distanceMode) {
            case "EUC":
                distanceCalculator = new EuclidianDistanceCalculator();
                break;
            case "COS":
                distanceCalculator = new CosineDistanceCalculator();
                break;
        }
        
        generateVector(wordCount);
    }

    protected abstract void generateVector(HashMap<String, Integer> wordCount);

    public abstract double getWeight(String term);

    public double calculateDistance(VectorSpaceModel otherVSM) {
        return distanceCalculator.calculateDistance(this, otherVSM);
    }

    @Override
    public String toString() {
        return termsWeight.toString();
    }
    
    public void mutate(){
        Random rand=new Random();
        String key=(String) termsWeight.keySet().toArray()[rand.nextInt(termsWeight.size())];
        termsWeight.put(key, rand.nextDouble()*termsWeight.get(key)*2);//mutasi dari 0 - nilai sendiri dikali 2
    }

    public void setTermsWeight(HashMap<String, Double> termsWeight) {
        this.termsWeight = termsWeight;
    }
    
    
}
