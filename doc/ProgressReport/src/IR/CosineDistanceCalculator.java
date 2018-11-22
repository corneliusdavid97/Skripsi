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
public class CosineDistanceCalculator extends DistanceCalculator{

    @Override
    public double calculateDistance(VectorSpaceModel vsm1, VectorSpaceModel vsm2) {
//        System.out.println("l1 "+vectorLength(vsm1));
//        System.out.println("l2 "+vectorLength(vsm2));
//        System.out.println("dot "+dotProduct(vsm1, vsm2));
        if(vectorLength(vsm1)==0 || vectorLength(vsm2)==0){
            System.out.println("asdasad");
            return 0;
        }
        return dotProduct(vsm1, vsm2)/(vectorLength(vsm1)*vectorLength(vsm2));
    }
    
    private double dotProduct(VectorSpaceModel vsm1, VectorSpaceModel vsm2){
        Set<String> terms=Dictionary.getInstance().getAllTermList();
        Iterator<String> it=terms.iterator();
        double result=0;
        while(it.hasNext()){
            String term=it.next();
            double weight1=vsm1.getWeight(term);
            double weight2=vsm2.getWeight(term);
            result+=(weight1*weight2);
        }
        return result;
    }
    
    private double vectorLength(VectorSpaceModel vsm){
        Set<String> terms=Dictionary.getInstance().getAllTermList();
        Iterator<String> it=terms.iterator();
        double result=0;
        while(it.hasNext()){
            String term=it.next();
            double weight=vsm.getWeight(term);
            result+=(weight*weight);
        }
        return Math.sqrt(result);
    }
}
