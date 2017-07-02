/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.chart;

import java.awt.Paint;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;

/**
 * 
 * DataSeries This class creates data series to be used by Chart class. 
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public class DataSeries {
    
    private String seriesLabel;
    private int dimension = 2;
    private XYSeries series;
    private int length;
    private Paint color;
    
    private XYDataset dataset2d;
    private XYZDataset dataset3d;
    
    /**
	 * Data series constructor 
	 * @param chart label
	 * @param axis x data
	 * @param axis y data
	 */
    public DataSeries(String label,double[] xdata,double[] ydata){
        dimension=2;
        seriesLabel = label;
        series = new XYSeries(label);
        length=xdata.length;
        for(int i=0;i<length;i++){
            series.add(xdata[i],ydata[i]);
        }
        dataset2d = new XYSeriesCollection();
    }
    
}
