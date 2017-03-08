package edu.packt.neuralnet.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/** 
 * This abstract class reads a CSV file and loads data into Java matrix 
 * @author Alan de Souza, Fábio Soares
 *
 */
public class LoadCsv {
	
	/**
	 * CSV file path 
	 */
	private String PATH;
	/**
	 * CSV file name 
	 */
	private String FILE_NAME;
	/**
	 * Data matrix to store values from CSV file 
	 */
	private double[][] dataMatrix;
	
	/**
	 * Gets data matrix
	 * @param path
	 * @param fileName
	 * @return Java Matrix
	 */
	public double[][] getData(String path, String fileName){
		this.PATH = path;
		this.FILE_NAME = fileName;
		try {
			dataMatrix = csvData2Matrix();
			System.out.println("File loaded!");
		} catch (Exception e) {
			System.err.println("Error when load CSV data. Details: " + e.getMessage());
		}
		return dataMatrix;
	}
	
	/**
	 * Loads CSV file into Java matrix
	 * @return Java matrix
	 * @throws IOException
	 */
	private double[][] csvData2Matrix() throws IOException {

		String fullPath = defineAbsoluteFilePath();

		BufferedReader buffer = new BufferedReader(new FileReader(fullPath));
		
		try {
			StringBuilder builder = new StringBuilder();
			
			String line = buffer.readLine();
			
			int columns = line.split(",").length;
			int rows = 0; 
			while (line != null) {
				builder.append(line);
				builder.append(System.lineSeparator());
				line = buffer.readLine();
				rows++;
			}
			
			double[][] matrix = new double[rows][columns];
			String everything = builder.toString();
			
			Scanner scan = new Scanner( everything );
			rows = 0;
			while(scan.hasNextLine()){
				String[] strVector = scan.nextLine().split(",");
				for (int i = 0; i < strVector.length; i++) {
					matrix[rows][i] = Double.parseDouble(strVector[i]);
				}
				rows++;
			}
			scan.close();
			
			return matrix;

		} finally {
			buffer.close();
		}

	}
	
	/**
	 * Defines absolute file path according user folder and operation system 
	 * @return absolute path
	 * @throws IOException
	 */
	private String defineAbsoluteFilePath() throws IOException {

		String absoluteFilePath = "";

		String workingDir = System.getProperty("user.dir");

		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0) {
			absoluteFilePath = workingDir + "\\" + PATH + "\\" + FILE_NAME;
		} else {
			absoluteFilePath = workingDir + "/" + PATH + "/" + FILE_NAME;
		}

		File file = new File(absoluteFilePath);

		if (file.exists()) {
			System.out.println("File found!");
			System.out.println(absoluteFilePath);
		} else {
			System.err.println("File did not find...");
		}

		return absoluteFilePath;

	}

}
