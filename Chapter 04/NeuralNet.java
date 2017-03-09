package edu.packt.neuralnet;

import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.init.WeightInitialization;
import edu.packt.neuralnet.math.IActivationFunction;
import java.util.ArrayList;

/**
 *
 * NeuralNet
 * This class represents the Neural Network itself. It contains all the 
 * definitions that a Neural Network has, including method for calculation 
 * (forward).
 * 
 * @author Alan de Souza, Fábio Soares
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
    
    
    protected WeightInitialization weightInitialization
            =new UniformInitialization(0.0,1.0);
    
    
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
     * setInputs
     * Feeds an array of real values to the neural network's inputs
     * @param inputs Array of real values to be fed into the neural inputs
     */
    public void setInputs(ArrayList<Double> inputs){
        if(inputs.size()==numberOfInputs){
            this.input=inputs;
        }
    }
    
    /**
     * setInputs
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
    
    public ArrayList<Double> getArrayInputs(){
        return input;
    }
    
    public Double getInput(int i){
        return input.get(i);
    }
    
    public double[] getInputs(){
        double[] result=new double[numberOfInputs];
        for(int i=0;i<numberOfInputs;i++){
            result[i]=input.get(i);
        }
        return result;
    }
    
    /**
     * calc
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
     * getArrayOutputs
     * @return Returns the neural outputs in the form of Array
     */
    public ArrayList<Double> getArrayOutputs(){
        return output;
    }
    
    /**
     * getOutputs
     * @return Returns the neural outputs in the form of vector
     */
    public double[] getOutputs(){
        double[] _outputs = new double[numberOfOutputs];
        for(int i=0;i<numberOfOutputs;i++){
            _outputs[i]=output.get(i);
        }
        return _outputs;
    }
    
    public double getOutput(int i){
        return output.get(i);
    }
    
    /**
     * print
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
    
    public void setNeuralDataSet(NeuralDataSet _neuralDataSet){
        _neuralDataSet.neuralNet=this;
    }
    
    public int getNumberOfHiddenLayers(){
        return numberOfHiddenLayers;
    }
    
    public int getNumberOfInputs(){
        return numberOfInputs;
    }
    
    public int getNumberOfOutputs(){
        return numberOfOutputs;
    }
    
    public InputLayer getInputLayer(){
        return inputLayer;
    }
    
    public HiddenLayer getHiddenLayer(int i){
        return hiddenLayer.get(i);
    }
    
    public ArrayList<HiddenLayer> getHiddenLayers(){
        return hiddenLayer;
    }
    
    public OutputLayer getOutputLayer(){
        return outputLayer;
    }
    
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
    
    public boolean isBiasActive(){
        return activeBias;
    }
    
    public WeightInitialization getWeightInitialization(){
        return weightInitialization;
    }
    
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
    
    public double getWeight(int layer,int neuron,int input){
        if(layer==numberOfHiddenLayers){
            return outputLayer.getWeight(input, neuron);
        }
        else{
            return hiddenLayer.get(layer).getWeight(input, neuron);
        }
    }
    
    public int getTotalNumberOfWeights(){
        int result=0;
        for(HiddenLayer hl:this.hiddenLayer){
            result+=hl.numberOfNeuronsInLayer*(hl.numberOfInputs+1);
        }
        result+=outputLayer.numberOfNeuronsInLayer
                *(outputLayer.numberOfInputs+1);
        return result;
    }
    
    public void setNeuralNetMode(NeuralNet.NeuralNetMode _neuralNetMode){
        this.neuralNetMode=_neuralNetMode;
    }
    
    public NeuralNetMode getNeuralNetMode(){
        return this.neuralNetMode;
    }
    
}
