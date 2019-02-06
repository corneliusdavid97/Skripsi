/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import java.io.File;
import java.util.HashSet;

/**
 *
 * @author CorneliusDavid
 */
public class Tester {

    public static void main(String[] args) {
        File folder = new File("documents");
        File[] files = folder.listFiles();
        Document docs[] = new Document[files.length];
        for (int i = 0; i < docs.length; i++) {
            docs[i] = new Document(files[i]);
        }
    }
}
