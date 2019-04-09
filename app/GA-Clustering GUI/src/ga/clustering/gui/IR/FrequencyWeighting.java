/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import java.util.HashMap;

/**
 *
 * @author Cornelius David
 */
public class FrequencyWeighting extends TermWeighting{

    @Override
    public double calculateWeight(String term, HashMap<String, Integer> wordCount) {
        int frequency=wordCount.get(term);
        return frequency*1.0;
    }
    
}
