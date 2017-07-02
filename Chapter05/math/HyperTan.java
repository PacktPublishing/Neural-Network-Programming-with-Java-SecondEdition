package edu.packt.neuralnet.math;

/**
 * This class represents the Hyperbolic Tangent Activation Function implementing
 * the interface IActivationFunction
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class HyperTan implements IActivationFunction {
    
    /**
     * Hyperbolic tangent coefficient 
     */
    private double a=1.0;
    
    /**
     * HyperTan dummy constructor
     */
    public HyperTan(){
        
    }
    
    /**
     * HyperTan constructor
     * @param value new coefficient
     */
    public HyperTan(double value){
        this.setA(value);
    }
    
    /**
     * Sets a new coefficient for this function
     * @param value new coefficient
     */
    public void setA(double value){
        this.a=value;
    }
    
    /**
     * Calculates and returns the result of the hyperbolic tangent function
     * @param input value 
     * @return result of the hyperbolic tangent function
     */
    @Override
    public double calc(double x){
        return (1.0-Math.exp(-a*x))/(1.0+Math.exp(-a*x));
    }
    
    /**
     * Calculates and returns the derivative value of the hyperbolic tangent function
     * @param input value 
     * @return derivative of the hyperbolic tangent function
     */
    @Override 
    public double derivative(double x){
        return (1.0)-Math.pow(calc(x),2.0);
    }
    
}
