/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import ga.clustering.gui.Params;
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
    private SimilarityCalculator similarityCalculator;
    private TermWeighting termWeighting;
    

    public Vector(HashMap<String, Integer> wordCount) {
        termsWeight = new HashMap<>();

        similarityCalculator = new CosineSimilarityCalculator();
        if(Params.getInstance().getWeightingMethod()==0){
            termWeighting=new TFIDFWeighting();
        }else{
            termWeighting=new FrequencyWeighting();
        }
        
        generateVector(wordCount);
    }
    
    public Vector(Vector v){
        similarityCalculator = new CosineSimilarityCalculator();
        if(Params.getInstance().getWeightingMethod()==0){
            termWeighting=new TFIDFWeighting();
        }else{
            termWeighting=new FrequencyWeighting();
        }
        this.termsWeight=new HashMap<>(v.getTermsWeight());
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

    public double calculateSimilarity(Vector otherVector) {
        return similarityCalculator.calculateSimilarity(this, otherVector);
    }
    
    public void mutate(){
        Random rand=new Random();
        String key=(String) termsWeight.keySet().toArray()[rand.nextInt(termsWeight.size())];
        double value=termsWeight.get(key);
        double newVal=value+(rand.nextBoolean()?value*2*rand.nextDouble():value*-2*rand.nextDouble());
        newVal=newVal<0?0:newVal;
        termsWeight.put(key, newVal);
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
