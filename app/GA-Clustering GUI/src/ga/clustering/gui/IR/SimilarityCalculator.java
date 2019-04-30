/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

/**
 *
 * @author CorneliusDavid
 */
public abstract class SimilarityCalculator {
    public abstract double calculateSimilarity(Vector vector1,Vector vector2);
}
