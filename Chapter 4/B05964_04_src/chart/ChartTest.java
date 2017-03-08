package edu.packt.neuralnet.chart;

import edu.packt.neuralnet.math.RandomNumberGenerator;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import org.jfree.chart.ChartFrame;

public class ChartTest {

	public static void main(String[] args) {
		ArrayList<Double> dados1 = new ArrayList<Double>();
		dados1.add(1.0);
		dados1.add(2.0);
		dados1.add(4.0);
		dados1.add(8.0);
		dados1.add(16.0);
		dados1.add(32.0);
		dados1.add(64.0);
		dados1.add(128.0);
		
		Chart c = new Chart();
		
		c.plot(dados1, "Line plot", "X axis", "Y axis");
                
            int numberOfInputs=2;
            int numberOfNeurons=10;
            int numberOfPoints=100;
        
            double[][] rndDataSet = RandomNumberGenerator.GenerateMatrixBetween(numberOfPoints, numberOfInputs, -10.0, 10.0);
            
            String[] seriesNames = {"Scatter Plot"};
            Paint[] seriesColor = {Color.WHITE};
            
            Chart chart = new Chart("Scatter Plot",rndDataSet,seriesNames,0,seriesColor,Chart.SeriesType.DOTS);
            ChartFrame frame = new ChartFrame("Scatter Plot", chart.scatterPlot("X Axis", "Y Axis"));
            frame.pack();
            frame.setVisible(true);
		
	}
	
}
