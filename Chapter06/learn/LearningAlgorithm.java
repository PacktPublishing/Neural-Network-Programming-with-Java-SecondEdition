
package edu.packt.neuralnet.learn;

import java.util.ArrayList;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.DataNormalization;
import edu.packt.neuralnet.data.DataSet;
import edu.packt.neuralnet.data.NeuralDataSet;
import org.jfree.chart.ChartFrame;

/**
 * This class supports learning algorithms. 
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public abstract class LearningAlgorithm {
    
    protected NeuralNet neuralNet;
    
    public enum LearningMode {ONLINE,BATCH};
    
    protected enum LearningParadigm {SUPERVISED,UNSUPERVISED};
    
    protected LearningMode learningMode;
    
    protected LearningParadigm learningParadigm;
    
    protected int MaxEpochs=100;
    
    protected int epoch=0;
    
    protected double MinOverallError=0.001;
    
    protected double LearningRate=0.1;
    
    protected NeuralDataSet trainingDataSet;
    
    protected NeuralDataSet testingDataSet;
    
    protected NeuralDataSet validatingDataSet;
    
    protected NeuralDataSet fittingEvolution;
    
    protected int fittingEvolutionsOutput;
    
    protected int[] fittingEvolutionFilterColumns;
    
    protected double[][] fittingEvolutionFilters; 
    
    protected DataSet fittingEvolutionDataSet;
    
    protected ArrayList<Double> listOfErrorsByEpoch;
    
    protected ArrayList<Double> listOfTestErrorsByEpoch;
    
    public boolean printTraining=false;
    
    public boolean showPlotError=false;
    
    public boolean showScatterPlot=false;
    
    public boolean showFittingPlot=false;
    
    protected ChartFrame plotErrorEvolution;
    
    protected ChartFrame plotFittingEvolution;
    
    protected ChartFrame plotScatterFittingEvolution;
    
    protected boolean normalization=false;
    
    public abstract void train() throws NeuralException;
    
    public abstract void forward() throws NeuralException;
    
    public abstract void forward(int i) throws NeuralException;
    
    public abstract Double calcNewWeight(int layer,int input,int neuron) throws NeuralException;
    
    public abstract Double calcNewWeight(int layer,int input,int neuron,double error) throws NeuralException;
    
    public abstract void test() throws NeuralException;
    
    public abstract void test(int i) throws NeuralException;
    
    public abstract void print();
    
    public abstract void showErrorEvolution();
    
    public abstract void showFittingEvolution();
    
    public abstract void showScatterFittingEvolution();
    
    public ArrayList<Double> getListOfErrorsByEpoch() {
	return listOfErrorsByEpoch;
    }

    public ArrayList<Double> getListOfTestErrorsByEpoch() {
	return listOfTestErrorsByEpoch;
    }    

    public void setListOfErrorsByEpoch(ArrayList<Double> listOfErrorsByEpoch) {
	this.listOfErrorsByEpoch = listOfErrorsByEpoch;
    }

    public void setMaxEpochs(int _maxEpochs){
        this.MaxEpochs=_maxEpochs;
    }
    
    public int getMaxEpochs(){
        return this.MaxEpochs;
    }
    
    public int getEpoch(){
        return epoch;
    }
    
    public void setMinOverallError(Double _minOverallError){
        this.MinOverallError=_minOverallError;
    }
    
    public Double getMinOverallError(){
        return this.MinOverallError;
    }
    
    public void setLearningRate(Double _learningRate){
        this.LearningRate=_learningRate;
    }
    
    public Double getLearningRate(){
        return this.LearningRate;
    }
    
    public void setLearningMode(LearningMode _learningMode){
        this.learningMode=_learningMode;
    }
    
    public LearningMode getLearningMode(){
        return this.learningMode;
    }
    
    public void setTestingDataSet(NeuralDataSet _testingDataSet){
        this.testingDataSet=_testingDataSet;
    }
    
    public void setTrainingDataSet(NeuralDataSet _trainingDataSet){
        this.trainingDataSet=_trainingDataSet;
    }
    
    public void setPlot(ChartFrame frame){
        this.plotErrorEvolution=frame;
    }
    
    public ChartFrame getPlot(){
        return this.plotErrorEvolution;
    }
    
    public void setNormalization(DataNormalization.NormalizationTypes nType){
        normalization=true;
        if(trainingDataSet!=null)
            trainingDataSet.setNormalization(nType);
        if(testingDataSet!=null)
            testingDataSet.setNormalization(nType);
        if(validatingDataSet!=null)
            validatingDataSet.setNormalization(nType);
    }
    
    public void setNormalization(double _scaleNorm){
        normalization=true;
        if(trainingDataSet!=null)
            trainingDataSet.setNormalization(_scaleNorm);
        if(testingDataSet!=null)
            testingDataSet.setNormalization(_scaleNorm);
        if(validatingDataSet!=null)
            validatingDataSet.setNormalization(_scaleNorm);
    }
    
    public void setNormalization(double _minNorm,double _maxNorm){
        normalization=true;
        if(trainingDataSet!=null)
            trainingDataSet.setNormalization(_minNorm,_maxNorm);
        if(testingDataSet!=null)
            testingDataSet.setNormalization(_minNorm,_maxNorm);
        if(validatingDataSet!=null)
            validatingDataSet.setNormalization(_minNorm,_maxNorm);
    }
    
    public void setFittingEvolution(DataSet _dataset, NeuralDataSet _fittingEvolution,int _outputColumn,int[] _filterColumns,double[][] _filters,ChartFrame ref){
        this.showFittingPlot=true;
        this.fittingEvolution=_fittingEvolution;
        this.fittingEvolutionDataSet=_dataset;
        if(fittingEvolution.neuralNet!=this.neuralNet)
            fittingEvolution.neuralNet=this.neuralNet;
        this.fittingEvolutionsOutput=_outputColumn;
        this.fittingEvolutionFilterColumns=_filterColumns;
        this.fittingEvolutionFilters=_filters;
        this.plotFittingEvolution=ref;
    }
    
}
