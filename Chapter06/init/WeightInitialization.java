package edu.packt.neuralnet.init;

/**
 * This abstract class is responsible to generate initial weights
 * early in the training process
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public abstract class WeightInitialization {

	/**
	 * Abstract method to generate initial weights 
	 */
    public abstract double Generate();
    
}
