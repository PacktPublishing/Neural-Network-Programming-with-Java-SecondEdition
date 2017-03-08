/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.som;

import edu.packt.neuralnet.HiddenLayer;
import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.Neuron;
import edu.packt.neuralnet.OutputLayer;
import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.ArrayOperations;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;

import org.jfree.chart.ChartFrame;

/**
 *
 * @author fab
 */
public class CompetitiveLearning extends LearningAlgorithm {

    private int currentRecord=0;
    
    private ArrayList<ArrayList<Double>> newWeights;
    
    private ArrayList<ArrayList<Double>> currWeights;

    private double initialLearningRate = 0.3;
    
    private int referenceEpoch = 30;
    
    private int[] indexWinnerNeuronTrain;
    
    private int[] indexWinnerNeuronTest;
    
    protected ChartFrame plot2DData;
    
    public boolean show2DData=false;
    
    public long sleep=-1;
    
    public CompetitiveLearning(NeuralNet _neuralNet){
        this.learningParadigm=LearningParadigm.UNSUPERVISED;
        this.neuralNet=_neuralNet;
        this.newWeights=new ArrayList<>();
        this.currWeights=new ArrayList<>();
        int numberOfNeuronsInLayer=this.neuralNet.getOutputLayer()
                        .getNumberOfNeuronsInLayer();
        for(int j=0;j<numberOfNeuronsInLayer;j++){
            int numberOfInputsInNeuron=this.neuralNet.getOutputLayer()
                            .getNeuron(j).getNumberOfInputs();
            this.newWeights.add(new ArrayList<Double>());
            this.currWeights.add(new ArrayList<Double>());
            for(int i=0;i<=numberOfInputsInNeuron;i++){
                this.newWeights.get(j).add(0.0);
                this.currWeights.get(j).add(neuralNet.getOutputLayer().getWeight(i, j));
            }

        }
        
        
    }
    
    public CompetitiveLearning(NeuralNet _neuralNet,NeuralDataSet _trainDataSet){
        this(_neuralNet);
        this.trainingDataSet=_trainDataSet;
        indexWinnerNeuronTrain=new int[_trainDataSet.numberOfRecords];
    }
    
    public CompetitiveLearning(NeuralNet _neuralNet,NeuralDataSet _trainDataSet
            ,LearningMode _learningMode){
        this(_neuralNet,_trainDataSet);
        this.learningMode=_learningMode;
    }    
    
    @Override
    public Double calcNewWeight(int layer,int input,int neuron) 
            throws NeuralException{
        if(layer>0){
            throw new NeuralException("Competitive learning can be used only with single"
                    + " layer neural network yet");
        }
        else{
            Double deltaWeight=getLearningRate(epoch);
            //Neuron n=neuralNet.getOutputLayer().getNeuron(neuron);
            double xi=neuralNet.getInput(input);
            double wi=neuralNet.getOutputLayer().getWeight(input, neuron);
            int wn = indexWinnerNeuronTrain[currentRecord];
            CompetitiveLayer cl = ((CompetitiveLayer)(((Kohonen)(neuralNet))
                    .getOutputLayer()));
            switch(learningMode){
                case BATCH:
                case ONLINE:
                    deltaWeight*=cl.neighborhood(wn, neuron, epoch, referenceEpoch)*(xi-wi);
                    break;
            }

            return deltaWeight;
        }
    }
    
    
    public double getLearningRate(int epoch){
        double exponent=(double)(epoch)/(double)(referenceEpoch);
        return initialLearningRate*Math.exp(-exponent);
    }
    
    public double getDistance(double[] pointX, double[] pointW){
        int size=pointX.length;
        Kohonen.Distance distance = ((Kohonen)(neuralNet)).distanceMeasure;
        double totalDistance=0;
        switch(distance){
            case EUCLIDIAN:
            default:
                for(int i=0;i<size;i++){
                    totalDistance+=Math.pow(pointX[i]-pointW[i],2);
                }
                totalDistance=Math.sqrt(totalDistance);                
        }
        return totalDistance;
    }
    
   
    @Override
    public Double calcNewWeight(int layer,int input,int neuron,double error) 
            throws NeuralException{
        throw new NeuralException("Competitive learning can be used only with the "
                + "neuron's inputs and outputs, no error is used");
     
    }
    
    @Override
    public void train() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Competitive learning can be used only with "
                    + "single layer neural network");
        }
        else{
            epoch=0;
            int k=0;
            forward();
            if(printTraining){
                print();
            }
            if(show2DData){
                showPlot2DData();
            }
            currentRecord=0;
            forward(currentRecord);
            while(!stopCriteria()){
                for(int j=0;j<neuralNet.getNumberOfOutputs();j++){
                    for(int i=0;i<neuralNet.getNumberOfInputs();i++){
                                //weightUpdate(0, i, j,error.get(currentRecord)
                                //        .get(j));
                        double newWeight=newWeights.get(j).get(i);
                        newWeights.get(j).set(i,newWeight+calcNewWeight(0,i,j));
                    }
                }   
                switch(learningMode){
                    case BATCH:
                        break;
                    case ONLINE:
                    default:
                        applyNewWeights();
                }
                currentRecord=++k;
                if(k>=trainingDataSet.numberOfRecords){
                    if(learningMode==LearningAlgorithm.LearningMode.BATCH){
                        applyNewWeights();
                    }
                    k=0;
                    currentRecord=0;
                    epoch++;
                    if(show2DData){
                        showPlot2DData();
                        if(sleep!=-1)
                            try{
                                Thread.sleep(sleep);
                            }
                            catch(Exception e){}
                    }                    
                }
                forward(k);
                if(printTraining){
                    print();
                }  
            }
        }
    }
    
    public void applyNewWeights(){
        int numberOfNeuronsInLayer,numberOfInputsInNeuron;
        CompetitiveLayer cl = (CompetitiveLayer)this.neuralNet.getOutputLayer();
        numberOfNeuronsInLayer=cl.getNumberOfNeuronsInLayer();
        for(int j=0;j<numberOfNeuronsInLayer;j++){
            numberOfInputsInNeuron=cl.getNeuron(j).getNumberOfInputs();
            for(int i=0;i<=numberOfInputsInNeuron;i++){
                double newWeight=this.newWeights.get(j).get(i);
                double currWeight=this.currWeights.get(j).get(i);
                this.currWeights.get(j).set(i, currWeight+newWeight);
                cl.getNeuron(j).updateWeight(i, currWeight+newWeight);
                this.newWeights.get(j).set(i,0.0);
            }
        }
    }

    @Override
    public void forward(int i) throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Kohonen learning can be used only with "
                    + "single layer neural network");
        }
        else{
            neuralNet.setInputs(trainingDataSet.getArrayInputRecord(i));
            ((Kohonen)(neuralNet)).calc();
            indexWinnerNeuronTrain[i]=((Kohonen)(neuralNet)).getIndexWinnerNeuron();
            //simpleError=simpleErrorEach.get(i);
        }
    }
    
    @Override 
    public void forward() throws NeuralException{
        if(neuralNet.getNumberOfHiddenLayers()>0){
            throw new NeuralException("Competitive learning can be used only with "
                    + "single layer neural network");
        }
        else{
            for(int i=0;i<trainingDataSet.numberOfRecords;i++){
                neuralNet.setInputs(trainingDataSet.getInputRecord(i));
                neuralNet.calc();
                indexWinnerNeuronTrain[i]=((Kohonen)(neuralNet)).getIndexWinnerNeuron();

            }
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
            indexWinnerNeuronTest[i]=((Kohonen)(neuralNet)).getIndexWinnerNeuron();

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
                indexWinnerNeuronTest[i]=((Kohonen)(neuralNet)).getIndexWinnerNeuron();
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
            if(currentRecord==0)
                System.out.println("Epoch= "+String.valueOf(epoch));
        
    }
    
    public boolean stopCriteria(){
        //boolean stop=true;
        return epoch>=MaxEpochs;
    }
    
    @Override
    public void showErrorEvolution(){
        
    }
    
    public void setPlot2DFrame(ChartFrame frame){
        this.plot2DData=frame;
    }
    
    public void showPlot2DData(){
        
        double[][] data= ArrayOperations.arrayListToDoubleMatrix(trainingDataSet.inputData.data);
        String[] seriesNames = {"Training Data"};
        Paint[] seriesColor = {Color.WHITE};
            
        Chart chart = new Chart("Training epoch n°"+String.valueOf(epoch)+" ",data,seriesNames,0,seriesColor,Chart.SeriesType.DOTS);
        if(plot2DData ==null){
            plot2DData = new ChartFrame("Training",chart.scatterPlot("X","Y"));
        }
        
        Paint[] newColor={Color.BLUE};
        String[] neuronsNames={""};
        CompetitiveLayer cl = ((CompetitiveLayer)(neuralNet.getOutputLayer()));
        double[][] neuronsWeights = cl.getWeights();
        switch(cl.dimension){
            case TWO_DIMENSION:
                ArrayList<double[][]> gridWeights = ((CompetitiveLayer2D)(cl)).getGridWeights();
                for(int i=0;i<gridWeights.size();i++){
                    chart.addSeries(gridWeights.get(i),neuronsNames,0,newColor,Chart.SeriesType.LINES);
                }
                break;
            case ONE_DIMENSION:
                neuronsNames[0]="Neurons Weights";
                chart.addSeries(neuronsWeights, neuronsNames, 0,newColor,Chart.SeriesType.LINES);
                break;
            case ZERO:
                neuronsNames[0]="Neurons Weights";
            default:
                chart.addSeries(neuronsWeights, neuronsNames, 0,newColor,Chart.SeriesType.DOTS);
        }
        
        
            
        plot2DData.getChartPanel().setChart(chart.scatterPlot("X", "Y"));
    }
    
    public ChartFrame getPlot2DFrame(){
        return plot2DData;
    }
    

    public void setLearningRate(double _learningRate){
        this.LearningRate=_learningRate;
        this.initialLearningRate=_learningRate;
    }
    
    public void setReferenceEpoch(int _refEpoch){
        this.referenceEpoch=_refEpoch;
    }
    
}
