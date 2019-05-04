/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Cornelius David
 */
public class TFIDFWeighting extends TermWeighting{

    @Override
    public double calculateWeight(String term, HashMap<String, Integer> wordCount) {
        return calculateTF(term, wordCount)*calculateIDF(term);
    }
    
    private double calculateTF(String term, HashMap<String, Integer> wordCount){
        int frequency=wordCount.get(term);
        int sumOfFrequency=0;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            sumOfFrequency+=entry.getValue();
        }
        return frequency*1.0/sumOfFrequency;
    }
    
    private double calculateIDF(String term){
        int docFreq=Lexicon.getInstance().getDocumentFrequency(term);
        int N=Lexicon.getInstance().getNumberOfDocument();
        
        return Math.log(N*1.0/docFreq)/Math.log(2);
    }
    
}
