package edu.packt.neuralnet;

/**
 *
 * NeuralException
 * This class will be used for throwing and catching any exception while 
 * programming Neural Networks
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class NeuralException extends Exception {
    
    /**
     * NeuralException constructor
     * 
     * @param message Message to be displayed with the exception
     * @see NeuralException
     */
    public NeuralException(String message){
        super(message);
    }
}
