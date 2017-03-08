package edu.packt.neuralnet.math;

import java.util.Random;

/**
 * 
 * RandomNumberGenerator
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
     * GenerateNext
     * Static method that returns a newly random number
     * @return 
     */
    public static double GenerateNext(){
        if(r==null)
            r = new Random(seed);
        return r.nextDouble();
    }
    
    /** 
     * setSeed
     * Sets a new seed for the random generator
     * @param seed new seed for random generator
     */
    public static void setSeed(long seed){
        seed=seed;
        r.setSeed(seed);
    }
}
