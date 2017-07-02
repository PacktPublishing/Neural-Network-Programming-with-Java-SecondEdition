package edu.packt.neuralnet.math;

/**
 * This interface represents the activation function applicable to the neural
 * network. Any activation function should implement this interface in order to
 * be used in neural computation.
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public interface IActivationFunction {
	/**
     * This enumeration lists some of the common used activation functions. The 
     * utility is to store this value as a neural network property
     */
    public enum ActivationFunctionENUM {
        STEP, LINEAR, SIGMOID, HYPERTAN
    }
	
    /**
     * This is the core method for calculating the activation function's value
     * @param input value 
     * @return returns the result of the activation function given x
     */
    double calc(double x);
    
    /**
     * This is the core method for calculating the activation function's derivative value
     * @param input value 
     * @return returns the derivative result of the activation function given x
     */
    double derivative(double x);
    
}
