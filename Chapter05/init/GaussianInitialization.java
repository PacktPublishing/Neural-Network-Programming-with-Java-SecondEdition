
package edu.packt.neuralnet.init;

import edu.packt.neuralnet.math.RandomNumberGenerator;

/**
 * This class extends WeightInitialization class and it uses values produced
 * by Guassian distribution to initialize neural net weights 
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class GaussianInitialization extends WeightInitialization {
    
	/**
     * Gaussian mean 
     */
    private double mean;
    
    /**
     * Gaussian standard deviation 
     */
    private double stdev;
    
    /**
     * GaussianInitialization constructor
     * 
     * @param Gaussian mean
     * @param Gaussian standard deviation
     */
    public GaussianInitialization(double _mean,double _stdev){
        this.mean=_mean;
        this.stdev=_stdev;
    }
    
    /**
     * Generates random numbers taking into account Gaussian parameters 
     * (mean and stdev)
     * 
     * @return Gaussian random number 
     */
    @Override 
    public double Generate(){
        return RandomNumberGenerator.GenerateGaussian(mean, stdev);
    }
    
}
