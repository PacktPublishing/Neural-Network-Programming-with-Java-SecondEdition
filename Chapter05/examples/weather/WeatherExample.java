package edu.packt.neuralnet.examples.weather;

import java.awt.Color;
import java.awt.Paint;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFrame;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.DataNormalization;
import edu.packt.neuralnet.data.LoadCsv;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.data.TimeSeries;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.HyperTan;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;
import edu.packt.neuralnet.misc.SoundUtils;

/**
*
* WeatherExample
* This class performs the use case described in chapter 5 of the book. Data are loaded,
* data normalization is done, neural net is created using parameters defined, Backpropagation
* algorithm is used to make neural net learn and charts are plotted  
* 
* @authors Alan de Souza, Fábio Soares 
* @version 0.1
* 
*/
public class WeatherExample {

    TimeSeries timeSeriesWeather;
//    TimeSeries picos;
//    TimeSeries camposdojordao;
//    TimeSeries portoalegre;
    
    NeuralNet nnWeather;
//    NeuralNet nnpicos;
//    NeuralNet nncamposjordao;
//    NeuralNet nnportoalegre;

    NeuralDataSet trainDataWeather;
    NeuralDataSet testDataWeather;    
//    NeuralDataSet trainDataP;
//    NeuralDataSet testDataP;    
//    NeuralDataSet trainDataCJ;
//    NeuralDataSet testDataCJ;    
//    NeuralDataSet trainDataPOA;
//    NeuralDataSet testDataPOA;
    
    NeuralDataSet fullDataWeather;
//    NeuralDataSet fullDataP;
//    NeuralDataSet fullDataCJ;
//    NeuralDataSet fullDataPOA;
    
    //latitude, longitude and altitude
    double[][] coord = { 
        {-7.6 , -72.8 , 170.0 } // Cruzeiro do Sul
       ,{-7.1 , -41.5 , 208.0 } // Picos
       ,{-22.75, -45.6 , 1642.0} // Campos do Jordao
       ,{-30.0,-51.2 , 48.0}  // Porto Alegre
    };
    
    public void train(String dataSet, int numberNeuronsHdnLayer, double learningRate, String dataNormType, int maxEpochs, double momentumRate, double minOverallError, boolean playSound) {
    	RandomNumberGenerator.r = null;
		
    	RandomNumberGenerator.setSeed( 7 );
		//RandomNumberGenerator.setSeed( System.currentTimeMillis() ); //seed dynamic
        
        WeatherExample we = new WeatherExample();
        
        //load weather data
        //"Cruzeiro do Sul", "Picos", "Campos do Jordao", "Porto Alegre"
        switch (dataSet) {
			case "Cruzeiro do Sul":
				try{
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data","cruzeirodosul2010daily_delays_clean.txt",true,";"));
		        }
		        catch(Exception e){
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data", "cruzeirodosul2010daily.txt", true, ";"));
		            we.timeSeriesWeather.setIndexColumn(0);
		            we.addSolarNoonAngle(we.timeSeriesWeather,we.coord[0][0]);
		            we.makeDelays(we.timeSeriesWeather, 3);
		            we.timeSeriesWeather.dropNaN();
		            we.timeSeriesWeather.save("data","cruzeirodosul2010daily_delays_clean.txt",";");
		        }
				break;
			case "Picos":
				try{
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data","picos2010daily_delays_clean.txt",true,";"));
		        }
		        catch(Exception e){
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data", "picos2010daily.txt", true, ";"));
		            we.timeSeriesWeather.setIndexColumn(0);
		            we.addSolarNoonAngle(we.timeSeriesWeather,we.coord[1][0]);
		            we.makeDelays(we.timeSeriesWeather, 3);
		            we.timeSeriesWeather.dropNaN();
		            we.timeSeriesWeather.save("data","picos2010daily_delays_clean.txt",";");
		        }
				break;
			case "Campos do Jordao":
				try{
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data","camposdojordao2010daily_delays_clean.txt",true,";"));
		        }
		        catch(Exception e){
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data", "camposjordao2010daily.txt", true, ";"));
		            we.timeSeriesWeather.setIndexColumn(0);
		            we.addSolarNoonAngle(we.timeSeriesWeather,we.coord[2][0]);
		            we.makeDelays(we.timeSeriesWeather, 3);
		            we.timeSeriesWeather.dropNaN();
		            we.timeSeriesWeather.save("data","camposdojordao2010daily_delays_clean.txt",";");
		        }
				break;	
			case "Porto Alegre":
				try{
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data", "portoalegre2010daily_delays_clean.txt", true, ";"));
		        }
		        catch(Exception e){
		            we.timeSeriesWeather = new TimeSeries(LoadCsv.getDataSet("data", "portoalegre2010daily.txt", true, ";"));
		            we.timeSeriesWeather.setIndexColumn(0);
		            we.addSolarNoonAngle(we.timeSeriesWeather,we.coord[3][0]);
		            we.makeDelays(we.timeSeriesWeather, 3);
		            we.timeSeriesWeather.dropNaN();
		            we.timeSeriesWeather.save("data","portoalegre2010daily_delays_clean.txt",";");
		        }
				break;
			default:
				showNoMatchingDatasetError(dataSet);
				System.exit(0);
		}
        
        //correlation
        //we.correlationAnalysis(0.35,false);
        
        //load weather data:
        we.createNNs( numberNeuronsHdnLayer, dataNormType, dataSet );
        
        //setup backpropagation learning algorithm parameters
        Backpropagation backprog = new Backpropagation(
        		 we.nnWeather
                , we.trainDataWeather
                , LearningAlgorithm.LearningMode.BATCH );
        
        backprog.setTestingDataSet(we.testDataWeather);
        backprog.setLearningRate( learningRate );
        backprog.setMaxEpochs( maxEpochs );
        backprog.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        backprog.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        backprog.setMinOverallError( minOverallError );
        backprog.printTraining = true;
        backprog.setMomentumRate( momentumRate );
        backprog.showPlotError=true;
        
        //Initial view
        we.fullDataWeather.simulate();
        String[] neuralOutputs = { "NeuralMaxTemp", "NeuralMinTemp"};
            
        we.timeSeriesWeather.addColumn(we.fullDataWeather.getIthNeuralOutput(0), neuralOutputs[0]);
        we.timeSeriesWeather.addColumn(we.fullDataWeather.getIthNeuralOutput(1), neuralOutputs[1]);
        
        String[] comparison = {"MaxTemp","NeuralMaxTemp"//,"MaxTemp","NeuralMaxTemp"
        };
        Paint[] comp_color = {Color.BLUE, Color.RED//, Color.GREEN, Color.BLACK
        };
        
        final double minDate = 41000.0;
        final double maxDate = 41100.0;
        
        ChartFrame viewChart = we.timeSeriesWeather.getTimePlot("Comparison", comparison, comp_color, minDate, maxDate);
        viewChart.setVisible(true);
        
        backprog.setFittingEvolution(we.timeSeriesWeather, we.fullDataWeather, 0, new int[]{0}, new double[][]{{minDate,maxDate}}, viewChart);
        backprog.showFittingPlot=true;
        try{
            backprog.forward();
            //neuralDataSet.printNeuralOutput();
            
            backprog.train();
            System.out.println("End of training");
            if(backprog.getMinOverallError()>=backprog.getOverallGeneralError()){
                System.out.println("Training successful!");
            }
            else{
                System.out.println("Training was unsuccessful");
            }
            
            //print some results
            System.out.println("Overall Error:"      + String.valueOf(backprog.getOverallGeneralError()));
            System.out.println("Testing Error:"      + String.valueOf(backprog.getTestingOverallGeneralError()));
            System.out.println("Min Overall Error:"  + String.valueOf(backprog.getMinOverallError()));
            System.out.println("Epochs of training:" + String.valueOf(backprog.getEpoch()));
            
            //plot list of errors by epoch 
            backprog.showErrorEvolution();
            
            we.fullDataWeather.simulate();
            
            we.timeSeriesWeather.setColumnValues(we.fullDataWeather.getIthNeuralOutput(0), neuralOutputs[0]);
            we.timeSeriesWeather.setColumnValues(we.fullDataWeather.getIthNeuralOutput(1), neuralOutputs[1]);
            
            ChartFrame scatter = we.timeSeriesWeather.getScatterChart("Scatter Plot", comparison[0], comparison[1], comp_color[0]);
            scatter.setVisible(true);
            
//            viewChart=we.cruzeirodosul.getTimePlot(viewChart,"Comparison", comparison, comp_color, 41300, 41400);
//            viewChart.setVisible(true);
//            System.out.println("Target Outputs:");
//            neuralDataSet.printTargetOutput();
            
        }
        catch(NeuralException ne){
            
        }
        
        if ( playSound ) {
	        try {
				SoundUtils.tone(350, 2000);
			} catch (LineUnavailableException e) {
				JOptionPane.showMessageDialog(null, 
						"Error playing ending sound! Details: " + e.getMessage(),
						"Error!",
						JOptionPane.ERROR_MESSAGE);
			}
        }
		
    }

	public double calcSolarNoonAngle(double date,double latitude){
        return 90-Math.abs(-23.44*Math.cos((2*Math.PI/365.25)*(date+8.5))-latitude);
    }
    
    public void addSolarNoonAngle(TimeSeries ts,double latitude){
        double[] sna = new double[ts.numberOfRecords];
        for(int i=0;i<ts.numberOfRecords;i++){
            sna[i]=calcSolarNoonAngle(ts.data.get(i).get(ts.getIndexColumn()),latitude);
        }
        ts.addColumn(sna, "NoonAngle");
    }

    public void makeDelays(TimeSeries ts,int maxdelays){
        int nc=ts.numberOfColumns;
        for(int i=0;i<nc;i++){
            if(i!=ts.getIndexColumn())
                for(int j=1;j<=maxdelays;j++)
                    ts.shift(i, -j);
        }
    }
    
    /*
    public void correlationAnalysis(double minAbsCorr,boolean plot){
        //indexes of output variables (max. and min. temperature) 
        int[][] outputs = { 
            {2,3}, //cruzeiro do sul
            {2,3}, //picos
            {2,3}, //campos do jordao
            {2,3}}; //porto alegre
        int[][] potentialInputs = { //indexes of input variables (delayed)
            {10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,38,39,40}, //cruzeiro do sul
            {10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,38,39,40}, //picos
            {8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,30,31,32}, //campos do jordao
            {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36} // portoalegre
        };
        ArrayList<ArrayList<ArrayList<Integer>>> chosenInputs = new ArrayList<>();
        TimeSeries[] tscollect = {this.timeSeriesWeather,this.picos,this.camposdojordao,this.portoalegre};
        String[] names = {"Cruzeiro do Sul","Picos","Campos do Jordão","Porto Alegre"};
        double[][][] correlation = new double[4][][];
        for(int i=0;i<4;i++){
            chosenInputs.add(new ArrayList<ArrayList<Integer>>());
            correlation[i]=new double[outputs[i].length][potentialInputs[i].length];
            System.out.println("\nCorrelation Analysis for data from "+names[i]);
            for(int j=0;j<outputs[i].length;j++){
                System.out.println("\tCorrelations with the output Variable:"+tscollect[i].columns.get(outputs[i][j]));
                chosenInputs.get(i).add(new ArrayList<Integer>());
                for(int k=0;k<potentialInputs[i].length;k++){
                    correlation[i][j][k]=tscollect[i].correlation(outputs[i][j], potentialInputs[i][k]);
                    System.out.println("\t"+tscollect[i].columns.get(potentialInputs[i][k])+":"+String.valueOf(correlation[i][j][k]));
                    if(Math.abs(correlation[i][j][k])>minAbsCorr){
                        chosenInputs.get(i).get(j).add(potentialInputs[i][k]);
                        if(plot)
                            tscollect[i].getScatterChart(names[i]+" - Correlation "
                                    +String.valueOf(correlation[i][j][k])
                                    , outputs[i][j]
                                    , potentialInputs[i][k]
                                    , Color.BLACK).setVisible(true);
                    }
                }
            }
        }
    }
    */
    
    public void createNNs(int numberNeuronsHdnLayer, String dataNormType, String dataSet){
        //cruzeiro do sul
        int[] inputColumns = null;
        int[] outputColumns = null;
        NeuralDataSet[] nntt = null;
        
        //setup neural net parameters:
        //"Cruzeiro do Sul", "Picos", "Campos do Jordao", "Porto Alegre"
        switch (dataSet) {
		case "Cruzeiro do Sul":
			inputColumns = new int[] {10,14,17,18,19,20,26,27,29,38,39,40};
			outputColumns = new int[] {2,3};
			
			break;
		case "Picos":
			inputColumns = new int[] {10,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29,30,31,38,39,40};
			outputColumns = new int[] {2,3};
	        
			break;
		case "Campos do Jordao":
			inputColumns = new int[] {8,12,13,14,15,16,17,18,21,22,23,24,30,31,32};
			outputColumns = new int[] {2,3};
			
			break;
		case "Porto Alegre":
			inputColumns = new int[] {9,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,34,35,36};
			outputColumns = new int[] {2,3};
	        
			break;
		default:
			showNoMatchingDatasetError(dataSet);
			System.exit(0);
		}
        
        //this static method hashes the dataset
        fullDataWeather = this.timeSeriesWeather.makeNeuralDataSet(inputColumns, outputColumns);
        nntt = NeuralDataSet.randomSeparateTrainTest(this.timeSeriesWeather, inputColumns, outputColumns, 0.7);
        
        defineDataNormByGUI(dataNormType, nntt);
        
        this.trainDataWeather = nntt[0];
        this.testDataWeather = nntt[1];
        this.fullDataWeather.setNormalizationFrom(trainDataWeather);
		
		this.nnWeather = new NeuralNet( inputColumns.length, outputColumns.length, new int[]{ numberNeuronsHdnLayer } 
            , new IActivationFunction[] {new Sigmoid(1.0)}
            , new HyperTan()
            , new UniformInitialization(-1.0, 1.0) );
		
		this.fullDataWeather.neuralNet=this.nnWeather;
        
    }
    
    private void defineDataNormByGUI(String dataNormType, NeuralDataSet[] nntt){
    	//{ "1) MIN_MAX [-1.0, 1.0]", "2) MIN_MAX [0.0, 1.0]", "3) Z-SCORE [1.0]", "4) Z-SCORE [0.5]" };
		switch (dataNormType) {
			case "1":
				DataNormalization.setNormalization(nntt, -1.0, 1.0);
				break;
			case "2":
				DataNormalization.setNormalization(nntt, 0.0, 1.0);
				break;
			case "3":
				DataNormalization.setNormalization(nntt, 1.0);
				break;
			case "4":
				DataNormalization.setNormalization(nntt, 0.5);
				break;
			default:
		}
    }
    
    private void showNoMatchingDatasetError(String dataSet) {
    	JOptionPane.showMessageDialog(null, 
				"Error! Details: There is no case matching with " + dataSet + ".",
				"Error!",
				JOptionPane.ERROR_MESSAGE);
	}
    
}
