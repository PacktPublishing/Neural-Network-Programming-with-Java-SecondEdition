package edu.packt.neuralnet.init;

import edu.packt.neuralnet.math.RandomNumberGenerator;

/**
 * This class extends WeightInitialization class and it uses uniform values  
 * to initialize neural net weights 
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class UniformInitialization extends WeightInitialization {
    
	/**
     * Minimum value 
     */
    private double min;
    
    /**
     * Maximum value 
     */
    private double max;
    
    /**
     * UniformInitialization constructor
     * 
     * @param minimum value
     * @param maximum value
     */
    public UniformInitialization(double _min,double _max){
        this.min=_min;
        this.max=_max;
    }
    
    /**
     * Generates random numbers between min and max values 
     * 
     * @return uniform random number 
     */
    @Override
    public double Generate(){
        return RandomNumberGenerator.GenerateBetween(min, max);
    }
    
}
