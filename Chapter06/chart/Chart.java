package edu.packt.neuralnet.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;

/**
 * 
 * Chart This class allows create and manipulate charts. 
 * It is based in JFreeChart library (www.jfree.org/jfreechart/)
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public class Chart {
    
    private String chartTitle;
    private ArrayList<XYDataset> dataset = new ArrayList<XYDataset>();
    
    private JFreeChart jfChart;
    private ArrayList<Paint> seriesColor = new ArrayList<>();
    
    private ArrayList<XYZDataset> dataset3d = new ArrayList<XYZDataset>();
    
    public enum SeriesType {DOTS,LINES};
    
    public ArrayList<SeriesType> seriesTypes = new ArrayList<SeriesType>();
    
    public Chart(){
        
    }
    
    /**
	 * Chart constructor 
	 * @param chart label
	 * @param data matrix
	 * @param series label array
	 * @param xcolumn
	 * @param colors array
	 */
    public Chart(String chartLabel, double[][] data, String[] seriesLabel, int xcolumn, Paint[] color){
        chartTitle=chartLabel;
        
        addSeries(data,seriesLabel,xcolumn,color,Chart.SeriesType.LINES);
    }
    
    /**
	 * Chart constructor 
	 * @param chart label
	 * @param data matrix
	 * @param series label array
	 * @param xcolumn
	 * @param colors array
	 * @param chart type
	 */
    public Chart(String chartLabel, double[][] data,String[] seriesLabel, int xcolumn, Paint[] color, Chart.SeriesType _seriesType){
        chartTitle=chartLabel;
        addSeries(data,seriesLabel,xcolumn,color,_seriesType);
    }
    
    /**
	 * Adds chart series
	 * @param data matrix
	 * @param series label array
	 * @param xcolumn
	 * @param colors array
	 */
    private void addSeries(double[][] data,String[] seriesLabel,int xcolumn,Paint[] color){
        int length = data.length;
        int numberOfSeries = data[0].length-1;
        //int currentSize = seriesColor.size();
        
        for (int j = 0; j < numberOfSeries+1; j++) {
            XYSeriesCollection seriesCollection = new XYSeriesCollection();
            if(j==xcolumn)
                continue;
            else{
                XYSeries series = new XYSeries(seriesLabel[(j>=xcolumn)?j-1:j],false);
                seriesColor.add(color[(j>=xcolumn)?j-1:j]);
                
                for(int i=0;i<length;i++){
                    series.add( data[i][xcolumn], data[i][j] ,false);
                }
                seriesCollection.addSeries(series);
            }
            dataset.add(seriesCollection);
        }
    }
    
    /**
	 * Adds chart series
	 * @param data matrix
	 * @param series label array
	 * @param xcolumn
	 * @param colors array
	 * @param chart type
	 */
    public void addSeries(double[][] data,String[] seriesLabel,int xcolumn,Paint[] color,Chart.SeriesType _seriesType){
        this.addSeries(data,seriesLabel,xcolumn,color);
        for(int i=0;i<seriesLabel.length;i++)
            seriesTypes.add(_seriesType);
    }
    
    public void add3DSeries(double[][] data,String seriesLabel,double scaleX,double scaleY){
        
    }

    /**
     * Charting the values of a vector
     * @param data
     * @param chartTitle
     * @param xAxisLabel
     * @param yAxisLabel
     */
    public void plot(ArrayList<Double> data, String chartTitle, String xAxisLabel, String yAxisLabel) {
    
        int length = data.size();
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries(yAxisLabel);
        
        for (int i = 0; i < length; i++) {
            series.add( (i + 1), (double) data.get(i) );
        }
        
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
				chartTitle, // chart title
				xAxisLabel, // x axis label
				yAxisLabel, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, 
				true, // include legend
				true, // tooltips
				false // urls
        );
		
        XYPlot plot = chart.getXYPlot();
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));
        plot.getRenderer().setSeriesPaint(0, Color.BLACK);

        ChartFrame frame = new ChartFrame(chartTitle, chart);
        frame.pack();
        frame.setVisible(true);

    }
    
    /**
     * Performing line plot
     * @param xLabel
     * @param yLabel
     */
    public JFreeChart linePlot(String xLabel, String yLabel){
        int numDatasets = dataset.size();
        JFreeChart result = ChartFactory.createXYLineChart(
				chartTitle, // chart title
				xLabel, // x axis label
				yLabel, // y axis label
				dataset.get(0), // data
				PlotOrientation.VERTICAL, 
				true, // include legend
				true, // tooltips
				false // urls
        );
        XYPlot plot = result.getXYPlot();
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(1.0f));
        plot.getRenderer().setSeriesPaint(0, seriesColor.get(0));
        for(int i=1;i<numDatasets;i++){
            plot.setDataset(i,dataset.get(i));
            //XYItemRenderer renderer = plot.getRenderer(i-0);
            //plot.setRenderer(i, new XYLineAndShapeRenderer(false, true));
            plot.getRenderer(i).setSeriesStroke(0, new BasicStroke(1.0f));
            plot.getRenderer(i).setSeriesPaint(0,seriesColor.get(i));
        }

        return result;
    }
    
    /**
     * Performing scatter plot
     * @param xAxisLabel
     * @param yAxisLabel
     */
    public JFreeChart scatterPlot(String xAxisLabel,String yAxisLabel){
        int numDatasets = dataset.size();
        JFreeChart result = ChartFactory.createScatterPlot(chartTitle
                , xAxisLabel
                , yAxisLabel
                , dataset.get(0));
        XYPlot plot = result.getXYPlot();
        switch(seriesTypes.get(0)){
                case DOTS:
                    plot.setRenderer(0, new XYLineAndShapeRenderer(false, true));
                    break;
                case LINES:
                    plot.setRenderer(0, new XYLineAndShapeRenderer(true, true));
                    break;
            }
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(1.0f));
        plot.getRenderer().setSeriesPaint(0, seriesColor.get(0));        
        for(int i=1;i<numDatasets;i++){
            plot.setDataset(i,dataset.get(i));
            //XYItemRenderer renderer = plot.getRenderer(i-0);
            switch(seriesTypes.get(i)){
                case DOTS:
                    plot.setRenderer(i, new XYLineAndShapeRenderer(false, true));
                    break;
                case LINES:
                    plot.setRenderer(i, new XYLineAndShapeRenderer(true, true));
                    break;
            }
            plot.getRenderer(i).setSeriesStroke(0, new BasicStroke(1.0f));
            plot.getRenderer(i).setSeriesPaint(0,seriesColor.get(i));
        }

        return result;
    }
    
    
    /**
     * Performing scatter plot with grid
     * @param xLabel
     * @param yLabel
     */
    public JFreeChart scatterGridPlot(String xLabel, String yLabel){
        int numDatasets = dataset.size();
        JFreeChart result = ChartFactory.createScatterPlot(chartTitle
                , xLabel
                , yLabel
                , dataset.get(0));
        XYPlot plot = result.getXYPlot();
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(1.0f));
        plot.getRenderer().setSeriesPaint(0, seriesColor.get(0));        
        for(int i=1;i<numDatasets;i++){
            plot.setDataset(i,dataset.get(i));
            //XYItemRenderer renderer = plot.getRenderer(i-0);
            plot.setRenderer(i, new XYLineAndShapeRenderer(true, true));
            plot.getRenderer(i).setSeriesStroke(0, new BasicStroke(1.0f));
            plot.getRenderer(i).setSeriesPaint(0,seriesColor.get(i));
        }

        return result;
    }
    
}
