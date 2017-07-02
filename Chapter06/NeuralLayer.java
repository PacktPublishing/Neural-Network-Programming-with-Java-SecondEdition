package edu.packt.neuralnet;

import edu.packt.neuralnet.init.WeightInitialization;
import edu.packt.neuralnet.math.IActivationFunction;
import java.util.ArrayList;

/**
 * 
 * This is an abstract class for any Neural Layer. All general attributes and 
 * behaviors intrinsic to a layer of neuron are defined in this class.
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 */
public abstract class NeuralLayer {
    
    /**
     * Number of Neurons in this Layer
     */
    protected int numberOfNeuronsInLayer;
    /**
     * Array of Neurons of this Layer
     */
    protected ArrayList<Neuron> neuron;
    
    /**
     * Activation Function of this Layer
     */
    protected IActivationFunction activationFnc;
    
    /**
     * Previous Layer that feeds values to this Layer
     */
    protected NeuralLayer previousLayer;
    /**
     * Next Layer which this Layer will feed values to
     */
    protected NeuralLayer nextLayer;
    
    /**
     * Array of input values that are fed to this Layer
     */
    protected ArrayList<Double> input;
    /**
     * Array of output values this Layer will produce
     */
    protected ArrayList<Double> output;
    
    /**
     * Number of Inputs this Layer can receive
     */
    protected int numberOfInputs;
    
    
    protected NeuralNet neuralNet;
    
    
    /**
     * NeuralLayer constructor
     * 
     * @param numberofneurons Number of Neurons in this Layer
     * @see NeuralLayer
     */
    public NeuralLayer(NeuralNet _neuralNet,int numberofneurons){
        this.neuralNet=_neuralNet;
        this.numberOfNeuronsInLayer=numberofneurons;
        neuron = new ArrayList<>(numberofneurons);
        output = new ArrayList<>(numberofneurons);
    }
    
    /**
     * NeuralLayer constructor
     * 
     * @param numberofneurons Number of Neurons in this Layer
     * @param iaf Activation Function for all neurons in this Layer
     * @see NeuralLayer
     */
    public NeuralLayer(NeuralNet _neuralNet,int numberofneurons
            ,IActivationFunction iaf){
        this.neuralNet=_neuralNet;
        this.numberOfNeuronsInLayer=numberofneurons;
        this.activationFnc=iaf;
        neuron = new ArrayList<>(numberofneurons);
        output = new ArrayList<>(numberofneurons);
    }
    
    /**
     * Returns the number of neurons in this layer
     * @return integer number of neurons in this layer
     */
    public int getNumberOfNeuronsInLayer(){
        return numberOfNeuronsInLayer;
    }
    
    /**
     * Returns the whole array of neurons of this layer
     * @return neurons array list
     */
    public ArrayList<Neuron> getListOfNeurons(){
        return neuron;
    }
    
    /**
     * Returns the reference to the previous layer
     * @return previous layer object
     */
    public NeuralLayer getPreviousLayer(){
        return previousLayer;
    }
    
    /**
     * Returns the reference to the next layer
     * @return next layer object
     */
    public NeuralLayer getNextLayer(){
        return nextLayer;
    }
    
    /**
     * Sets the reference to the previous layer
     * @param layer 
     */
    protected void setPreviousLayer(NeuralLayer layer){
        previousLayer=layer;
    }
    
    /**
     * Sets the reference to the next layer
     * @param layer  
     */
    protected void setNextLayer(NeuralLayer layer){
        nextLayer=layer;
    }
    
    /**
     * Initializes the Neural Layer by setting the activation function for all 
     * neurons of this layer and then initializing each neuron.
     * @param weightInitialization 
     * @see NeuralLayer
     */
    protected void init(WeightInitialization weightInitialization){
        if(numberOfNeuronsInLayer>=0){
            for(int i=0;i<numberOfNeuronsInLayer;i++){
                try{
                    neuron.get(i).setActivationFunction(activationFnc);
                    neuron.get(i).setNeuralLayer(this);
                    neuron.get(i).init(weightInitialization);
                }
                catch(IndexOutOfBoundsException iobe){
                    neuron.add(new Neuron(numberOfInputs,activationFnc));
                    neuron.get(i).setNeuralLayer(this);
                    neuron.get(i).init(weightInitialization);
                }
            }
        }
    }
    
    /**
     * Sets an array of real values to this layer's input
     * @param inputs array of real values to be fed into this layer's input
     * @see NeuralInput
     */
    protected void setInputs(ArrayList<Double> inputs){
        this.numberOfInputs=inputs.size();
        this.input=inputs;
    }
    
    /**
     * Calculates the outputs of all neurons of this layer
     */
    protected void calc(){
        if(input!=null && neuron!=null){
            for(int i=0;i<numberOfNeuronsInLayer;i++){
                neuron.get(i).setInputs(this.input);
                neuron.get(i).calc();
                try{
                    output.set(i,neuron.get(i).getOutput());
                }
                catch(IndexOutOfBoundsException iobe){
                    output.add(neuron.get(i).getOutput());
                }
            }
        }
    }
    
    /**
     * Gets outputs
     * @return Returns the array of this layer's outputs 
     */
    protected ArrayList<Double> getOutputs(){
        return output;
    }
    
    /**
     * Gets neuron
     * @param i java index of the neuron
     * @return Returns the Neuron at the i-th java position in the layer
     */
    public Neuron getNeuron(int i){
        return neuron.get(i);
    }
    
    /**
     * Sets an already created Neuron at this layer's input
     * @param i java index where the Neuron will be placed
     * @param _neuron Neuron to be inserted or placed in the layer
     */
    protected void setNeuron(int i, Neuron _neuron){
        try{
            this.neuron.set(i, _neuron);
        }
        catch(IndexOutOfBoundsException iobe){
            this.neuron.add(_neuron);
        }
    }
    
    /**
     * Gets weight of line and column indexes
     * @param line index
     * @param column index
     * @return weight 
     */
    public Double getWeight(int i,int j){
        return this.neuron.get(j).getWeight(i);
    }
    
    /**
     * Gets inputs values of neural net as ArrayList
     * @return array list inputs  
     */
    public ArrayList<Double> getArrayListInputs(){
        return input;
    }
    
    /**
     * Gets inputs values of neural net as double array
     * @return double array of inputs  
     */
    public double[] getInputs(){
        double[] result = new double[numberOfInputs];
        for(int i=0;i<numberOfInputs;i++){
            result[i]=input.get(i);
        }
        return result;
    }
    

    /**
     * Gets the neural net mode
     * @return neural mode  
     */
    public NeuralNet.NeuralNetMode getNeuralMode(){
        return this.neuralNet.getNeuralNetMode();
    }
    
    /**
     * Deactivates bias
     */
    public void deactivateBias(){
        for(Neuron n:this.neuron){
            n.deactivateBias();
        }
    }
    
    /**
     * Activates bias
     */
    public void activateBias(){
        for(Neuron n:this.neuron){
            n.activateBias();
        }
    }
    
    /**
     * Verifies if bias is activated
     * @return true if it's activated; else, false
     */
    public boolean isBiasActive(){
        if(neuron.get(0).bias==1.0)
            return true;
        else
            return false;
    }
    
}
