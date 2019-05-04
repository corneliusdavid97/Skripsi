/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this termlate file, choose Tools | Templates
 * and open the termlate in the editor.
 */
package ga.clustering.gui.IR;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author CorneliusDavid
 */
public class Lexicon {

    private HashMap<String, Integer> globalTermCount;
    private HashMap<String, Integer> documentFrequency;
//    private HashSet<String> stopwords;
    private static Lexicon instance;
    private int numberOfDocument;

    private Lexicon() {
        this.globalTermCount = new HashMap<>();
//        this.stopwords=new HashSet<>();
        this.documentFrequency=new HashMap<>();
//        this.loadStopwords();
    }
    
//    private void loadStopwords(){
//        try {
//            BufferedReader br=new BufferedReader(new FileReader("stopword-list.txt"));
//            String tmp=br.readLine();
//            while(tmp!=null && tmp.length()>0){
//                this.stopwords.add(tmp);
//                tmp=br.readLine();
//            }
//        } catch (Exception ex) {}        
//    }

    public static Lexicon getInstance() {
        if (instance == null) {
            instance = new Lexicon();
        }
        return instance;
    }

    /**
     * memasukkan term ke dalam 
     *
     * @param term String yang akan dimasukkan
     */
    public void insertTerm(String term) {
        if (!globalTermCount.containsKey(term)) {
            globalTermCount.put(term, 0);
        }
        globalTermCount.put(term, globalTermCount.get(term) + 1);
    }
    
    public Set<String> getAllTermList(){
        return this.globalTermCount.keySet();
    }
    
    public void updateDF(String term){
        if(!documentFrequency.containsKey(term)){
            documentFrequency.put(term, 0);
        }
        documentFrequency.put(term, documentFrequency.get(term) + 1);                
    }
    
//    public double getMaxValue(){
//        double max=0;
//        String str="";
//        for(Entry<String,Integer> x:globalTermCount.entrySet()){
//            if(max<x.getValue()){
//                max=x.getValue();
//                str=x.getKey();
//            }
//        }
//        return max;
//    }
    
//    public boolean isStopWord(String term){
//        return stopwords.contains(term);
//    }

    public int getNumberOfDocument() {
        return numberOfDocument;
    }

    public void setNumberOfDocument(int numberOfDocument) {
        this.numberOfDocument = numberOfDocument;
    }
    
    public int getDocumentFrequency(String term){
        return this.documentFrequency.get(term);
    }
}
