package edu.packt.neuralnet.examples.weather;

import java.util.regex.Pattern;

public class WeatherMain {

	public static void main(String[] args) {
		new Thread(new ParameterWindow()).start();
	}
	
	
	
}

class WeatherChartsWindow implements Runnable {

	public String dataSet;
	public int numberNeuronsHdnLayer;
	public double learningRate;
	public String dataNormType;
	public int maxEpochs;
	public double momentumRate;
	public double minOverallError;
	public boolean playSound;

	public WeatherChartsWindow(String dataSet, int numberNeuronsHdnLayer, double learningRate, String dataNormType, int maxEpochs, double momentumRate, double minOverallError, boolean playSound) {
		this.dataSet = dataSet;
		this.numberNeuronsHdnLayer = numberNeuronsHdnLayer;
		this.learningRate = learningRate;
		this.dataNormType = dataNormType.split(Pattern.quote(")"))[0];
		this.maxEpochs = maxEpochs;
		this.momentumRate = momentumRate;
		this.minOverallError = minOverallError;
		this.playSound = playSound;
	}

	public void run(){
		WeatherExample w = new WeatherExample();
		w.train(dataSet, numberNeuronsHdnLayer, learningRate, dataNormType, maxEpochs, momentumRate, minOverallError, playSound);
	}
	
	
	
	
}

class ParameterWindow implements Runnable {

	public void run(){
		WeatherGUI wGUI = new WeatherGUI();
		wGUI.setupAndShowGUI();
	}
	
}