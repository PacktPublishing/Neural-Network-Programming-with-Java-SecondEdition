/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.learn;

import edu.packt.neuralnet.HiddenLayer;
import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralLayer;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.Neuron;
import edu.packt.neuralnet.OutputLayer;
import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.data.TimeSeries;
import edu.packt.neuralnet.math.ArrayOperations;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import org.jfree.chart.ChartFrame;

/**
 *
 * @author fab
 */
public class Backpropagation extends DeltaRule {
    
    private double MomentumRate=0.7;
    
    public ArrayList<ArrayList<Double>> deltaNeuron;
    
    public ArrayList<ArrayList<ArrayList<Double>>> lastDeltaWeights;
  
    public Backpropagation(NeuralNet _neuralNet){
        super(_neuralNet);
        initializeDeltaNeuron();
        initializeLastDeltaWeights();
    }
    
    public Backpropagation(NeuralNet _neuralNet,NeuralDataSet _trainDataSet){
        super(_neuralNet,_trainDataSet);
        initializeDeltaNeuron();
        initializeLastDeltaWeights();
    }
    
    public Backpropagation(NeuralNet _neuralNet,NeuralDataSet _trainDataSet
            ,DeltaRule.LearningMode _learningMode){
        super(_neuralNet,_trainDataSet,_learningMode);
        initializeDeltaNeuron();
        initializeLastDeltaWeights();
    }
    
    private void initializeDeltaNeuron(){
        deltaNeuron=new ArrayList<>();
        int numberOfHiddenLayers =neuralNet.getNumberOfHiddenLayers();
        for(int l=0;l<=numberOfHiddenLayers;l++){
            int numberOfNeuronsInLayer;
            deltaNeuron.add(new ArrayList<Double>());
            if(l==numberOfHiddenLayers){
                numberOfNeuronsInLayer=neuralNet.getOutputLayer()
                        .getNumberOfNeuronsInLayer();
            }
            else{
                numberOfNeuronsInLayer=neuralNet.getHiddenLayer(l)
                        .getNumberOfNeuronsInLayer();
            }
            for(int j=0;j<numberOfNeuronsInLayer;j++){
                deltaNeuron.get(l).add(null);
            }
        }
    }
    
    private void initializeLastDeltaWeights(){
        this.lastDeltaWeights=new ArrayList<>();
        int numberOfHiddenLayers=this.neuralNet.getNumberOfHiddenLayers();
        for(int l=0;l<=numberOfHiddenLayers;l++){
            int numberOfNeuronsInLayer,numberOfInputsInNeuron;
            this.lastDeltaWeights.add(new ArrayList<ArrayList<Double>>());
            if(l<numberOfHiddenLayers){
                numberOfNeuronsInLayer=this.neuralNet.getHiddenLayer(l)
                        .getNumberOfNeuronsInLayer();
                for(int j=0;j<numberOfNeuronsInLayer;j++){
                    numberOfInputsInNeuron=this.neuralNet.getHiddenLayer(l)
                            .getNeuron(j).getNumberOfInputs();
                    this.lastDeltaWeights.get(l).add(new ArrayList<Double>());
                    for(int i=0;i<=numberOfInputsInNeuron;i++){
                        this.lastDeltaWeights.get(l).get(j).add(0.0);
                    }
                }
            }
            else{
                numberOfNeuronsInLayer=this.neuralNet.getOutputLayer()
                        .getNumberOfNeuronsInLayer();
                for(int j=0;j<numberOfNeuronsInLayer;j++){
                    numberOfInputsInNeuron=this.neuralNet.getOutputLayer()
                            .getNeuron(j).getNumberOfInputs();
                    this.lastDeltaWeights.get(l).add(new ArrayList<Double>());
                    for(int i=0;i<=numberOfInputsInNeuron;i++){
                        this.lastDeltaWeights.get(l).get(j).add(0.0);
                    }
                }
            }
        }
        
    }
    
    @Override
    public Double calcNewWeight(int layer,int input,int neuron){
        Double deltaWeight=calcDeltaWeight(layer,input,neuron);
        return newWeights.get(layer).get(neuron).get(input)+deltaWeight;
    }
    
    @Override
    public Double calcNewWeight(int layer,int input,int neuron,double error){
        return calcNewWeight(layer,input,neuron);
    }     
    
    public Double calcDeltaWeight(int layer,int input,int neuron) {
        Double deltaWeight=1.0;
        switch(learningMode){
            case BATCH:
                deltaWeight*=LearningRate /
                        ((double)(trainingDataSet.numberOfRecords));
            case ONLINE:
                deltaWeight*=LearningRate;
        }
        NeuralLayer currLayer;
        Neuron currNeuron;
        double _deltaNeuron;
        if(layer==neuralNet.getNumberOfHiddenLayers()){ //output layer
            currLayer=neuralNet.getOutputLayer();
            currNeuron=currLayer.getNeuron(neuron);
            _deltaNeuron=error.get(currentRecord).get(neuron)
                *currNeuron.derivative(currLayer.getInputs());
        }
        else{ //hidden layer
            currLayer=neuralNet.getHiddenLayer(layer);
            currNeuron=currLayer.getNeuron(neuron);
            double sumDeltaNextLayer=0;
            NeuralLayer nextLayer=currLayer.getNextLayer();
            for(int k=0;k<nextLayer.getNumberOfNeuronsInLayer();k++){
                sumDeltaNextLayer+=nextLayer.getWeight(neuron, k)
                        *deltaNeuron.get(layer+1).get(k);
            }
            _deltaNeuron=sumDeltaNextLayer*currNeuron.derivative(currLayer.getInputs());
            
        }
        
        deltaNeuron.get(layer).set(neuron, _deltaNeuron);
        deltaWeight*=_deltaNeuron;
        if(input<currNeuron.getNumberOfInputs()){
            deltaWeight*=currNeuron.getInput(input);
        }
        
        return deltaWeight;
    }
    
    
    @Override
    public void train() throws NeuralException{
        neuralNet.setNeuralNetMode(NeuralNet.NeuralNetMode.TRAINING);
        epoch=0;
        int k=0;
        currentRecord=0;
        
        double trainingErrorVariation=-1.0,testErrorVariation=-1.0;
        
        this.listOfErrorsByEpoch = new ArrayList<Double>();
        this.listOfTestErrorsByEpoch = new ArrayList<Double>();
        
        forward();
        //double currentOverallError=overallGeneralError;
        forward(k);
        listOfErrorsByEpoch.add(overallGeneralError);
        //trainingErrorVariation=overallGeneralError;
        if(testingDataSet!=null){
            test();        
            listOfTestErrorsByEpoch.add(testingOverallGeneralError);
            //testErrorVariation=testingOverallGeneralError;
        }
        if(printTraining){
            print();
        } 
        
        int countRiseError=0;
        
        while(epoch<MaxEpochs && overallGeneralError>MinOverallError
                && (testingDataSet==null || testErrorVariation<0.0 || countRiseError<4) ){
            backward();
            switch(learningMode){
                case BATCH:
                    if(k==trainingDataSet.numberOfRecords-1)
                        applyNewWeights();
                    break;
                case ONLINE:
                    applyNewWeights();
            }
            currentRecord=++k;
            if(k>=trainingDataSet.numberOfRecords){
                k=0;
                currentRecord=0;
                forward();
                listOfErrorsByEpoch.add(overallGeneralError);
                if(testingDataSet!=null){ 
                    test();
                    listOfTestErrorsByEpoch.add(testingOverallGeneralError);
                }
                epoch++;
                trainingErrorVariation=listOfErrorsByEpoch.get(epoch)-listOfErrorsByEpoch.get(epoch-1);
                if(testingDataSet!=null){
                    testErrorVariation=listOfTestErrorsByEpoch.get(epoch)-listOfTestErrorsByEpoch.get(epoch-1);
                    if(testErrorVariation<0.0)
                        countRiseError=0;
                    else
                        countRiseError++;
                }
            }
            forward(k);
            if(learningMode==LearningMode.ONLINE || (k==0)){
                if(printTraining)
                    print();
                if(showPlotError)
                    showErrorEvolution();
                if(showFittingPlot)
                    showFittingEvolution();
            } 
            
        }
        //setListOfErrorsByEpoch( listOfErrorsByEpoch );
        neuralNet.setNeuralNetMode(NeuralNet.NeuralNetMode.RUN);
    }
    
    @Override
    public void forward(int i) throws NeuralException{
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
    
    public void backward(){
        int numberOfLayers=neuralNet.getNumberOfHiddenLayers();
        for(int l=numberOfLayers;l>=0;l--){
            int numberOfNeuronsInLayer=deltaNeuron.get(l).size();
            for(int j=0;j<numberOfNeuronsInLayer;j++){
                for(int i=0;i<newWeights.get(l).get(j).size();i++){
                    double currNewWeight = this.newWeights.get(l).get(j).get(i);
                    if(currNewWeight==0.0 && epoch==0.0)
                        if(l==numberOfLayers)
                            currNewWeight=neuralNet.getOutputLayer().getWeight(i, j);
                        else
                            currNewWeight=neuralNet.getHiddenLayer(l).getWeight(i, j);
                    double deltaWeight=calcDeltaWeight(l, i, j);
                    newWeights.get(l).get(j).set(i,currNewWeight+deltaWeight);
                }
            }
        }
    }
    
    @Override 
    public void forward(){
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
    public void test(int i) throws NeuralException{
        neuralNet.setInputs(testingDataSet.getInputRecord(i,normalization));
        neuralNet.calc();
        testingDataSet.setNeuralOutput(i, neuralNet.getOutputs(),normalization);
        testingGeneralError.set(i, 
                generalError(
                        testingDataSet.getArrayTargetOutputRecord(i,normalization)
                        ,testingDataSet.getArrayNeuralOutputRecord(i,normalization)));
        for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
            testingOverallError.set(j, 
                    overallError(testingDataSet
                            .getIthTargetOutputArrayList(j,normalization)
                            , testingDataSet
                                    .getIthNeuralOutputArrayList(j,normalization)));
            testingError.get(i).set(j
                    ,simpleError(testingDataSet
                            .getIthTargetOutputArrayList(j,normalization).get(i)
                            , testingDataSet.getIthNeuralOutputArrayList(j,normalization)
                                    .get(i)));
        }
        testingOverallGeneralError=overallGeneralErrorArrayList(
                testingDataSet.getArrayTargetOutputData(normalization)
                ,testingDataSet.getArrayNeuralOutputData(normalization));
        //simpleError=simpleErrorEach.get(i);
    }
    
    @Override 
    public void test() throws NeuralException{
        for(int i=0;i<testingDataSet.numberOfRecords;i++){
            neuralNet.setInputs(testingDataSet.getInputRecord(i,normalization));
            neuralNet.calc();
            testingDataSet.setNeuralOutput(i, neuralNet.getOutputs(),normalization);
            testingGeneralError.set(i, 
                generalError(
                        testingDataSet.getArrayTargetOutputRecord(i,normalization)
                        ,testingDataSet.getArrayNeuralOutputRecord(i,normalization)));
            for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
                testingError.get(i).set(j
                    ,simpleError(testingDataSet
                            .getArrayTargetOutputRecord(i,normalization).get(j)
                            , testingDataSet.getArrayNeuralOutputRecord(i,normalization)
                                    .get(j)));
            }
        }
        for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
            testingOverallError.set(j, 
                    overallError(testingDataSet
                            .getIthTargetOutputArrayList(j,normalization)
                            , testingDataSet
                                    .getIthNeuralOutputArrayList(j,normalization)));
        }
        testingOverallGeneralError=overallGeneralErrorArrayList(
                testingDataSet.getArrayTargetOutputData(normalization)
                ,testingDataSet.getArrayNeuralOutputData(normalization));
            //simpleError=simpleErrorEach.get(trainingDataSet.numberOfRecords-1);
    }
    
    @Override
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
                        Double lastDeltaWeight=lastDeltaWeights.get(l).get(j).get(i);
                        double momentum=MomentumRate*lastDeltaWeight;
                        double newWeight=this.newWeights.get(l).get(j).get(i)
                                -momentum;
                        this.newWeights.get(l).get(j).set(i,newWeight);
                        Neuron n=hl.getNeuron(j);
                        double deltaWeight=(newWeight-n.getWeight(i));
                        lastDeltaWeights.get(l).get(j).set(i,(double)deltaWeight);
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
                        Double lastDeltaWeight=lastDeltaWeights.get(l).get(j).get(i);
                        double momentum=MomentumRate*lastDeltaWeight;
                        Neuron n=ol.getNeuron(j);
                        double newWeight=this.newWeights.get(l).get(j).get(i) + momentum;
                        this.newWeights.get(l).get(j).set(i,newWeight);
                        double deltaWeight=(newWeight-n.getWeight(i));
                        lastDeltaWeights.get(l).get(j).set(i,deltaWeight);
                        ol.getNeuron(j).updateWeight(i, newWeight);
                    }
                }
            }
        }        
    }
    
    public void setMomentumRate(double _momentumRate){
        this.MomentumRate=_momentumRate;
    }
    
    @Override
    public void showErrorEvolution(){
        
//        Chart chart = new Chart("Training",rndDataSet,seriesNames,0,seriesColor);
//            ChartFrame frame = new ChartFrame("Training", chart.scatterPlot("X", "Y"));
//            frame.pack();
//            frame.setVisible(true);
        double[] trainingErrors = ArrayOperations.arrayListToVector(getListOfErrorsByEpoch());
        double[] testingErrors = ArrayOperations.arrayListToVector(getListOfTestErrorsByEpoch());
        double[][] errors = new double[trainingErrors.length][3];
        for(int i=0;i<trainingErrors.length;i++){
            errors[i][0]=i;
            errors[i][1]=trainingErrors[i];
            errors[i][2]=testingErrors[i];
        }
        String[] seriesNames = {"Train Error","Test Error"};
        Paint[] seriesColor = {Color.BLUE, Color.GREEN};
        Chart c = new Chart("Error Evolution",errors,seriesNames,0,seriesColor,Chart.SeriesType.LINES);
        if(plotErrorEvolution ==null){
            plotErrorEvolution = new ChartFrame("Error Evolution",c.scatterPlot("Epoch","Error"));
            plotErrorEvolution.pack();
            plotErrorEvolution.setVisible(true);
        }
        else{
            plotErrorEvolution.getChartPanel().setChart(c.scatterPlot("Epoch","Error"));
        }
        
    }    
    
    @Override
    public void showFittingEvolution(){
        this.fittingEvolution.simulate();
        Paint[] comp_color = {Color.BLUE, Color.RED};
        int outCol = this.fittingEvolutionsOutput;
        this.fittingEvolutionDataSet.setColumnValues(this.fittingEvolution.getIthNeuralOutput(outCol), this.fittingEvolution.outputNames[outCol]);
        if(this.fittingEvolutionDataSet instanceof TimeSeries){
            plotFittingEvolution=((TimeSeries)(this.fittingEvolutionDataSet))
                    .getTimePlot(plotFittingEvolution
                            , "Comparison - epoch: "+String.valueOf(epoch)
                            , new String[]{
                                this.fittingEvolution.targetNames[outCol]
                                    ,this.fittingEvolution.outputNames[outCol]}
                            , comp_color, this.fittingEvolutionFilters[0][0]
                            , this.fittingEvolutionFilters[0][1]);
        }
    }

    @Override
    public void showScatterFittingEvolution(){
        
    }
    
}
