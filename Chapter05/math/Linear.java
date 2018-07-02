package edu.packt.neuralnet.math;

/**
 * This class represents the pure linear activation function, implementing the
 * interface IActivationFunction
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class Linear implements IActivationFunction {
    
    /**
     * Coefficient that multiplies x
     */
    private double a=1.0;
    
    /** 
     * Linear dummy constructor
     */
    public Linear(){
        
    }
    
    /**
     * Linear constructor
     * @param value coefficient of the Linear function
     */
    public Linear(double value){
        this.setA(value);
    }
    
    /** 
     * Sets a new coefficient for the linear function
     * @param value new coefficient for the linear function
     */
    public void setA(double value){
        this.a=value;
    }
    
    /**
     * Calculates and returns the result of the linear function
     * @param input value 
     * @return result of the linear function
     */
    @Override
    public double calc(double x){
        return a*x;
    }
    
    /**
     * Calculates and returns the derivative value of the linear function
     * @param input value 
     * @return derivative of the linear function
     */
    @Override 
    public double derivative(double x){
        return a;
    }
    
}
