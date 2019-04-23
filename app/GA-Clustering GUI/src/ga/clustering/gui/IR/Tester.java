/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import java.io.File;

/**
 *
 * @author CorneliusDavid
 */
public class Tester {

    public static void main(String[] args) {
        Lexicon.getInstance().setNumberOfDocument(5);
        Document d1=new Document(new File("D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc\\business\\001.txt"));
        Document d2=new Document(new File("D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc\\business\\002.txt"));
        Document d3=new Document(new File("D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc\\business\\003.txt"));
        Document d4=new Document(new File("D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc\\business\\004.txt"));
        Document d5=new Document(new File("D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc\\business\\005.txt"));
        System.out.println(d1.getVector().getLength());
        System.out.println(d1.getVector().calculateDistance(d1.getVector()));
    }
}
