/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import IR.BagOfWordVSM;
import IR.Dictionary;
import IR.Document;
import java.io.File;
import java.util.LinkedList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author CorneliusDavid
 */
public class Tester {
    public static void main(String[] args) throws Exception{
        File f=new File("src");
        for (int i = 0; i < f.listFiles().length; i++) {
            for (int j = 0; j < f.listFiles()[i].listFiles().length; j++) {
                String name=f.listFiles()[i].listFiles()[j].getName();
                String folder=f.listFiles()[i].getName();
                System.out.printf("\\lstinputlisting[language=Java, caption=%s]{src/%s/%s}\n",name,folder,name);
            }
        }
//        File f=new File("test.pdf");
//        PDDocument doc=PDDocument.load(f);
//        
//        PDFTextStripper pdfts=new PDFTextStripper();
//        
//        String text=pdfts.getText(doc);
//        System.out.println(text);
//        doc.close();
        
//        Clusterer c=Clusterer.getInstance();
//        c.cluster(20, 2);
//        System.out.println(c.getSolution().computeFitness());
//        LinkedList<LinkedList<Document>> res=new LinkedList<>();
//        res.add(new LinkedList<>());
//        res.add(new LinkedList<>());
//        res.add(new LinkedList<>());
//        res.add(new LinkedList<>());
//        res.add(new LinkedList<>());
//        res.add(new LinkedList<>());
//        res.add(new LinkedList<>());
//        for(Document d: c.getAllDocs()){
//            res.get(d.getClusterCode()).add(d);
//        }
//        
//        int i=0;
//        for(LinkedList<Document> l:res){
//            for(Document d:l){
//                System.out.println(i+" "+d.getDocName());
//            }
//            i++;
//        }

//        Chromosome c=new Chromosome();
//        Document d1=new Document(new File("1.txt"));
//        Document d2=new Document(new File("2.txt"));
//        
//        c.addGene(new Gene(d1.getVector()));
//        c.addGene(new Gene(d2.getVector()));
//        System.out.println(c.computeFitness());
    }
}
