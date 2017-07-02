package edu.packt.neuralnet;

import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.init.WeightInitialization;
import edu.packt.neuralnet.math.IActivationFunction;
import java.util.ArrayList;

/**
 *
 * This class represents the Neural Network itself. It contains all the 
 * definitions that a Neural Network has, including method for calculation 
 * (forward).
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 */
public class NeuralNet {
    
    /**
     * Neural Network Input Layer
     */
    protected InputLayer inputLayer;
    /**
     * Neural Network array of hidden layers, that may contain 0 or many
     */
    protected ArrayList<HiddenLayer> hiddenLayer;
    /**
     * Neural Network Output Layer
     */
    protected OutputLayer outputLayer;
    
    /**
     * Number of Hidden Layers
     */
    protected int numberOfHiddenLayers;
    /**
     * Number of Inputs
     */
    protected int numberOfInputs;
    /**
     * Number of Outputs
     */
    protected int numberOfOutputs;
    
    /**
     * Array of neural inputs
     */
    protected ArrayList<Double> input;
    /**
     * Array of neural outputs
     */
    protected ArrayList<Double> output;
    
    
    protected boolean activeBias=true;
    
    
    protected WeightInitialization weightInitialization = new UniformInitialization(0.0,1.0);
    
    
    protected int[] neuronsInHiddenLayers;
    
    
    protected int[] indexesWeightPerLayer;
    
    
    public enum NeuralNetMode { BUILD, TRAINING, RUN };
    
    protected NeuralNetMode neuralNetMode = NeuralNetMode.BUILD;
    
    /**
     * NeuralNet constructor
     * This constructor initializes the neural network by initializing all of 
     * the underlying layers and their respective neurons.
     * 
     * @param numberofinputs Number of Inputs of this Neural Network
     * @param numberofoutputs Number of Outputs of this Neural Network
     * @param numberofhiddenneurons Array containing the number of Neurons in 
     * each of the Hidden Layers
     * @param hiddenAcFnc Array containing the activation function of each 
     * Hidden Layer
     * @param outputAcFnc Activation Function of the Output Layer
     * @param _weightInitialization 
     */
    public NeuralNet(int numberofinputs,int numberofoutputs,
            int [] numberofhiddenneurons,IActivationFunction[] hiddenAcFnc,
            IActivationFunction outputAcFnc,
            WeightInitialization _weightInitialization){
        weightInitialization=_weightInitialization;
        numberOfHiddenLayers=numberofhiddenneurons.length;
        neuronsInHiddenLayers = new int[numberOfHiddenLayers+1];
        indexesWeightPerLayer = new int[numberOfHiddenLayers+2];  
        for(int i=0;i<=numberOfHiddenLayers;i++){
            if(i==numberOfHiddenLayers){
                neuronsInHiddenLayers[i]=numberofoutputs;
            }
            else{
                neuronsInHiddenLayers[i]=numberofhiddenneurons[i];
            }
            if(i==0){
                indexesWeightPerLayer[i]=0;
            }
            else{
                indexesWeightPerLayer[i]=indexesWeightPerLayer[i-1]
                        + (neuronsInHiddenLayers[i-1]*
                            ((i==1?numberofinputs:neuronsInHiddenLayers[i-2])
                            +1));
            }
        }
        if(numberOfHiddenLayers>0){
            indexesWeightPerLayer[numberOfHiddenLayers+1]=
                indexesWeightPerLayer[numberOfHiddenLayers]
                + neuronsInHiddenLayers[numberOfHiddenLayers]
                    *(neuronsInHiddenLayers[numberOfHiddenLayers-1]+1);
        }
        else{
            indexesWeightPerLayer[numberOfHiddenLayers+1]=
                indexesWeightPerLayer[numberOfHiddenLayers] 
                    + neuronsInHiddenLayers[numberOfHiddenLayers]
                        *(numberOfInputs+1);
        }
        numberOfInputs=numberofinputs;
        numberOfOutputs=numberofoutputs;
        if(numberOfHiddenLayers==hiddenAcFnc.length){
            input=new ArrayList<>(numberofinputs);
            inputLayer=new InputLayer(this,numberofinputs);
            if(numberOfHiddenLayers>0){
                hiddenLayer=new ArrayList<>(numberOfHiddenLayers);
            }
            for(int i=0;i<numberOfHiddenLayers;i++){
                if(i==0){
                    try{
                        hiddenLayer.set(i,new HiddenLayer(this,numberofhiddenneurons[i],
                            hiddenAcFnc[i],
                            inputLayer.getNumberOfNeuronsInLayer()));
                    }
                    catch(IndexOutOfBoundsException iobe){
                        hiddenLayer.add(new HiddenLayer(this,numberofhiddenneurons[i],
                            hiddenAcFnc[i],
                            inputLayer.getNumberOfNeuronsInLayer()));
                    }
                    inputLayer.setNextLayer(hiddenLayer.get(i));
                }
                else{
                    try{
                        hiddenLayer.set(i, new HiddenLayer(this,numberofhiddenneurons[i],
                             hiddenAcFnc[i],hiddenLayer.get(i-1)
                            .getNumberOfNeuronsInLayer()
                            ));
                    }
                    catch(IndexOutOfBoundsException iobe){
                        hiddenLayer.add(new HiddenLayer(this,numberofhiddenneurons[i],
                             hiddenAcFnc[i],hiddenLayer.get(i-1)
                            .getNumberOfNeuronsInLayer()
                            ));
                    }
                    hiddenLayer.get(i-1).setNextLayer(hiddenLayer.get(i));
                }
            }
            if(numberOfHiddenLayers>0){
                outputLayer=new OutputLayer(this,numberofoutputs,outputAcFnc,
                        hiddenLayer.get(numberOfHiddenLayers-1)
                        .getNumberOfNeuronsInLayer() 
                        );
                hiddenLayer.get(numberOfHiddenLayers-1).setNextLayer(outputLayer);
            }
            else{
                outputLayer=new OutputLayer(this,numberofoutputs, outputAcFnc,
                        numberofinputs);
                inputLayer.setNextLayer(outputLayer);
            }
        }
        setNeuralNetMode(NeuralNetMode.RUN);
    }
    
    public NeuralNet(int numberofinputs,int numberofoutputs,
            int [] numberofhiddenneurons,IActivationFunction[] hiddenAcFnc,
            IActivationFunction outputAcFnc){
        this(numberofinputs,numberofoutputs,numberofhiddenneurons,hiddenAcFnc
                ,outputAcFnc,new UniformInitialization(0.0,1.0));
    }
    
    
    public NeuralNet(int numberofinputs,int numberofoutputs,
            IActivationFunction outputAcFnc){
        this(numberofinputs,numberofoutputs,new int[0],new IActivationFunction[0],outputAcFnc);
    }
    
    protected NeuralNet(){
        
    }
    
    /**
     * Feeds an array of real values to the neural network's inputs
     * @param inputs Array of real values to be fed into the neural inputs
     */
    public void setInputs(ArrayList<Double> inputs){
        if(inputs.size()==numberOfInputs){
            this.input=inputs;
        }
    }
    
    /**
     * Sets a vector of double-precision values into the neural network inputs
     * @param inputs vector of values to be fed into the neural inputs
     */
    public void setInputs(double[] inputs){
        if(inputs.length==numberOfInputs){
            for(int i=0;i<numberOfInputs;i++){
                try{
                    input.set(i, inputs[i]);
                }
                catch(IndexOutOfBoundsException iobe){
                    input.add(inputs[i]);
                }
            }
        }
    }
    
    /**
     * Gets inputs values of neural net as ArrayList
     * @return array list inputs  
     */
    public ArrayList<Double> getArrayInputs(){
        return input;
    }
    
    /**
     * Gets input value of neuron i
     * @return double input value  
     */
    public Double getInput(int i){
        return input.get(i);
    }
    
    /**
     * Gets inputs values of neural net as double array
     * @return double array of inputs  
     */
    public double[] getInputs(){
        double[] result=new double[numberOfInputs];
        for(int i=0;i<numberOfInputs;i++){
            result[i]=input.get(i);
        }
        return result;
    }
    
    /**
     * This method calculates the output of each layer and forwards all values 
     * to the next layer
     */
    public void calc(){
        inputLayer.setInputs(input);
        inputLayer.calc();
        if(numberOfHiddenLayers>0){
            for(int i=0;i<numberOfHiddenLayers;i++){
                HiddenLayer hl = hiddenLayer.get(i);
                hl.setInputs(hl.getPreviousLayer().getOutputs());
                hl.calc();
            }
        }
        outputLayer.setInputs(outputLayer.getPreviousLayer().getOutputs());
        outputLayer.calc();
        this.output=outputLayer.getOutputs();
    }
    
    /**
     * Returns the neural outputs in the form of ArrayList
     * 
     * @return outputs as array list
     */
    public ArrayList<Double> getArrayOutputs(){
        return output;
    }
    
    /**
     * Returns the neural outputs in the form of array
     * 
     * @return outputs as array
     */
    public double[] getOutputs(){
        double[] _outputs = new double[numberOfOutputs];
        for(int i=0;i<numberOfOutputs;i++){
            _outputs[i]=output.get(i);
        }
        return _outputs;
    }
    
    /**
     * Gets output value of neuron i
     * @return double output value  
     */
    public double getOutput(int i){
        return output.get(i);
    }
    
    /**
     * Method to print the neural network information
     */
    public void print(){
        System.out.println("Neural Network: "+this.toString());
        System.out.println("\tInputs:"+String.valueOf(this.numberOfInputs));
        System.out.println("\tOutputs:"+String.valueOf(this.numberOfOutputs));
        System.out.println("\tHidden Layers: "+String.valueOf(numberOfHiddenLayers));
        for(int i=0;i<numberOfHiddenLayers;i++){
            System.out.println("\t\tHidden Layer "+
                    String.valueOf(i)+": "+
                    String.valueOf(this.hiddenLayer.get(i)
                            .numberOfNeuronsInLayer)+" Neurons");
        }
        
    }
    
    /**
     * Sets neural net dataset
     * @param neural net dataset object 
     */
    public void setNeuralDataSet(NeuralDataSet _neuralDataSet){
        _neuralDataSet.neuralNet=this;
    }
    
    /**
     * Gets number of hidden layers
     * @return number of hidden layers 
     */
    public int getNumberOfHiddenLayers(){
        return numberOfHiddenLayers;
    }
    
    /**
     * Gets number of inputs
     * @return number of inputs  
     */
    public int getNumberOfInputs(){
        return numberOfInputs;
    }
    
    /**
     * Gets number of outputs
     * @return number of outputs  
     */
    public int getNumberOfOutputs(){
        return numberOfOutputs;
    }
    
    /**
     * Gets input layer
     * @return input layer object 
     */
    public InputLayer getInputLayer(){
        return inputLayer;
    }
    
    /**
     * Gets hidden layer
     * @return hidden layer object 
     */
    public HiddenLayer getHiddenLayer(int i){
        return hiddenLayer.get(i);
    }
    
    /**
     * Gets hidden layers
     * @return hidden layers as ArrayList 
     */
    public ArrayList<HiddenLayer> getHiddenLayers(){
        return hiddenLayer;
    }
    
    /**
     * Gets output layer
     * @return output layer object 
     */
    public OutputLayer getOutputLayer(){
        return outputLayer;
    }
    
    /**
     * Deactivates bias
     */
    public void deactivateBias(){
        if(numberOfHiddenLayers>0){
            for(HiddenLayer hl:hiddenLayer){
                for(Neuron n:hl.getListOfNeurons()){
                    n.deactivateBias();
                }
            }
        }
        for(Neuron n:outputLayer.getListOfNeurons()){
            n.deactivateBias();
        }
    }
    
    /**
     * Activates bias
     */
    public void activateBias(){
        for(HiddenLayer hl:hiddenLayer){
            for(Neuron n:hl.getListOfNeurons()){
                n.activateBias();
            }
        }
        for(Neuron n:outputLayer.getListOfNeurons()){
            n.activateBias();
        }
    }
    
    /**
     * Returns if bias is activated or not
     * @return true if it's activated; else, false
     */
    public boolean isBiasActive(){
        return activeBias;
    }
    
    /**
     * Gets weight initialization
     * @return weight init object 
     */
    public WeightInitialization getWeightInitialization(){
        return weightInitialization;
    }
    
    /**
     * Gets weight values
     * @return all weights array 
     */
    public double[] getAllWeights(){
        int numberOfWeights=indexesWeightPerLayer[numberOfHiddenLayers+1];
        double[] weights=new double[numberOfWeights];
        for(int l=0;l<=numberOfHiddenLayers;l++){
            int j=0;
            NeuralLayer nl;
            if(l==numberOfHiddenLayers) // outputlayer
                nl = outputLayer;
            else
                nl = hiddenLayer.get(l);
            
            for(Neuron n:nl.getListOfNeurons()){
                for(int i=0;i<=n.getNumberOfInputs();i++){
                    weights[indexesWeightPerLayer[l]
                           +j*(neuronsInHiddenLayers[l]+1)
                           +i]=n.getWeight(i);
                }
                j++;
            }
        }
        return weights;
    }
    
    /**
     * Gets weight value
     * @param layer index
     * @param neuron index
     * @param input index
     * @return weight 
     */
    public double getWeight(int layer,int neuron,int input){
        if(layer==numberOfHiddenLayers){
            return outputLayer.getWeight(input, neuron);
        }
        else{
            return hiddenLayer.get(layer).getWeight(input, neuron);
        }
    }
    
    /**
     * Gets the total number of weights
     * @return total number of weights 
     */
    public int getTotalNumberOfWeights(){
        int result=0;
        for(HiddenLayer hl:this.hiddenLayer){
            result+=hl.numberOfNeuronsInLayer*(hl.numberOfInputs+1);
        }
        result+=outputLayer.numberOfNeuronsInLayer
                *(outputLayer.numberOfInputs+1);
        return result;
    }
    
    /**
     * Sets neural net mode
     * @param neural net mode 
     */
    public void setNeuralNetMode(NeuralNet.NeuralNetMode _neuralNetMode){
        this.neuralNetMode=_neuralNetMode;
    }
    
    /**
     * Gets neural net mode
     * @return neural net mode 
     */
    public NeuralNetMode getNeuralNetMode(){
        return this.neuralNetMode;
    }
    
}
