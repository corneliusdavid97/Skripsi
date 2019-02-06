package GA;

import IR.VectorSpaceModel;
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
    private VectorSpaceModel value;

    public Gene(VectorSpaceModel value) {
        this.value = value;
    }

    public VectorSpaceModel getValue() {
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
