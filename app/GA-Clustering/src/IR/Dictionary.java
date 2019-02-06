/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this termlate file, choose Tools | Templates
 * and open the termlate in the editor.
 */
package IR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author CorneliusDavid
 */
public class Dictionary {

    private HashMap<String, Integer> globalTermCount;
    private HashSet<String> stopwords;
    private static Dictionary instance;

    private Dictionary() {
        this.globalTermCount = new HashMap<>();
        this.stopwords=new HashSet<>();
        this.loadStopwords();
    }
    
    private void loadStopwords(){
        try {
            BufferedReader br=new BufferedReader(new FileReader("stopword-list.txt"));
            String tmp=br.readLine();
            while(tmp!=null && tmp.length()>0){
                this.stopwords.add(tmp);
                tmp=br.readLine();
            }
        } catch (Exception ex) {}        
    }

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
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
    
    public double getMaxValue(){
        double max=0;
        String str="";
        for(Entry<String,Integer> x:globalTermCount.entrySet()){
            if(max<x.getValue()){
                max=x.getValue();
                str=x.getKey();
            }
        }
        return max;
    }
    
    public boolean isStopWord(String term){
        return stopwords.contains(term);
    }
}
