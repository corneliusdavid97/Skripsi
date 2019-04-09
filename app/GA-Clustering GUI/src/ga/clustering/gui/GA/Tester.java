/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.GA;

import ga.clustering.gui.GA.Clusterer;
import ga.clustering.gui.IR.Document;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author CorneliusDavid
 */
public class Tester {
    public static void main(String[] args) throws Exception{
        String filepath="D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc";
        Chromosome res=Clusterer.getInstance().cluster();
        List<Document>[] result=new ArrayList[res.getAllGenes().size()];
        for (int i = 0; i < result.length; i++) {
            result[i]=new ArrayList<>();
        }
        for(Entry<Document,Integer> entry:res.getClusteringResult().entrySet()){
            result[entry.getValue()].add(entry.getKey());
        }
        int maxLength=0;
        for (int i = 0; i < result.length; i++) {
            maxLength=Math.max(maxLength, result[i].size());
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH_mm_ss");
	Date date = new Date();
        String filename="D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\res\\GA-"+dateFormat.format(date)+".csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < result.length; j++) {
                if(i<result[j].size()){
                    String name=result[j].get(i).getDocName().substring(filepath.length());
                    if(name.charAt(0)=='\\'){
                        name=name.substring(1);
                    }
                    bw.write(name);
                }
                bw.write(",");
            }
            bw.write("\r\n");
        }
        bw.close();
    }
}
