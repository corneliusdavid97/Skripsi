/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author CorneliusDavid
 */
public class CosineSimilarityCalculator extends SimilarityCalculator{

    @Override
    public double calculateSimilarity(Vector vector1, Vector vector2) {
        return dotProduct(vector1, vector2)/(vector1.getLength()*vector2.getLength());
    }
    
    private double dotProduct(Vector vector1, Vector vector2){
        Set<String> terms=vector1.getKeySet();
        Iterator<String> it=terms.iterator();
        double result=0;
        while(it.hasNext()){
            String term=it.next();
            double weight1=vector1.getWeight(term);
            double weight2=vector2.getWeight(term);
            result+=(weight1*weight2);
        }
        return result;
    }
}
