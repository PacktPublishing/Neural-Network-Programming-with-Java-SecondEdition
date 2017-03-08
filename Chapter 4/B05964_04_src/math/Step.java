package edu.packt.neuralnet.math;

/**
 *
 * Step
 * This class implements the interface IActivationFunction to represent the 
 * Hardlimiting Threshold or Step function
 * @author fSoares
 */
public class Step implements IActivationFunction {
    
    /**
     * calc
     * Method that returns the result of the hardlimiting threshold function
     * @param x 
     * @return 
     */
    @Override
    public double calc(double x){
        if(x<0)
            return 0.0;
        else
            return 1.0;
    }
    
    @Override
    public double derivative(double x){
        if(x==0)
            return Double.MAX_VALUE;
        else
            return 0.0;
            
    }
    
}
