/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.math;

/**
 *
 * @author fab
 */
public class MatrixTest {
    
    public static void main(String[] args){
        RandomNumberGenerator.setSeed(42);
        //double[][] mat = RandomNumberGenerator
        //        .GenerateMatrixBetween(5, 5, -1.0, 1.0);
        double[][] mat = {{0.45512,0.36644,-0.3825,-0.4458,0.331}
            ,{0.8067,-0.2624,-0.4485,-0.0726,0.5658}
            ,{0.8386,-0.127,0.4998,-0.2268,-0.6452}
            ,{0.1886,-0.5804,0.6519,-0.6555,0.1748}
            ,{0.5025,0.142,0.16,0.505,-0.9371}};
        double [][] mat3 = {{25.0,5.0,1.0},{64.0,8.0,1.0},{144.0,12.0,1.0}};
        Matrix m = new Matrix(mat);
        double det=m.determinant();
        Matrix d = m.inverse();
        Matrix I = m.multiply(d);
    }
}
