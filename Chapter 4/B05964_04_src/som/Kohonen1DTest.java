/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.som;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.ChartFrame;

import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import java.awt.Color;
import java.awt.Paint;
import java.util.HashSet;
import java.util.Set;
import org.jfree.chart.ChartFrame;

/**
 *
 * @author fab
 */
public class Kohonen1DTest {
    
    public static void main(String[] args){
        
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=2;
        int numberOfNeurons=20;
        int numberOfPoints=1000;
        
        double[][] rndDataSet = RandomNumberGenerator.GenerateMatrixBetween(numberOfPoints, numberOfInputs, -100.0, 100.0);
        
        for (int i=0;i<numberOfPoints;i++){
            rndDataSet[i][0]=i;            
            rndDataSet[i][0]+=RandomNumberGenerator.GenerateNext();
            rndDataSet[i][1]=Math.cos(i/100.0)*1000;            
            rndDataSet[i][1]+=RandomNumberGenerator.GenerateNext()*400;
        }
        
        Kohonen kn1 = new Kohonen(numberOfInputs,numberOfNeurons,new UniformInitialization(0.0,1000.0),1);
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(rndDataSet,2);
        
        CompetitiveLearning complrn=new CompetitiveLearning(kn1,neuralDataSet,LearningAlgorithm.LearningMode.ONLINE);
        complrn.show2DData=true;
        complrn.printTraining=true;
        complrn.setLearningRate(0.3);
        complrn.setMaxEpochs(10000);
        complrn.setReferenceEpoch(3000);
        try{
            String[] seriesNames = {"Training Data"};
            Paint[] seriesColor = {Color.WHITE};
            
            Chart chart = new Chart("Training",rndDataSet,seriesNames,0,seriesColor,Chart.SeriesType.DOTS);
            ChartFrame frame = new ChartFrame("Training", chart.scatterPlot("X", "Y"));
            frame.pack();
            frame.setVisible(true);
            
            complrn.setPlot2DFrame(frame);
            complrn.showPlot2DData();
            System.in.read();
            
            complrn.train();
        }
        catch(Exception ne){
            
        }
    }
    
}