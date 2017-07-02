
package edu.packt.neuralnet.test;

import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.GaussianInitialization;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.som.CompetitiveLearning;
import edu.packt.neuralnet.som.Kohonen;

import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.ChartFrame;

/**
*
* Kohonen2DTest
* This class solely performs Kohonen 2D learning algorithm test 
* 
* @authors Alan de Souza, Fábio Soares 
* @version 0.1
* 
*/
public class Kohonen2DTest {
    
    public static void main(String[] args){
        
        RandomNumberGenerator.seed=System.currentTimeMillis();
        
        int numberOfInputs=2;
        int neuronsGridX=12;
        int neuronsGridY=12;
        int numberOfPoints=1000;
        
        double[][] rndDataSet;
        rndDataSet = RandomNumberGenerator.GenerateMatrixGaussian(numberOfPoints, numberOfInputs, 100.0, 1.0);
        //rndDataSet = RandomNumberGenerator.GenerateMatrixBetween(numberOfPoints, numberOfInputs, 100.0, 110.0);
        
        for (int i=0;i<numberOfPoints;i++){
            rndDataSet[i][0]*=Math.sin(i);            
            rndDataSet[i][0]+=RandomNumberGenerator.GenerateNext()*50;
            rndDataSet[i][1]*=Math.cos(i);            
            rndDataSet[i][1]+=RandomNumberGenerator.GenerateNext()*50;
        }
        
//        for (int i=0;i<numberOfPoints;i++){
//            rndDataSet[i][0]=i;            
//            rndDataSet[i][0]+=RandomNumberGenerator.GenerateNext();
//            rndDataSet[i][1]=Math.cos(i/100.0);            
//            rndDataSet[i][1]+=RandomNumberGenerator.GenerateNext()*5;
//        }        
        
        Kohonen kn2 = new Kohonen(numberOfInputs,neuronsGridX,neuronsGridY,new GaussianInitialization(500.0,20.0));
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(rndDataSet,2);
        
        CompetitiveLearning complrn=new 
            CompetitiveLearning(kn2,neuralDataSet
                    ,LearningAlgorithm.LearningMode.ONLINE);
        complrn.show2DData=true;
        complrn.printTraining=true;
        complrn.setLearningRate(0.5);
        complrn.setMaxEpochs(1000);
        complrn.setReferenceEpoch(300);
        complrn.sleep=-1;
        
        try{
            String[] seriesNames = {"Training Data"};
            Paint[] seriesColor = {Color.WHITE};
            
            Chart chart = new Chart("Training",rndDataSet,seriesNames,0,seriesColor,Chart.SeriesType.DOTS);
            ChartFrame frame = new ChartFrame("Training", chart.scatterPlot("X", "Y"));
            frame.pack();
            frame.setVisible(true);
//            //System.in.read();
            complrn.setPlot2DFrame(frame);
            complrn.showPlot2DData();
            //System.in.read();
            complrn.train();
        }
        catch(Exception ne){
            
        }
    }

}
