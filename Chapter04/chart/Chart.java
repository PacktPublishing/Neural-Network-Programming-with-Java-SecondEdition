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
    
    public Chart(String chartLabel, double[][] data, String[] seriesLabel, int xcolumn, Paint[] color){
        chartTitle=chartLabel;
        
        addSeries(data,seriesLabel,xcolumn,color,Chart.SeriesType.LINES);
    }
    
    public Chart(String chartLabel, double[][] data,String[] seriesLabel, int xcolumn, Paint[] color, Chart.SeriesType _seriesType){
        chartTitle=chartLabel;
        addSeries(data,seriesLabel,xcolumn,color,_seriesType);
    }
    
    private void addSeries(double[][] data,String[] seriesLabel,int xcolumn,Paint[] color){
        int length = data.length;
        int numberOfSeries = data[0].length-1;
        //int currentSize = seriesColor.size();
        
        for (int j = 0; j < numberOfSeries+1; j++) {
            XYSeriesCollection seriesCollection = new XYSeriesCollection();
            XYSeries series = new XYSeries(seriesLabel[0],false);
            if(j==xcolumn)
                continue;
            else{
                seriesColor.add(color[(j>=xcolumn)?j-1:j]);
                
                for(int i=0;i<length;i++){
                    series.add( data[i][xcolumn], data[i][j] ,false);
                }
                seriesCollection.addSeries(series);
            }
            dataset.add(seriesCollection);
        }
    }
    
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
    
    
    public void plotWeather(ArrayList<double[]> data, String chartTitle, String xAxisLabel, String yAxisLabel, ArrayList<String> listOfChartLabels) {
		
        XYSeriesCollection dataset = new XYSeriesCollection();
    	
        for(int line_i = 0; line_i < data.size(); line_i++) {
            XYSeries seriesMin = null;
            XYSeries seriesMax = null;
            seriesMin = new XYSeries( "-1.0 oC" );
            seriesMax = new XYSeries( "+1.0 oC" );

            XYSeries series = null;
            series = new XYSeries( listOfChartLabels.get(line_i) );

            double[] line = data.get(line_i);

            for (int rows_j = 0; rows_j < line.length; rows_j++) {
                series.add( (rows_j+1), line[rows_j] );
                seriesMin.add( (rows_j+1), line[rows_j] - 1.0 );
                seriesMax.add( (rows_j+1), line[rows_j] + 1.0 );
            }
            dataset.addSeries(series);
            if(line_i == 1) { //add only to target values
                dataset.addSeries(seriesMin);
                dataset.addSeries(seriesMax);
            }
        }
    	
    	JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,		// chart title
                xAxisLabel,     // x axis label
                yAxisLabel,     // y axis label
                dataset,        // data
                PlotOrientation.VERTICAL,
                true,           // include legend
                true,           // tooltips
                false           // urls
        );
		
        XYPlot plot = chart.getXYPlot();
        chart.getXYPlot().getRangeAxis().setRange(22, 30);
		
        for(int line_i = 0; line_i < data.size(); line_i++) {
            plot.getRenderer().setSeriesStroke(line_i, new BasicStroke(2.0f));
        }
		
        //dashed line (min and max):
        XYLineAndShapeRenderer rendererMin = (XYLineAndShapeRenderer) plot.getRenderer();
        XYLineAndShapeRenderer rendererMax = (XYLineAndShapeRenderer) plot.getRenderer();
        rendererMin.setSeriesPaint(2, Color.black);
        rendererMax.setSeriesPaint(3, Color.black);

        rendererMin.setSeriesStroke(2, new BasicStroke(
              2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
              1.0f, new float[] {1.0f, 6.0f}, 0.0f));
        rendererMax.setSeriesStroke(3, new BasicStroke(
			  2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
	          1.0f, new float[] {1.0f, 6.0f}, 0.0f));
		
        ChartFrame frame = new ChartFrame("Neural Net Chart", chart);
        frame.pack();
        frame.setVisible(true);
    	
    }
    
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
    
//    public JFreeChart blockPlot(String xAxisLabel, String yAxisLabel){
//        NumberAxis xAxis = new NumberAxis("Theta");
//        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//        xAxis.setLowerMargin(0.0);
//        xAxis.setUpperMargin(0.0);
//        xAxis.setAxisLinePaint(Color.white);
//        xAxis.setTickMarkPaint(Color.white);
//
//        NumberAxis yAxis = new NumberAxis("Phi");
//        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//        yAxis.setLowerMargin(0.0);
//        yAxis.setUpperMargin(0.0);
//        yAxis.setAxisLinePaint(Color.white);
//        yAxis.setTickMarkPaint(Color.white);
//
//        XYBlockRenderer renderer = new XYBlockRenderer();
//        LookupPaintScale paintScale = new LookupPaintScale(min, max,
//                Color.gray);
//   //code adding paints to paintScale
//        renderer.setPaintScale(paintScale);
//
//        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
//        plot.setBackgroundPaint(Color.lightGray);
//        //plot.setDomainGridlinesVisible(false);
//        plot.setRangeGridlinePaint(Color.white);
//        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));
//        plot.setOutlinePaint(Color.blue);
//        JFreeChart chart = new JFreeChart(patternType.toUpperCase(), plot);
//        chart.removeLegend();
//        NumberAxis scaleAxis = new NumberAxis("Scale");
//        scaleAxis.setAxisLinePaint(Color.white);
//        scaleAxis.setTickMarkPaint(Color.white);
//        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 7));
//        scaleAxis.setRange(min, max);
//        PaintScaleLegend legend = new PaintScaleLegend(paintScale,
//                scaleAxis);
//        legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
//        legend.setAxisOffset(5.0);
//        legend.setMargin(new RectangleInsets(5, 5, 5, 5));
//        legend.setFrame(new BlockBorder(Color.red));
//        legend.setPadding(new RectangleInsets(10, 10, 10, 10));
//        legend.setStripWidth(10);
//        legend.setPosition(RectangleEdge.RIGHT);
//        legend.setBackgroundPaint(new Color(120, 120, 180));
//        chart.addSubtitle(legend);
//        chart.setBackgroundPaint(new Color(180, 180, 250));
//        return chart;
//    }

}
