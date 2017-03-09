package edu.packt.neuralnet.math;

/**
 *
 * HyperTan
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
     * HyperTan consctrutor
     * @param value new coefficient
     */
    public HyperTan(double value){
        this.setA(value);
    }
    
    /**
     * setA
     * Sets a new coefficient for this function
     * @param value new coefficient
     */
    public void setA(double value){
        this.a=value;
    }
    
    /**
     * calc
     * Peforms the calculation of hyperbolic tangent of x
     * @param x 
     * @return Returns the hyperbolic tangent of x
     */
    @Override
    public double calc(double x){
        return (1.0-Math.exp(-a*x))/(1.0+Math.exp(-a*x));
    }
    
    @Override 
    public double derivative(double x){
        return (1.0)-Math.pow(calc(x),2.0);
    }
    
}
