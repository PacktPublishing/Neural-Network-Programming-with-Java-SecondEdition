package edu.packt.neuralnet.math;

/**
 *
 * IActivationFunction
 * This interface represents the activation function applicable to the neural
 * network. Any activation function should implement this interface in order to
 * be used in neural computation.
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public interface IActivationFunction {
    /**
     * calc
     * This is the core method for calculating the activation function's value
     * @param x 
     * @return returns the result of the activation function given x
     */
    double calc(double x);
    
    /**
     * ActivationFunctionENUM
     * This enumeration lists some of the common used activation functions. The 
     * utility is to store this value as a neural network property
     */
    public enum ActivationFunctionENUM {
        STEP, LINEAR, SIGMOID, HYPERTAN
    }
    
    double derivative(double x);
    
}
