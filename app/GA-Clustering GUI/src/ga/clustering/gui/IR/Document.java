/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author CorneliusDavid
 */
public class Document {
    private File file;
    protected HashMap<String,Integer> wordCount;
    private Vector vector;
    
    public Document(File file){
        this.file=file;
        wordCount=new HashMap<>();
        
        try {
            this.indexDocument();
        } catch (Exception ex) {}
        
        this.vector=new Vector(wordCount);//TODO: ganti param
    }

    private void indexDocument() throws FileNotFoundException, IOException {
        Scanner sc=new Scanner(new InputStreamReader(new FileInputStream(file)));
        if(file.getName().substring(file.getName().lastIndexOf(".")+1).equalsIgnoreCase("pdf")){
            PDDocument doc=PDDocument.load(file);
            PDFTextStripper pdfts=new PDFTextStripper();
            String input=pdfts.getText(doc);
            sc=new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));    
            doc.close();
        }
//        StemmerIndo stemmer=new StemmerIndo();
        while(sc.hasNext())
        {
            String temp=sc.next();
            temp=temp.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            if(temp.length()==0)continue;
//            if(ClusteringConfig.getInstance().USE_STEMMER){
//                temp=stemmer.getRootWord(temp);
//            }
//            if(ClusteringConfig.getInstance().STOPWORD_REMOVAL){
//                if(Lexicon.getInstance().isStopWord(temp)){
//                    continue;
//                }
//            }
            Lexicon.getInstance().insertTerm(temp);
            if (!wordCount.containsKey(temp)) {
                wordCount.put(temp, 0);
                Lexicon.getInstance().updateDF(temp);
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

    public Vector getVector() {
        return vector;
    }
    
    public String getDocName(){
        return file.getAbsolutePath();
    }
}
