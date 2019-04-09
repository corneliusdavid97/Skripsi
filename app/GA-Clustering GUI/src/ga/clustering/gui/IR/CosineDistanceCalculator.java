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
public class CosineDistanceCalculator extends DistanceCalculator{

    @Override
    public double calculateDistance(Vector vsm1, Vector vsm2) {
//        System.out.println("l1 "+vsm1.getLength());
//        System.out.println("l2 "+vsm2.getLength());
//        System.out.println("dot "+dotProduct(vsm1, vsm2));
//        if(vsm1.getLength()==0 || vsm2.getLength()==0){
//            System.out.println("asdasad");
//            return 0;
//        }
        return dotProduct(vsm1, vsm2);///vsm1.getLength()*vsm2.getLength();
    }
    
    private double dotProduct(Vector vsm1, Vector vsm2){
        Set<String> terms=vsm1.getKeySet();
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
}
