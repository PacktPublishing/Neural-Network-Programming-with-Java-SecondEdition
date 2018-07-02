package edu.packt.neuralnet;

import edu.packt.neuralnet.math.Linear;

/**
 *
 * InputNeuron
 * This class inherits from Neuron, since it is a special case for a Neuron, 
 * which is at the Neural Network Input Layer
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class InputNeuron extends Neuron {
    
    /**
     * InputNeuron constructor
     * 
     * @see InputNeuron
     */
    public InputNeuron(){
        super(1);
        setActivationFunction(new Linear(1));
        bias=0.0;
    }
    
    /**
     * init
     * Method for initialization of the input neuron, it just adds the weights 
     * with 1's values and a 0 at the bias
     * 
     * @see InputNeuron
     */
    @Override
    public void init(){
        try{
            this.weight.set(0, 1.0);
            this.weight.set(1, 0.0);
        }
        catch(IndexOutOfBoundsException iobe){
            this.weight.add(1.0);
            this.weight.add(0.0);
        }
    }
    
}
