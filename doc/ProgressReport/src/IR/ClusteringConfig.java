package IR;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CorneliusDavid
 */
public class ClusteringConfig {
    public boolean USE_STEMMER;
    public boolean STOPWORD_REMOVAL;
    private static ClusteringConfig instance;
    
    private ClusteringConfig(){
        USE_STEMMER=true;
        STOPWORD_REMOVAL=true;
    }
    
    public static ClusteringConfig getInstance(){
        if(instance==null){
            instance=new ClusteringConfig();
        }
        return instance;
    }
}
