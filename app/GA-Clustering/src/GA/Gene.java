package GA;

import IR.Vector;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CorneliusDavid
 */
public class Gene {
    private Vector value;

    public Gene(Vector value) {
        this.value = value;
    }

    public Vector getValue() {
        return value;
    }
    
    public void mutate(){
        value.mutate();
    }

    @Override
    public String toString() {
        return value.toString();
    }
    
    public void setTermsWeight(HashMap<String, Double> termsWeight) {
        this.value.setTermsWeight(termsWeight);
    }
}
