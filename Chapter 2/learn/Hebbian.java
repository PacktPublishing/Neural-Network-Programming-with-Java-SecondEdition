/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.learn;

import edu.packt.neuralnet.HiddenLayer;
import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.Neuron;
import edu.packt.neuralnet.OutputLayer;
import edu.packt.neuralnet.data.NeuralDataSet;
import java.util.ArrayList;

/**
 *
 * @author fab
 */
public class Hebbian extends LearningAlgorithm {
    
    private int currentRecord=0;
    
    private ArrayList<ArrayList<ArrayList<Double>>> newWeights;
    
    private ArrayList<Double> currentOutputMean;
    
    private ArrayList<Double> lastOutputMean;
    
   
    public Hebbian(NeuralNet _neuralNet){
        this.learningParadigm=LearningParadigm.UNSUPERVISED;
        this.neuralNet=_neuralNet;
        this.newWeights=new ArrayList<>();
        int numberOfHiddenLayers=this.neuralNet.getNumberOfHiddenLayers();
        for(int l=0;l<=numberOfHiddenLayers;l++){
            int numberOfNeuronsInLayer,numberOfInputsInNeuron;
            this.newWeights.add(new ArrayList<ArrayList<Double>>());
            if(l<numberOfHiddenLayers){
                numberOfNeuronsInLayer=this.neuralNet.getHiddenLayer(l)
                        .getNumberOfNeuronsInLayer();
                for(int j=0;j<numberOfNeuronsInLayer;j++){
                    numberOfInputsInNeuron=this.neuralNet.getHiddenLayer(l)
                            .getNeuron(j).getNumberOfInputs();
                    this.newWeights.get(l).add(new ArrayList<Double>());
                    for(int i=0;i<=numberOfInputsInNeuron;i++){
                        this.newWeights.get(l).get(j).add(0.0);
                    }
                }
            }
            else{
                numberOfNeuronsInLayer=this.neuralNet.getOutputLayer()
                        .getNumberOfNeuronsInLayer();
                for(int j=0;j<numberOfNeuronsInLayer;j++){
                    numberOfInputsInNeuron=this.neuralNet.getOutputLayer()
                            .getNeuron(j).getNumberOfInputs();
                    this.newWeights.get(l).add(new ArrayList<Double>());
                    for(int i=0;i<=numberOfInputsInNeuron;i++){
                        this.newWeights.get(l).get(j).add(0.0);
                    }
                }
            }
        }
    }
    
    public Hebbian(NeuralNet _neuralNet,NeuralDataSet _trainDataSet){
        this(_neuralNet);
        this.trainingDataSet=_trainDataSet;
    }
    
    public Hebbian(NeuralNet _neuralNet,NeuralDataSet _trainDataSet
            ,LearningMode _learningMode){
        this(_neuralNet,_trainDataSet);
        this.learningMode=_learningMode;
    }
    
    @Override
    public Double calcNewWeight(int layer,int input,int neuron) 
            throws NeuralException{
        if(layer>0){
            throw new NeuralException("Hebbian can be used only with single"
                    + " layer neural network yet");
        }
        else{
            Double deltaWeight=LearningRate;
            Neuron currNeuron=neuralNet.getOutputLayer().getNeuron(neuron);
            switch(learningMode){
                case BATCH:
                    ArrayList<Double> _ithInput;
                    if(input<currNeuron.getNumberOfInputs()){
                        _ithInput=trainingDataSet.getIthInputArrayList(input);
                    }
                    else{
                        _ithInput=new ArrayList<>();
                        for(int i=0;i<trainingDataSet.numberOfRecords;i++){
                            _ithInput.add(0.0);
                        }
                    }
                    Double multResultIthInput=0.0;
                    for(int i=0;i<trainingDataSet.numberOfRecords;i++){
                        multResultIthInput+=
                                trainingDataSet.getArrayNeuralOutputRecord(i).get(neuron)
                                *_ithInput.get(i);
                    }
                    deltaWeight*=multResultIthInput;
                    break;
                case ONLINE:
                    deltaWeight*=currNeuron.getOutput();
                    if(input<currNeuron.getNumberOfInputs()){
                        deltaWeight*=neuralNet.getInput(input);
                    }
                    break;
            }

            return currNeuron.getWeight(input)+deltaWeight;
        }
    }
    
    @Override
    public Double calcNewWeight(int layer,int input,int neuron,double error) 
            throws NeuralException{
        throw new NeuralException("Hebbian learning can be used only with the "
                + "neuron's inputs and outputs, no error is used");
     
    }
    
    @Override
    public void train() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Hebbian learning can be used only with "
                    + "single layer neural network");
        }
        else{
            switch(learningMode){
                case BATCH:
                    epoch=0;
                    forward();
                    if(printTraining){
                        print();
                    }         
                    setLastOutputMean();
                    while(!stopCriteria()){
                        epoch++;                        
                        for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
                            for(int i=0;i<=neuralNet.getNumberOfInputs();i++){
                                //weightUpdate(0, i, j,overallError.get(j));
                                newWeights.get(0).get(j).set(i, calcNewWeight(0,i,j));
                            }
                        }
                        applyNewWeights();
                        setLastOutputMean();
                        forward();
                        if(printTraining){
                            print();
                        }                        
                    }
                    break;
                case ONLINE:
                    epoch=0;
                    int k=0;
                    currentRecord=0;
                    if(currentOutputMean.get(0)==null){
                        forward();
                    }
                    forward(k);
                    if(printTraining){
                        print();
                    }                    
                    setLastOutputMean();
                    while(!stopCriteria()){
                        for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
                            for(int i=0;i<=neuralNet.getNumberOfInputs();i++){
                                //weightUpdate(0, i, j,error.get(currentRecord)
                                //        .get(j));
                                newWeights.get(0).get(j).set(i,calcNewWeight(0,i,j));
                            }
                        }   
                        applyNewWeights();
                        currentRecord=++k;
                        if(k>=trainingDataSet.numberOfRecords){
                            k=0;
                            setLastOutputMean();
                            currentOutputMean=trainingDataSet.getMeanNeuralOutput();
                            currentRecord=0;
                            epoch++;
                        }
                        forward(k);
                        if(printTraining){
                            print();
                        }                        
                    }
                    
                    break;
                
            }
        }
    }
    
    public void applyNewWeights(){
        int numberOfHiddenLayers=this.neuralNet.getNumberOfHiddenLayers();
        for(int l=0;l<=numberOfHiddenLayers;l++){
            int numberOfNeuronsInLayer,numberOfInputsInNeuron;
            if(l<numberOfHiddenLayers){
                HiddenLayer hl = this.neuralNet.getHiddenLayer(l);
                numberOfNeuronsInLayer=hl.getNumberOfNeuronsInLayer();
                for(int j=0;j<numberOfNeuronsInLayer;j++){
                    numberOfInputsInNeuron=hl.getNeuron(j).getNumberOfInputs();
                    for(int i=0;i<=numberOfInputsInNeuron;i++){
                        double newWeight=this.newWeights.get(l).get(j).get(i);
                        hl.getNeuron(j).updateWeight(i, newWeight);
                    }
                }
            }
            else{
                OutputLayer ol = this.neuralNet.getOutputLayer();
                numberOfNeuronsInLayer=ol.getNumberOfNeuronsInLayer();
                for(int j=0;j<numberOfNeuronsInLayer;j++){
                    numberOfInputsInNeuron=ol.getNeuron(j).getNumberOfInputs();
                    
                    for(int i=0;i<=numberOfInputsInNeuron;i++){
                        double newWeight=this.newWeights.get(l).get(j).get(i);
                        ol.getNeuron(j).updateWeight(i, newWeight);
                    }
                }
            }
        }        
    }

    @Override
    public void forward(int i) throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Hebbian learning can be used only with "
                    + "single layer neural network");
        }
        else{
            neuralNet.setInputs(trainingDataSet.getArrayInputRecord(i));
            neuralNet.calc();
            trainingDataSet.setNeuralOutput(i, neuralNet.getOutputs());

            //simpleError=simpleErrorEach.get(i);
        }
    }
    
    @Override 
    public void forward() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Hebbian learning can be used only with "
                    + "single layer neural network");
        }
        else{
            for(int i=0;i<trainingDataSet.numberOfRecords;i++){
                neuralNet.setInputs(trainingDataSet.getInputRecord(i));
                neuralNet.calc();
                trainingDataSet.setNeuralOutput(i, neuralNet.getOutputs());
            }
            currentOutputMean=trainingDataSet.getMeanNeuralOutput();
            //simpleError=simpleErrorEach.get(trainingDataSet.numberOfRecords-1);
        }
    }

    @Override
    public void test(int i) throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Hebbian learning can be used only with "
                    + "single layer neural network");
        }
        else{
            neuralNet.setInputs(testingDataSet.getArrayInputRecord(i));
            neuralNet.calc();
            testingDataSet.setNeuralOutput(i, neuralNet.getOutputs());

            //simpleError=simpleErrorEach.get(i);
        }
    }
    
    @Override 
    public void test() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Hebbian learning can be used only with "
                    + "single layer neural network");
        }
        else{
            for(int i=0;i<testingDataSet.numberOfRecords;i++){
                neuralNet.setInputs(testingDataSet.getInputRecord(i));
                neuralNet.calc();
                testingDataSet.setNeuralOutput(i, neuralNet.getOutputs());
            }
            //currentOutputMean=trainingDataSet.getMeanNeuralOutput();
            //simpleError=simpleErrorEach.get(trainingDataSet.numberOfRecords-1);
        }
    }

    
    @Override 
    public void print(){
        if(learningMode==LearningMode.ONLINE)
            System.out.println("Epoch="+String.valueOf(epoch)+"; Record="
                    +String.valueOf(currentRecord));
        else
            System.out.println("Epoch= "+String.valueOf(epoch));
    }
    
    public boolean stopCriteria(){
        boolean stop=true;
        for(int i=0;i<currentOutputMean.size();i++){
            if(currentOutputMean.get(i)<=lastOutputMean.get(i))
                stop=false;
        }
        return stop || epoch>=MaxEpochs;
    }
    
    private void setLastOutputMean(){
        lastOutputMean=new ArrayList<>();
        for(Double d:currentOutputMean){
            lastOutputMean.add(d);
        }
    }
    
    
}
