/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import ga.clustering.gui.GA.Params;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author CorneliusDavid
 */
public class Vector {

    private HashMap<String, Double> termsWeight;
    private SimilarityCalculator distanceCalculator;
    private TermWeighting termWeighting;
    

    public Vector(HashMap<String, Integer> wordCount) {
        termsWeight = new HashMap<>();

        distanceCalculator = new CosineSimilarityCalculator();
        if(Params.getInstance().getWeightMethod()==0){
            termWeighting=new TFIDFWeighting();
        }else{
            termWeighting=new FrequencyWeighting();
        }
        
        generateVector(wordCount);
    }

    private void generateVector(HashMap<String, Integer> wordCount){
        Set<Map.Entry<String,Integer>> entrySet=wordCount.entrySet();
        for(Map.Entry<String,Integer> entry:entrySet){
            this.termsWeight.put(entry.getKey(), calculateWeight(entry.getKey(), wordCount));
        }
    }

    public double getWeight(String term){
        if(!termsWeight.containsKey(term))return 0.0;
        return termsWeight.get(term);
    }
    
    public double calculateWeight(String term, HashMap<String, Integer> wordCount){
        return termWeighting.calculateWeight(term, wordCount);
    }

    public double calculateDistance(Vector otherVSM) {
        return distanceCalculator.calculateDistance(this, otherVSM);
    }

    @Override
    public String toString() {
        return termsWeight.toString();
    }
    
    public void mutate(){
        Random rand=new Random();
//        System.out.println("tw: "+termsWeight.size());
        String key=(String) termsWeight.keySet().toArray()[rand.nextInt(termsWeight.size())];
        termsWeight.put(key, rand.nextDouble()*termsWeight.get(key)*2);//mutasi dari 0 - nilai sendiri dikali 2
    }

    public void setTermsWeight(HashMap<String, Double> termsWeight) {
        this.termsWeight = new HashMap<>(termsWeight);
    }
    
    public void setWeight(String term, double value){
        termsWeight.put(term,value);
    }
    
    public double getLength(){
        double res=0.0;
        for(Map.Entry<String,Double> entry: termsWeight.entrySet()){
            res+=(entry.getValue()*entry.getValue());
        }
        res=Math.sqrt(res);
        return res;
    }
    
    public Set<String> getKeySet(){
        return termsWeight.keySet();
    }

    public HashMap<String, Double> getTermsWeight() {
        return termsWeight;
    }
    
    public int getDimension(){
        return this.termsWeight.size();
    }
}
