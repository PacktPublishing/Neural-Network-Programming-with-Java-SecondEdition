
package edu.packt.neuralnet.learn;

import edu.packt.neuralnet.HiddenLayer;
import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.OutputLayer;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.math.Matrix;

/**
 * This class implements ELM algorithm. 
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public class ELM extends DeltaRule {
    
    private Matrix H;
    private Matrix T;
    
    private boolean outputBiasActive = true;
    
    public ELM(NeuralNet _neuralNet){
        super(_neuralNet);
        learningMode=LearningMode.BATCH;
        initializeMatrices();
    }
    
    public ELM(NeuralNet _neuralNet,NeuralDataSet _trainDataSet){
        super(_neuralNet,_trainDataSet);
        learningMode=LearningMode.BATCH;
        initializeMatrices();
    }
    
    private void initializeMatrices(){
        int numberOfRecords = this.trainingDataSet.numberOfRecords;
        int numberOfHiddenNeurons = this.neuralNet.getHiddenLayer(0).getNumberOfNeuronsInLayer();
        int numberOfOutputs = this.trainingDataSet.numberOfOutputs;
        if(this.neuralNet.getOutputLayer().isBiasActive())
            outputBiasActive=true;
        else
            outputBiasActive=false;
        if(outputBiasActive)
            H = new Matrix(numberOfRecords,numberOfHiddenNeurons+1);
        else
            H = new Matrix(numberOfRecords,numberOfHiddenNeurons);
        T = new Matrix(numberOfRecords,numberOfOutputs);
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfOutputs;j++){
                double[] targetOutput = trainingDataSet.getTargetOutputRecord(i,normalization);
                T.setValue(i, j, targetOutput[j]);
            }
        }
    }
    
    @Override
    public void train() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()!=1)
            throw new NeuralException("The ELM learning algorithm can be performed only on Single Hidden Layer Neural Network");
        neuralNet.setNeuralNetMode(NeuralNet.NeuralNetMode.TRAINING);
        forward();
        int k=0;
        epoch=0;
        int N=trainingDataSet.numberOfRecords;
        currentRecord=0;
        double currentOverallError=overallGeneralError;
        if(printTraining){
            print();
        } 
        while(k<N){
            forward(k);
            buildMatrices();
            currentRecord=++k;
        }
        currentRecord=0;
        k=0;
        epoch++;
        applyNewWeights();
        forward();
        currentOverallError=overallGeneralError;
        if(printTraining){
            print();
        }         
        neuralNet.setNeuralNetMode(NeuralNet.NeuralNetMode.RUN);
    }
    
    private void buildMatrices(){
        HiddenLayer hl = this.neuralNet.getHiddenLayer(0);
        OutputLayer ol = this.neuralNet.getOutputLayer();
        int h= hl.getNumberOfNeuronsInLayer();
        for(int i=0;i<=h;i++){
            if(i==h){
                if(outputBiasActive)
                    H.setValue(currentRecord, i, 1.0);
            }
            else
                H.setValue(currentRecord, i, hl.getNeuron(i).getOutput());
        }
       
    }
    
    @Override
    public void forward(int i) throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()!=1){
            throw new NeuralException("ELM can be used only with single"
                    + " hidden layer neural network");
        }
        neuralNet.setInputs(trainingDataSet.getInputRecord(i,normalization));
        neuralNet.calc();
        trainingDataSet.setNeuralOutput(i, neuralNet.getOutputs(),normalization);
        generalError.set(i, 
                generalError(
                        trainingDataSet.getArrayTargetOutputRecord(i,normalization)
                        ,trainingDataSet.getArrayNeuralOutputRecord(i,normalization)));
        for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
            overallError.set(j, 
                    overallError(trainingDataSet
                            .getIthTargetOutputArrayList(j,normalization)
                            , trainingDataSet
                                    .getIthNeuralOutputArrayList(j,normalization)));
            error.get(i).set(j
                    ,simpleError(trainingDataSet
                            .getIthTargetOutputArrayList(j,normalization).get(i)
                            , trainingDataSet.getIthNeuralOutputArrayList(j,normalization)
                                    .get(i)));
        }
        overallGeneralError=overallGeneralErrorArrayList(
                trainingDataSet.getArrayTargetOutputData(normalization)
                ,trainingDataSet.getArrayNeuralOutputData(normalization));
        //simpleError=simpleErrorEach.get(i);
    }
    
    @Override 
    public void forward() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()!=1){
            throw new NeuralException("ELM can be used only with single"
                    + " hidden layer neural network");
        }
        for(int i=0;i<trainingDataSet.numberOfRecords;i++){
            neuralNet.setInputs(trainingDataSet.getInputRecord(i,normalization));
            neuralNet.calc();
            trainingDataSet.setNeuralOutput(i, neuralNet.getOutputs(),normalization);
            generalError.set(i, 
                generalError(
                        trainingDataSet.getArrayTargetOutputRecord(i,normalization)
                        ,trainingDataSet.getArrayNeuralOutputRecord(i,normalization)));
            for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
                error.get(i).set(j
                    ,simpleError(trainingDataSet
                            .getArrayTargetOutputRecord(i,normalization).get(j)
                            , trainingDataSet.getArrayNeuralOutputRecord(i,normalization)
                                    .get(j)));
            }
        }
        for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
            overallError.set(j, 
                    overallError(trainingDataSet
                            .getIthTargetOutputArrayList(j,normalization)
                            , trainingDataSet
                                    .getIthNeuralOutputArrayList(j,normalization)));
        }
        overallGeneralError=overallGeneralErrorArrayList(
                trainingDataSet.getArrayTargetOutputData(normalization)
                ,trainingDataSet.getArrayNeuralOutputData(normalization));
        //simpleError=simpleErrorEach.get(trainingDataSet.numberOfRecords-1);

    }
    
    
    @Override
    public void applyNewWeights(){
        Matrix Ht = H.transpose();
        Matrix HtH = Ht.multiply(H);
        Matrix invH = HtH.inverse();
        Matrix invHt = invH.multiply(Ht);
        Matrix beta = invHt.multiply(T);
        
        OutputLayer ol = this.neuralNet.getOutputLayer();
        HiddenLayer hl = (HiddenLayer)ol.getPreviousLayer();
        int h = hl.getNumberOfNeuronsInLayer();
        int n = ol.getNumberOfNeuronsInLayer();
        for(int i=0;i<=h;i++){
            for(int j=0;j<n;j++){
                if(i<h || outputBiasActive)
                    ol.getNeuron(j).updateWeight(i, beta.getValue(i, j));
            }
        }
    }
}
