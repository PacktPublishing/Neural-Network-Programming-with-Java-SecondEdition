package edu.packt.neuralnet.math;

/**
 * This class implements the interface IActivationFunction to represent the 
 * Hardlimiting Threshold or Step function
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class Step implements IActivationFunction {
    
    /**
     * Calculates and returns the result of the hardlimiting threshold function
     * @param input value 
     * @return result of the threshold function
     */
    @Override
    public double calc(double x){
        if(x<0)
            return 0.0;
        else
            return 1.0;
    }
    
    /**
     * Calculates and returns the derivative value of the hardlimiting threshold function
     * @param input value 
     * @return derivative of the threshold function
     */
    @Override
    public double derivative(double x){
        if(x==0)
            return Double.MAX_VALUE;
        else
            return 0.0;
            
    }
    
}
