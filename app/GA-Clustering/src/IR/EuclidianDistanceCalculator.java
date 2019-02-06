/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author CorneliusDavid
 */
public class EuclidianDistanceCalculator extends DistanceCalculator{

    @Override
    public double calculateDistance(VectorSpaceModel vsm1, VectorSpaceModel vsm2) {
        Set<String> terms=Dictionary.getInstance().getAllTermList();
        Iterator<String> it=terms.iterator();
        double result=0;
        while(it.hasNext()){
            String term=it.next();
            double weight1=vsm1.getWeight(term);
            double weight2=vsm2.getWeight(term);
            result+=((weight1-weight2)*(weight1-weight2));
        }
        return Math.sqrt(result);
    }
    
}
