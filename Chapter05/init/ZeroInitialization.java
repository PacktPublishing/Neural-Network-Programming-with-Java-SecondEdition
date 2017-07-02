package edu.packt.neuralnet.init;

/**
 * This class extends WeightInitialization class and it initializes 
 * neural net weights with zeros 
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class ZeroInitialization extends WeightInitialization {
    
	/**
     * Generates zeros  
     * 
     * @return zero 
     */
    @Override
    public double Generate(){
        return 0.0;
    }
    
}
