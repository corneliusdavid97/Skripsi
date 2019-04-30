package ga.clustering.gui.GA;


import ga.clustering.gui.IR.Vector;
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
        this.value = new Vector(value);
    }
    
    public Gene(Gene g){
        this.value=new Vector(g.value);
    }

    public Vector getValue() {
        return value;
    }
    
    public void mutate(){
        value.mutate();
    }
}
