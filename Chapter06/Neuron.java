package edu.packt.neuralnet;

import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.init.WeightInitialization;
import edu.packt.neuralnet.math.IActivationFunction;
import java.util.ArrayList;


/**
 *
 * This class represents the artificial Neuron, the most basic unit in a neural 
 * network. It encapsulates all attributes and properties related to a neuron, 
 * including weights, inputs, output, activation function, calculation. 
 * One important notation about bias: The weights are numbered from 0 to the 
 * total number of inputs, because the last weight will be included for 
 * multiplying the bias.
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 */
public class Neuron {
    
    /**
     * Weights associated with this Neuron
     */
    protected ArrayList<Double> weight;
    /**
     * Inputs of this neuron
     */
    private ArrayList<Double> input;
    /**
     * Output of this neuron, generated by the activation function.
     */
    private Double output;
    /**
     * Value that is passed to the activation function.
     */
    private Double outputBeforeActivation;
    
    /**
     * Number of Inputs. If is 0, it means the neuron wasn't initialized yet.
     */
    private int numberOfInputs = 0;
    
    /**
     * Bias of the neuron. It should be always 1.0, except for the first layer.
     */
    protected Double bias = 1.0;
    
    /**
     * Activation funcion of the neuron. A reference to the function on all the
     * neuron's inputs.
     */
    private IActivationFunction activationFunction;
    
    /**
     * Neuron's Neural Layer. A reference to the neural layer to which the neuron belongs
     */
    private NeuralLayer neuralLayer;
    
    /**
     * Value of the first derivative. Used as a cache for aidding subsequent
     * calculations.
     */
    private Double firstDerivative;
    /**
     * Neuron dummy constructor
     */
    public Neuron(){
        
    }
    /**
     * Neuron constructor
     * @param numberofinputs Number of Inputs 
     */
    public Neuron(int numberofinputs){
        numberOfInputs=numberofinputs;
        weight=new ArrayList<>(numberofinputs+1);
        input=new ArrayList<>(numberofinputs);
    }
    
    /**
     * Neuron constructor
     * @param numberofinputs Number of inputs
     * @param iaf Activation function
     */
    public Neuron(int numberofinputs,IActivationFunction iaf){
        numberOfInputs=numberofinputs;
        weight=new ArrayList<>(numberofinputs+1);
        input=new ArrayList<>(numberofinputs);
        activationFunction=iaf;
    }
    
    /**
     * setNeuralLayer
     * This method assigns the concerned Neuron to some NeuralLayer
     * @param _neuralLayer the NeuralLayer it should be assigned to this Neuron
     */
    public void setNeuralLayer(NeuralLayer _neuralLayer){
        if(this.neuralLayer==null){
            this.neuralLayer=_neuralLayer;
        }
    }
    
    
    /**
     * This method initializes the neuron by setting randomly (uniform 
     * distribution between 0.0 and 1.0) its weights
     */
    public void init(){
        init(new UniformInitialization(0.0,1.0));
    }
    
    /**
     * init
     * This method initializes the weights of this neuron and sets the weights 
     * according to a probability distribution type (represented by class 
     * WeightInitialization)
     * @param weightInit the probability distribution type
     */
    public void init(WeightInitialization weightInit){
        if(numberOfInputs>0){
            for(int i=0;i<=numberOfInputs;i++){
                double newWeight = weightInit.Generate();
                try{
                    this.weight.set(i, newWeight);
                }
                catch(IndexOutOfBoundsException iobe){
                    this.weight.add(newWeight);
                }
            }
        }
    }
    
    /**
     * Sets a vector of double-precision values to the neuron input
     * @param values vector of values applied at the neuron input
     */
    public void setInputs(double [] values){
        if(values.length==numberOfInputs){
            for(int i=0;i<numberOfInputs;i++){
                try{
                    input.set(i, values[i]);
                }
                catch(IndexOutOfBoundsException iobe){
                    input.add(values[i]);
                }
            }
        }
    }
    
    /**
     * Sets an array of values to the neuron's input
     * @param values 
     */
    public void setInputs(ArrayList<Double> values){
        if(values.size()==numberOfInputs){
            input=values;
        }
    }
    
    /**
     * @return Returns the neuron's inputs in an ArrayList
     */
    public ArrayList<Double> getArrayInputs(){
        return input;
    }
    
    /** 
     * @return Return the neuron's inputs in a vector
     */
    public double[] getInputs(){
        double[] inputs = new double[numberOfInputs];
        for (int i=0;i<numberOfInputs;i++){
            inputs[i]=this.input.get(i);
        }
        return inputs;
    }
    
    /**
     * Sets a real value at the ith java position of the neuron's inputs
     * @param i neuron input java index 
     * @param value value to be set in the input
     */
    public void setInput(int i,double value){
        if(i>=0 && i<numberOfInputs){
            try{
                input.set(i, value);
            }
            catch(IndexOutOfBoundsException iobe){
                input.add(value);
            }
        }
    }
    
    /**
     * @param i ith java position at the input
     * @return Returns the ith java input
     */
    public double getInput(int i){
        return input.get(i);
    }
    
    /**
     * @return Returns the neuron's weights in the form of vector
     */
    public double[] getWeights(){
        double[] weights = new double[numberOfInputs+1];
        for(int i=0;i<=numberOfInputs;i++){
            weights[i]=weight.get(i);
        }
        return weights;
    }
    
    /**
     * getWeight
     * Method to return the ith weight of the neuron. 
     * @param i index of the weight. Bias is the last.
     * @return Returns the ith weight of the neuron.
     */
    public Double getWeight(int i){
        return weight.get(i);
    }
    
    /**
     * getBias
     * Method to retrieve the bias of the neuron.
     * @return Returns the bias of the neuron
     */
    public Double getBias(){
        return weight.get(numberOfInputs);
    }
    
    /**
     * Returns the neuron's weights in the form of Arraylist
     * @return array of weights as ArrayList
     */
    public ArrayList<Double> getArrayWeights(){
        return weight;
    }
    
    /**
     * Method used for updating the weight during learning
     * @param i ith java position of the weight
     * @param value value to be updated on the weight
     */
    public void updateWeight(int i, double value){
        if(i>=0 && i<=numberOfInputs){
            weight.set(i, value);
        }
    }
    
    /**
     * Returns the number of inputs
     * @return number of inputs
     */
    public int getNumberOfInputs(){
        return this.numberOfInputs;
    }
    
    /**
     * Sets the weight at the ith java position
     * @param i ith java position
     * @param value value to be set on the weight
     * @throws NeuralException 
     */
    public void setWeight(int i,double value) throws NeuralException{
        if(i>=0 && i<numberOfInputs){
            this.weight.set(i, value);
        }
        else{
            throw new NeuralException("Invalid weight index");
        }
    }
    
    /**
     * @return Returns the neuron's output
     */
    public double getOutput(){
        return output;
    }
    
    /**
     * Calculates the neuron's output
     */
    public void calc(){
        outputBeforeActivation=0.0;
        if(numberOfInputs>0){
            if(input!=null && weight!=null){
                for(int i=0;i<=numberOfInputs;i++){
                    outputBeforeActivation+=(i==numberOfInputs?bias:input.get(i))*weight.get(i);
                }
            }
        }
        output=activationFunction.calc(outputBeforeActivation);
        if(neuralLayer.getNeuralMode()==NeuralNet.NeuralNetMode.TRAINING){
            firstDerivative=activationFunction.derivative(outputBeforeActivation);
        }
    }
    
    /**
     * calc
     * Calculates the neuron's output and returns the result (does not set the 
     * result in the output attribute). This method can be used during training 
     * algorithms or in situations when one does not want to alter the neuron's 
     * actual output.
     * @param _input an arraylist containing the input values to be fed into the 
     * neuron
     * @return Returns the result of the neuron processing
     */
    public Double calc(ArrayList<Double> _input){
        Double _outputBeforeActivation=0.0;
        if(numberOfInputs>0){
            if(weight!=null){
                for(int i=0;i<=numberOfInputs;i++){
                    _outputBeforeActivation+=(i==numberOfInputs?bias:_input.get(i))*weight.get(i);
                }
            }
        }
        return activationFunction.calc(_outputBeforeActivation);
    }
    
    /**
     * calc
     * Calculates the neuron's output and returns the result (does not set the 
     * result in the output attribute). 
     * @param _input an array containing the input values to be fed into the 
     * neuron
     * @return Returns the result of the neuron processing
     */
    public Double calc(Double[] _input){
        Double _outputBeforeActivation=0.0;
        if(numberOfInputs>0){
            if(weight!=null){
                for(int i=0;i<=numberOfInputs;i++){
                    _outputBeforeActivation+=(i==numberOfInputs?bias:_input[i])*weight.get(i);
                }
            }
        }
        return activationFunction.calc(_outputBeforeActivation);
    }
    
    /**
     * derivative
     * 
     * @param _input an array of the neuron inputs
     * @return Returns the derivative of the neuron output.
     */
    public Double derivative(double[] _input){
        Double _outputBeforeActivation=0.0;
        if(numberOfInputs>0){
            if(weight!=null){
                for(int i=0;i<=numberOfInputs;i++){
                    _outputBeforeActivation+=(i==numberOfInputs?bias:_input[i])*weight.get(i);
                }
            }
        }
        return activationFunction.derivative(_outputBeforeActivation);
    }
    
    /**
     * calcBatch
     * @param _input a 2-D arraylist of arraylist (matrix) of inputs to be fed into 
     * the Neuron
     * @return Returns the results for each of the input sets fed into the 
     * neuron, an 1-D arraylist
     */
    public ArrayList<Double> calcBatch(ArrayList<ArrayList<Double>> _input){
        ArrayList<Double> result = new ArrayList<>();
        for(int i=0;i<_input.size();i++){
            result.add(0.0);
            Double _outputBeforeActivation=0.0;
            for(int j=0;j<numberOfInputs;j++){
                _outputBeforeActivation+=(j==numberOfInputs?bias:_input.get(i).get(j))*weight.get(j);
            }
            result.set(i,activationFunction.calc(_outputBeforeActivation));
        }
        return result;
    }
    
    /**
     * derivativeBatch
     * @param _input a 2-D ArrayList (matrix) containing the inputs
     * to be fed into the neuron.
     * @return Returns the first derivative for each fo the input sets in an 1-D 
     * arraylist
     */
    public ArrayList<Double> derivativeBatch(ArrayList<ArrayList<Double>> _input){
        ArrayList<Double> result = new ArrayList<>();
        for(int i=0;i<_input.size();i++){
            result.add(0.0);
            Double _outputBeforeActivation=0.0;
            for(int j=0;j<numberOfInputs;j++){
                _outputBeforeActivation+=(j==numberOfInputs?bias:_input.get(i).get(j))*weight.get(j);
            }
            result.set(i,activationFunction.derivative(_outputBeforeActivation));
        }
        return result;
    }
    
    /**
     * Sets the activation function of this neuron
     * @param iaf Activation function
     */
    public void setActivationFunction(IActivationFunction iaf){
        this.activationFunction=iaf;
    }
    
    /**
     * Returns the weighted sum of the inputs multiplied by weights
     * 
     * @return output value before activation
     */
    public double getOutputBeforeActivation(){
        return outputBeforeActivation;
    }

    /**
     * deactivateBias
     * This method sets the bias parameter to be zero, so the bias weight will 
     * be useless.
     */
    public void deactivateBias(){
        this.bias=0.0;
    }
    
    /**
     * activateBias
     * This method sets the bias parameter to be one, so the bias weight 
     * becomes effective.
     */
    public void activateBias(){
        this.bias=1.0;
    }
    
    /**
     * getFirstDerivative
     * @return Returns the (cached) first derivative of the Neuron
     */
    public double getFirstDerivative(){
        return firstDerivative;
    }
    
    /**
     * getBiasSource
     * @return Returns the bias parameter, whether to know if it is activated
     * or not.
     */
    public double getBiasSource(){
        return this.bias;
    }
    
}
