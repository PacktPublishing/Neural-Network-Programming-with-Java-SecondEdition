package edu.packt.neuralnet.data;

import java.util.Arrays;

public class LoadCsvTest {
	
	public static void main(String[] args) {
		double[][] dataMatrix;
		LoadCsv csv = new LoadCsv();
		dataMatrix = csv.getData("data", "inmet_13_14_input.csv");
		System.out.println( Arrays.deepToString(dataMatrix).replaceAll("],", "]\n") );
	}

}
