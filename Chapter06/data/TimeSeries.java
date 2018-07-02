/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.data;

import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.math.ArrayOperations;
import java.awt.Paint;
import java.util.ArrayList;
import org.jfree.chart.ChartFrame;

/**
 * 
 * TimeSeries This class creates time series to be used by Chart class. 
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public class TimeSeries extends DataSet {
    
    private int indexTimeColumn;
    
    public TimeSeries(double[][] _data,String[] _columns){
        super(_data,_columns);
    }
    
    public TimeSeries(String path, String filename){
        super(path,filename);
    }
    
    public TimeSeries(String filename){
        super(filename,false,",");
    }
    
    public TimeSeries(DataSet ds){
        super(ds.getData(),ds.getColumns());
    }
    
    public void setIndexColumn(int col){
        this.indexTimeColumn=col;
        this.sortBy(indexTimeColumn);
    }
    
    public int getIndexColumn(){
        return this.indexTimeColumn;
    }
    
    public double[] shiftColumn(int col,int shift){
        double[][] _data = ArrayOperations.arrayListToDoubleMatrix(data);
        return ArrayOperations.shiftColumn(_data, indexTimeColumn, shift, col);
    }

    public void shift(int col,int shift){
        String colName = columns.get(col);
        if(shift>0)
            colName=colName+"_"+String.valueOf(shift);
        else
            colName=colName+"__"+String.valueOf(-shift);
        addColumn(shiftColumn(col,shift),colName);
    }
    
    public ChartFrame getTimePlot(ChartFrame ref,String title,String[] cols,Paint[] color,double start,double end){
        int[] icols = new int[cols.length];
        for(int i=0;i<cols.length;i++)
            icols[i]=getColumnIndex(cols[i]);
        return getTimePlot(ref,title,icols,color,start,end);
    }
    
    public ChartFrame getTimePlot(String title,String[] cols,Paint[] color,double start,double end){
        int[] icols = new int[cols.length];
        for(int i=0;i<cols.length;i++)
            icols[i]=getColumnIndex(cols[i]);
        return getTimePlot(title,icols,color,start,end);
    }
    
    public ChartFrame getTimePlot(ChartFrame ref,String title,int[] cols,Paint[] color,double start,double end){
        int[] cls;
        if(ArrayOperations.isInIntArray(cols, indexTimeColumn)){
            cls=new int[cols.length-1];
            int ii=0;
            for(int i=0;i<cols.length;i++){
                if(cols[i]!=indexTimeColumn)
                    cls[ii++]=cols[i];
            }
        }
        else{
            cls=new int[cols.length];
            System.arraycopy(cols, 0, cls, 0, cols.length);
        }

        double[][][] seriesdata = new double[cls.length][][];
        String[][] sns = new String[cls.length][1];
        Paint[][] colorv = new Paint[cls.length][1];
        for(int i=0;i<cls.length;i++){
            sns[i][0]=columns.get(cls[i]);
            colorv[i][0]=color[i];
            int[] clls = { indexTimeColumn,cls[i]};
            seriesdata[i]=this.getData(clls,start,end);
        }
        Chart chart = new Chart(title,seriesdata[0],sns[0],0,colorv[0],Chart.SeriesType.LINES);
        for(int i=1;i<cls.length;i++){
            chart.addSeries(seriesdata[i], sns[i], 0, colorv[i], Chart.SeriesType.LINES);
        }
        if(ref==null){
            ChartFrame frame = new ChartFrame(title, chart.scatterPlot(columns.get(indexTimeColumn), "Data"));
            frame.pack();
            return frame;
        }
        else{
            ref.getChartPanel().setChart(chart.scatterPlot(columns.get(indexTimeColumn), "Data"));
            return ref;
        }
    }
    
    public ChartFrame getTimePlot(String title,int[] cols,Paint[] color,double start,double end){
        return getTimePlot(null,title,cols,color,start,end);
    }
    
    public double[][] getData(int[] cols,double start,double end){
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        int ii=0;
        for(int i=0;i<numberOfRecords;i++){
            if((data.get(i).get(indexTimeColumn)>=start) 
                    &&(data.get(i).get(indexTimeColumn)<=end)){
                result.add(new ArrayList<Double>());
                for(int j=0;j<cols.length;j++){
                    double value = data.get(i).get(cols[j]);
                    result.get(ii).add(value);
                }
                ii++;
            }
        }
        return ArrayOperations.arrayListToDoubleMatrix(result);
    }
    
    public NeuralDataSet makeNeuralDataSet(int[] inputColumns,int[] outputColumns){
        int ic = inputColumns.length;
        int nc = ic+outputColumns.length;
        String[] inputColumnNames=new String[inputColumns.length];
        String[] outputColumnNames=new String[outputColumns.length];
        String[] targetColumnNames=new String[outputColumns.length];
        for(int i=0;i<inputColumns.length;i++)
            inputColumnNames[i]=columns.get(inputColumns[i]);
        for(int i=0;i<outputColumns.length;i++){
            outputColumnNames[i]="Neural"+columns.get(outputColumns[i]);
            targetColumnNames[i]=columns.get(outputColumns[i]);
        }
        int[] inpcol = new int[ic];
        int[] outcol = new int[nc-ic];
        for(int i=0;i<ic;i++)
            inpcol[i]=i;
        for(int i=ic;i<nc;i++)
            outcol[i-ic]=i;
        double[][] alldata = this.getData();
        double[][] neuraldata = new double[this.numberOfRecords][nc];
        for(int i=0;i<this.numberOfRecords;i++){
            for(int j=0;j<nc;j++){
                int jj;
                if(j<ic)
                    jj=inputColumns[j];
                else
                    jj=outputColumns[j-ic];
                neuraldata[i][j]=alldata[i][jj];
            }
        }
        NeuralDataSet nn = new NeuralDataSet(neuraldata, inpcol, outcol);        
        nn.inputNames=inputColumnNames;
        nn.targetNames=targetColumnNames;
        nn.outputNames=outputColumnNames;
        return nn;
    }
    
}
