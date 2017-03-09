package edu.packt.neuralnet.data;

import java.util.Arrays;

import edu.packt.neuralnet.data.DataNormalization.NormalizationTypes;

public class DataNormalizationTest {

    public static void main(String[] args) {
		
		double[][] data = { {2.0,28.0,5.9}, {4.0,18.0,9.5}, {2.0,49.0,6.5}, {20.0,18.0,-3.5},
						    {1.0,21.0,-4.5}, {3.0,22.0,7.5}, {6.0,19.0,5.5}, {2.0,34.0,12.5}};
		
		//DataNormalization norm = new DataNormalization();
		
		double[][] dataNormalized = new double[data.length][data[0].length];
		double[][] dataDenormalized = new double[data.length][data[0].length];
		
		DataNormalization dn = new DataNormalization();
		
		dn.TYPE = NormalizationTypes.MINUSONE_PLUSONE;
		dataNormalized = dn.normalize(data);
		dataDenormalized = dn.denormalize(data, dataNormalized);
		
		//dataNormalized = norm.customNormalize(data, -1.5, 1.5);

		System.out.println( Arrays.deepToString(dataNormalized).replaceAll("],", "]\n") );
		System.out.println("##########################################");
		System.out.println( Arrays.deepToString(dataDenormalized).replaceAll("],", "]\n") );
		
	}
	
}
