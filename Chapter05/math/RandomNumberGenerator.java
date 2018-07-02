package edu.packt.neuralnet.math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class generates double precision random numbers according to a seed. It 
 * is used in weights initialization, for example.
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class RandomNumberGenerator {
	
    /**
     * Seed that is used for random number generation
     */
    public static long seed=0;
    
    /**
     * Random singleton object that actually generates the random numbers
     */
    public static Random r;
    
    /**
     * Static method that returns a newly random number
     * @return 
     */
    public static double[][] GenerateMatrix(int nRows,int nCols){
        double[][] result = new double[nRows][nCols];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                result[i][j]=GenerateNext();
            }
        }
        return result;
    }
    
    public static double[][] GenerateMatrixBetween(int nRows, int nCols, double min,double max){
        double[][] result = new double[nRows][nCols];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                result[i][j]=GenerateBetween(min,max);
            }
        }
        return result;
    }
    
    public static double[][] GenerateMatrixGaussian(int nRows,int nCols,double mean,double stdev){
        double[][] result=new double[nRows][nCols];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                result[i][j]=GenerateGaussian(mean,stdev);
            }
        }
        return result;
            
    }
    
    public static double GenerateNext(){
        if(r==null)
            r = new Random(seed);
        return r.nextDouble();
    }
    
    public static double GenerateBetween(double min,double max){
        if(r==null)
            r=new Random(seed);
        if(max<min)
           return min;
        return min+(r.nextDouble()*(max-min));
    }
    
    public static int GenerateIntBetween(int min,int max){
        if(r==null)
            r=new Random(seed);
        if(max<min)
            return min;
        return min+(r.nextInt(max-min));
    }
    
    public static double GenerateGaussian(double mean,double stdev){
        if(r==null)
            r=new Random(seed);
        return mean + (r.nextGaussian()*stdev);
            
    }
    
    /** 
     * setSeed
     * Sets a new seed for the random generator
     * @param seed new seed for random generator
     */
    public static void setSeed(long seed){
        if(r==null)
            r=new Random(seed);
        seed=seed;
    }
    
    public static int[] hashInt(int start,int end){
        LinkedList<Integer> ll = new LinkedList<>();
        ArrayList<Integer> al = new ArrayList<>();
        for(int i=start;i<=end;i++){
            ll.add(i);
        }
        int start0=0;
        for(int end0=end-start;end0>start0;end0--){
            int rnd = RandomNumberGenerator.GenerateIntBetween(start0, end0);
            int value = ll.get(rnd);
            ll.remove(rnd);
            al.add(value);
        }
        al.add(ll.get(0));
        ll.remove(0);
        return ArrayOperations.arrayListToIntVector(al);
    }
    
}
