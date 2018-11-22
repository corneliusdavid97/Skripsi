/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import GA.Chromosome;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author CorneliusDavid
 */
public class Document {
    private File file;
    protected HashMap<String,Integer> wordCount;
    private VectorSpaceModel vector;
    private int clusterCode;
    
    public Document(File file){
        this.file=file;
        wordCount=new HashMap<>();
        
        try {
            this.indexDocument();
        } catch (FileNotFoundException ex) {}
        
        
        this.vector=new BagOfWordVSM(wordCount,"COS");//TODO: ganti param
    }

    private void indexDocument() throws FileNotFoundException {
        
        
        Scanner sc=new Scanner(new InputStreamReader(new FileInputStream(file)));
//        StemmerIndo stemmer=new StemmerIndo();
        while(sc.hasNext())
        {
            String temp=sc.next();
            temp=temp.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            if(temp.length()==0)continue;
//            if(ClusteringConfig.getInstance().USE_STEMMER){
//                temp=stemmer.getRootWord(temp);
//            }
            if(ClusteringConfig.getInstance().STOPWORD_REMOVAL){
                if(Dictionary.getInstance().isStopWord(temp)){
                    continue;
                }
            }
            Dictionary.getInstance().insertTerm(temp);
            if (!wordCount.containsKey(temp)) {
                wordCount.put(temp, 0);
            }
            wordCount.put(temp, wordCount.get(temp) + 1);
        }
    }
    
    public int getWordCount(String term) {
        if(!wordCount.containsKey(term)){
            return 0;
        }
        return wordCount.get(term).intValue();
    }

    @Override
    public String toString() {
        return vector.toString();
    }

    public VectorSpaceModel getVector() {
        return vector;
    }

    public int getClusterCode() {
        return clusterCode;
    }

    public void determineClusterCode(Chromosome chromosome) {
        this.clusterCode=0;
        double distance=Double.MAX_VALUE;
        for (int i = 0; i < chromosome.getAllGenes().size(); i++) {
            double tempDist=chromosome.getAllGenes().get(i).getValue().calculateDistance(vector);
            if(tempDist<distance){
                distance=tempDist;
                clusterCode=i;
            }
        }
    }
    
    public String getDocName(){
        return file.getName();
    }
}
