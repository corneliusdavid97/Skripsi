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
    private static Lexicon instance;
    private int numberOfDocument;

    private Lexicon() {
        this.globalTermCount = new HashMap<>();
        this.documentFrequency=new HashMap<>();
    }

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
