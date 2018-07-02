
package edu.packt.neuralnet.learn;

import edu.packt.neuralnet.HiddenLayer;
import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralLayer;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.Neuron;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.math.IdentityMatrix;
import edu.packt.neuralnet.math.Matrix;
import java.util.ArrayList;

/**
 * This class implements LevenbergMarquardt algorithm. 
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public class LevenbergMarquardt extends Backpropagation {
    
    private Matrix jacobian = null;
    private double damping=0.1;
    
    private ArrayList<ArrayList<ArrayList<Double>>> errorBackpropagation;
    private Matrix errorLMA;
    
    public ArrayList<ArrayList<ArrayList<Double>>> lastWeights;
    
    public LevenbergMarquardt(NeuralNet _neuralNet){
        super(_neuralNet);
    }
    
    public LevenbergMarquardt(NeuralNet _neuralNet,NeuralDataSet _trainDataSet){
        super(_neuralNet,_trainDataSet);
        initializeJacobian();
        initializeErrorBackpropagation();
        initializeErrorLMA();
        initializeLastWeights();
    }
    
    public LevenbergMarquardt(NeuralNet _neuralNet,NeuralDataSet _trainDataSet
        ,DeltaRule.LearningMode _learningMode){
        super(_neuralNet,_trainDataSet,_learningMode);
        initializeJacobian();
        initializeErrorBackpropagation();
        initializeErrorLMA();   
        initializeLastWeights();
    }
    
    private void initializeJacobian(){
        if(this.neuralNet!=null && this.trainingDataSet!=null){
            int numberOfOutputs=neuralNet.getNumberOfOutputs();
            int numberOfRecords=trainingDataSet.numberOfRecords;
            int numberOfWeights=neuralNet.getTotalNumberOfWeights();
            jacobian=new Matrix(numberOfRecords*numberOfOutputs,numberOfWeights);
        }
    }
    
    private void initializeErrorBackpropagation(){
        int numberOfOut = this.neuralNet.getNumberOfOutputs();
        int numberOfHl = this.neuralNet.getNumberOfHiddenLayers();
        errorBackpropagation=new ArrayList<>();
        for(int m=0;m<numberOfOut;m++){
            errorBackpropagation.add(new ArrayList<ArrayList<Double>>());
            for(int l=0;l<=numberOfHl;l++){
                int numberOfNl;
                if(l==numberOfHl){
                    numberOfNl=this.neuralNet.getOutputLayer().getNumberOfNeuronsInLayer();
                }
                else{
                    numberOfNl=this.neuralNet.getHiddenLayer(l).getNumberOfNeuronsInLayer();
                }
                errorBackpropagation.get(m).add(new ArrayList<Double>());
                for(int j=0;j<numberOfNl;j++){
                    errorBackpropagation.get(m).get(l).add(0.0);
                }
            }
        }
    }
    
    private void initializeErrorLMA(){
        int numberOfOutputs=this.neuralNet.getNumberOfOutputs();
        int numberOfRecords=this.trainingDataSet.numberOfRecords;
        errorLMA = new Matrix(numberOfOutputs*numberOfRecords,1);
    }
    
    private void initializeLastWeights(){
        int numberOfLayers=neuralNet.getNumberOfHiddenLayers();
        int numberOfInputs=neuralNet.getNumberOfInputs();
        lastWeights=new ArrayList<>();
        for(int l=0;l<=numberOfLayers;l++){
            lastWeights.add(new ArrayList<ArrayList<Double>>());
            NeuralLayer nl;
            if(l==numberOfLayers)
                nl=neuralNet.getOutputLayer();
            else
                nl=neuralNet.getHiddenLayer(l);
            int numberOfNeurons=nl.getNumberOfNeuronsInLayer();
            for(int j=0;j<numberOfNeurons;j++){
                lastWeights.get(l).add(new ArrayList<Double>());
                int inputs;
                if(l==0)
                    inputs=numberOfInputs;
                else
                    inputs=nl.getPreviousLayer().getNumberOfNeuronsInLayer();
                for(int i=0;i<=inputs;i++){
                    lastWeights.get(l).get(j).add(0.0);
                }
            }
        }
    }
    
    @Override
    public void setTrainingDataSet(NeuralDataSet _trainingDataSet){
        this.trainingDataSet=_trainingDataSet;
        initializeJacobian();
    }
    
    @Override
    public void train() throws NeuralException{
        neuralNet.setNeuralNetMode(NeuralNet.NeuralNetMode.TRAINING);
        epoch=0;
        int k=0;
        currentRecord=0;
        forward();
        double currentOverallError=overallGeneralError;
        forward(k);
        double currentGeneralError=generalError(k);
        buildErrorVector();
        if(printTraining){
            print();
        } 
        while(epoch<MaxEpochs && overallGeneralError>MinOverallError && damping<=10000000000.0){
            backward();
            calculateJacobian();
            switch(learningMode){
                case BATCH:
                    if(k==trainingDataSet.numberOfRecords-1){
                        applyNewWeights();
                        forward();
                        if(overallGeneralError<currentOverallError){
                            damping/=10.0;
                            currentOverallError=overallGeneralError;
                        }
                        else{
                            damping*=10.0;
                            restoreLastWeights();
                            forward();
                        }
                    }
                    break;
                case ONLINE:
                    applyNewWeights();
            }
            currentRecord=++k;
            if(k>=trainingDataSet.numberOfRecords){
                k=0;
                currentRecord=0;
                epoch++;
            }

            forward(k);
            if(learningMode==LearningMode.ONLINE){
                if(generalError(k)<currentGeneralError){
                    damping/=10.0;
                    currentGeneralError=generalError(k);
                }
                else{
                    if(epoch<=5){
                        damping*=10.0;
                        restoreLastWeights();
                    }
                }
                
                forward(k);
            }
            if(printTraining && (learningMode==LearningMode.ONLINE || (k==0))){
                print();
            }
            buildErrorVector();
            
        }
        neuralNet.setNeuralNetMode(NeuralNet.NeuralNetMode.RUN);

    }
    
    public void buildErrorVector(){
        int numberOfOutputs = trainingDataSet.numberOfOutputs;
        int k=currentRecord*numberOfOutputs;
        for(int j=0;j<numberOfOutputs;j++){
            errorLMA.setValue(k++,0,error.get(currentRecord).get(j));
        }
    }
    
   
    @Override
    public void backward(){
        int numberOfOutputs = neuralNet.getNumberOfOutputs();
        int numberOfLayers = neuralNet.getNumberOfHiddenLayers();
        for(int m=0;m<numberOfOutputs;m++){
            for(int l=numberOfLayers;l>=0;l--){
                if(l==numberOfLayers){
                    double slope=neuralNet.getOutputLayer().getNeuron(m).getFirstDerivative();
                    errorBackpropagation.get(m).get(l).set(m, slope);
                }
                else if(l==numberOfLayers-1){
                    int numberOfNeuronsInLayer=neuralNet.getHiddenLayer(l).getNumberOfNeuronsInLayer();
                    for(int j=0;j<numberOfNeuronsInLayer;j++){
                        double slope=neuralNet.getHiddenLayer(l).getNeuron(j).getFirstDerivative();
                        double errorbackprop=errorBackpropagation.get(m).get(l+1).get(m);
                        double weight=neuralNet.getOutputLayer().getWeight(j, m);
                        errorBackpropagation.get(m).get(l).set(j, weight*errorbackprop*slope);
                    }
                }
                else{
                    HiddenLayer hl = neuralNet.getHiddenLayer(l);
                    NeuralLayer nl = hl.getNextLayer();
                    int numberOfNeuronsInLayer=hl.getNumberOfNeuronsInLayer();
                    int numberOfNeuronsNextLayer=nl.getNumberOfNeuronsInLayer();
                    for(int j=0;j<numberOfNeuronsInLayer;j++){
                        double errorBackprop=0;
                        for(int k=0;k<numberOfNeuronsNextLayer;k++){
                            double weight=nl.getWeight(j, k);
                            errorBackprop+=weight*errorBackpropagation.get(m).get(l+1).get(k);
                        }
                        double slopeNeuron=hl.getNeuron(j).getFirstDerivative();
                        errorBackpropagation.get(m).get(l).set(j,slopeNeuron*errorBackprop);
                    }
                }
            }
        }
    }
    
    
    public void calculateJacobian(){
        int numberOfOutputs=trainingDataSet.numberOfOutputs;
        int currentRow = currentRecord*numberOfOutputs;
        int numberOfLayers=neuralNet.getNumberOfHiddenLayers();
        int m=0;
        for(int i=currentRow;i<currentRow+numberOfOutputs;i++){
            int j=0;
            for(int l=0;l<=numberOfLayers;l++){
                NeuralLayer nl;
                if(l==numberOfLayers)
                    nl=neuralNet.getOutputLayer();
                else
                    nl=neuralNet.getHiddenLayer(l);
                int numberOfNeurons=nl.getNumberOfNeuronsInLayer();
                int numberOfInputs;
                if(l==0)
                    numberOfInputs=neuralNet.getNumberOfInputs();
                else
                    numberOfInputs=nl.getPreviousLayer().getNumberOfNeuronsInLayer();
                for(int k=0;k<numberOfNeurons;k++){
                    Neuron n = nl.getNeuron(k);
                    for(int p=0;p<=numberOfInputs;p++){
                        double input;
                        if(p==numberOfInputs)
                            input=1.0;
                        else
                            input = n.getInput(p);
                        double deltaBackprop = errorBackpropagation.get(m).get(l).get(k);
                        jacobian.setValue(i, j++, deltaBackprop*input);
                    }
                }
            }
            m++;
        }
    }
    
    @Override
    public void applyNewWeights(){
        int numberOfOutputs=neuralNet.getNumberOfOutputs();
        int rowi=0;
        int rowe=trainingDataSet.numberOfRecords*numberOfOutputs-1;
        int numberOfWeights=neuralNet.getTotalNumberOfWeights();
        switch(learningMode){
            case ONLINE:
                rowi=currentRecord*numberOfOutputs;
                rowe=rowi+numberOfOutputs-1;
                break;
            case BATCH:
                
                break;
        }
        Matrix jacob=jacobian.subMatrix(rowi, rowe, 0, numberOfWeights-1);
        Matrix errorVec = errorLMA.subMatrix(rowi, rowe, 0, 0);
        Matrix pseudoHessian=jacob.transpose().multiply(jacob);
        Matrix miIdent = new IdentityMatrix(numberOfWeights).multiply(damping);
        Matrix inverseHessianMi = pseudoHessian.add(miIdent).inverse();
        Matrix deltaWeight = inverseHessianMi.multiply(jacob.transpose()).multiply(errorVec);
        
        int i=0;
        int numberOfLayers=neuralNet.getNumberOfHiddenLayers();
        for(int l=0;l<=numberOfLayers;l++){
            NeuralLayer nl;
            if(l==numberOfLayers)
                nl=neuralNet.getOutputLayer();
            else
                nl=neuralNet.getHiddenLayer(l);
            for(int k=0;k<nl.getNumberOfNeuronsInLayer();k++){
                int numberOfInputs;
                if(l==0){
                    numberOfInputs=neuralNet.getNumberOfInputs();
                }
                else{
                    numberOfInputs=nl.getPreviousLayer().getNumberOfNeuronsInLayer();
                }
                for(int j=0;j<=numberOfInputs;j++){
                    Neuron n=nl.getNeuron(k);
                    double currWeight=n.getWeight(j);
                    double newWeight=currWeight+deltaWeight.getValue(i++,0);
                    newWeights.get(l).get(k).set(j,newWeight);
                    lastWeights.get(l).get(k).set(j,currWeight);
                    n.updateWeight(j, newWeight);
                }
            }
        }
    }
    
    public void restoreLastWeights(){
        int numberOfLayers=neuralNet.getNumberOfHiddenLayers();
        for(int l=0;l<=numberOfLayers;l++){
            NeuralLayer nl;
            if(l==numberOfLayers)
                nl=neuralNet.getOutputLayer();
            else
                nl=neuralNet.getHiddenLayer(l);
            for(int k=0;k<nl.getNumberOfNeuronsInLayer();k++){
                int numberOfInputs;
                if(l==0){
                    numberOfInputs=neuralNet.getNumberOfInputs();
                }
                else{
                    numberOfInputs=nl.getPreviousLayer().getNumberOfNeuronsInLayer();
                }
                for(int j=0;j<=numberOfInputs;j++){
                    Neuron n=nl.getNeuron(k);
                    double lastWeight =lastWeights.get(l).get(k).get(j);
                    n.updateWeight(j, lastWeight);
                }
            }
        }
    }
    
    public double generalError(int k){
        return generalError(
                        trainingDataSet.getArrayTargetOutputRecord(k)
                        ,trainingDataSet.getArrayNeuralOutputRecord(k));
    }
    
    public void setDamping(double _damping){
        this.damping=_damping;
    }
    
}
