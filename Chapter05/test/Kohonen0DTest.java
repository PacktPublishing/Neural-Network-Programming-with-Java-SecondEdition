package edu.packt.neuralnet.test;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.ChartFrame;

import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.som.CompetitiveLearning;
import edu.packt.neuralnet.som.Kohonen;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashSet;
import java.util.Set;
import org.jfree.chart.ChartFrame;

/**
*
* Kohonen0DTest
* This class solely performs Kohonen 0D learning algorithm test 
* 
* @authors Alan de Souza, Fábio Soares 
* @version 0.1
* 
*/
public class Kohonen0DTest {
    
    public static void main(String[] args){
        
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=2;
        int numberOfNeurons=10;
        int numberOfPoints=100;
        
        double[][] rndDataSet = RandomNumberGenerator.GenerateMatrixBetween(numberOfPoints, numberOfInputs, -10.0, 10.0);
        
        Kohonen kn0 = new Kohonen(numberOfInputs,numberOfNeurons,new UniformInitialization(-1.0,1.0),0);
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(rndDataSet,2);
        
        CompetitiveLearning complrn=new CompetitiveLearning(kn0,neuralDataSet,LearningAlgorithm.LearningMode.ONLINE);
        complrn.show2DData=true;
        complrn.printTraining=true;
        complrn.setLearningRate(0.003);
        complrn.setMaxEpochs(10000);
        complrn.setReferenceEpoch(3000);
        
        try{
            String[] seriesNames = {"Training Data"};
            Paint[] seriesColor = {Color.WHITE};
            
            Chart chart = new Chart("Training",rndDataSet,seriesNames,0,seriesColor);
            ChartFrame frame = new ChartFrame("Training", chart.scatterPlot("X", "Y"));
            frame.pack();
            frame.setVisible(true);
            //System.in.read();
            complrn.setPlot2DFrame(frame);
            complrn.showPlot2DData();
            //System.in.read();
            complrn.train();
        }
        catch(Exception ne){
            
        }
    }
    
}
