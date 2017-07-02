package edu.packt.neuralnet.test;

import java.util.Arrays;

import edu.packt.neuralnet.data.LoadCsv;

/**
*
* LoadCsvTest
* This class solely performs test to load CSV file
* 
* @authors Alan de Souza, Fabio Soares 
* @version 0.1
* 
*/
public class LoadCsvTest {
	
	public static void main(String[] args) {
		double[][] dataMatrix;
		LoadCsv csv = new LoadCsv();
		dataMatrix = csv.getData("data", "inmet_13_14_input.csv");
		System.out.println( Arrays.deepToString(dataMatrix).replaceAll("],", "]\n") );
	}

}
