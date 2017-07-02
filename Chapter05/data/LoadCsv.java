package edu.packt.neuralnet.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/** 
 * This class deals with CSV files 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
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
	
        
        private boolean columnsInFirstRow=false;
        
        private String separator = ",";
        
        private String fullFilePath;
        
        private String[] columnNames;
        
        final double missingValue=Double.NaN;
        
        public LoadCsv(){
            
        }
        
        public LoadCsv(String path,String fileName){
            this.PATH=path;
            this.FILE_NAME=fileName;
        }
        
        public LoadCsv(String fileName,boolean _columnsInFirstRow,String _separator){
            this.fullFilePath=fileName;
            this.columnsInFirstRow=_columnsInFirstRow;
            this.separator=_separator;
        }
        
	/**
	 * Gets data matrix
	 * @param path
	 * @param fileName
	 * @return Java Matrix
	 */
	public double[][] getData(String path, String fileName){
            return getData(path,fileName,false);
	}
        
        public double[][] getData(String path,String fileName,boolean _columnsInFirstRow){
            return getData(path,fileName,_columnsInFirstRow,",");
        }
        
        public double[][] getData(String path,String fileName,boolean _columnsInFirstRow,String _separator){
            this.PATH = path;
            this.FILE_NAME=fileName;
            this.columnsInFirstRow=_columnsInFirstRow;
            this.separator=_separator;
            try {
		dataMatrix = csvData2Matrix();
		System.out.println("File loaded!");
            } catch (Exception e) {
		System.err.println("Error while loading CSV file. Details: " + e.getMessage());
            }
            return dataMatrix;
        }
        
        public static double[][] getData(String fullPath,boolean _columnsInFirstRow,String _separator){
            LoadCsv lcsv = new LoadCsv(fullPath,_columnsInFirstRow,_separator);
            lcsv.columnsInFirstRow=_columnsInFirstRow;
            lcsv.separator=_separator;
            try{
                lcsv.dataMatrix=lcsv.csvData2Matrix(fullPath);
                System.out.println("File "+fullPath+" loaded!");
            }
            catch(IOException ioe){
                System.err.println("Error while loading CSV file. Details: " + ioe.getMessage());
            }
            return lcsv.dataMatrix;
        }
        
        public double[][] getDataMatrix(String fullPath,boolean _columnsInFirstRow,String _separator){
            this.columnsInFirstRow=_columnsInFirstRow;
            this.separator=_separator;
            try{
                this.dataMatrix=csvData2Matrix(fullPath);
                System.out.println("File "+fullPath+" loaded!");
            }
            catch(IOException ioe){
                System.err.println("Error while loading CSV file. Details: "+ioe.getMessage());
            }
            return this.dataMatrix;
        }
        
        public static DataSet getDataSet(String path,String fileName,boolean _columnsInFirstRow,String _separator){
            LoadCsv lcsv = new LoadCsv(path, fileName);
            try{
                String fullPath=lcsv.defineAbsoluteFilePath();
                return LoadCsv.getDataSet(fullPath, _columnsInFirstRow, _separator);
            }
            catch(IOException ioe){
                return null;
            }
        }
        
        public static DataSet getDataSet(String fullPath,boolean _columnsInFirstRow,String _separator){
            LoadCsv lcsv = new LoadCsv(fullPath,_columnsInFirstRow,_separator);
            lcsv.columnsInFirstRow=_columnsInFirstRow;
            lcsv.separator=_separator;
            try{
                lcsv.dataMatrix=lcsv.csvData2Matrix(fullPath);
                System.out.println("File "+fullPath+" loaded!");
            }
            catch(IOException ioe){
                System.err.println("Error while loading CSV file. Details: " + ioe.getMessage());
            }
            return new DataSet(lcsv.dataMatrix, lcsv.columnNames);
            
        }
        
	/**
	 * Loads CSV file into Java matrix
	 * @return Java matrix
	 * @throws IOException
	 */
	private double[][] csvData2Matrix() throws IOException {

		String fullPath = defineAbsoluteFilePath();

		return csvData2Matrix(fullPath);

	}

	private double[][] csvData2Matrix(String fullPath) throws IOException {

            BufferedReader buffer = new BufferedReader(new FileReader(fullPath));
		
            try {
                StringBuilder builder = new StringBuilder();
			
                String line = buffer.readLine();
			
                int columns = line.split(this.separator).length;
                columnNames=new String[columns];
                int rows = 0; 
                while (line != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                    line = buffer.readLine();
                    rows++;
                }
		boolean cr = this.columnsInFirstRow;
                double[][] matrix = new double[cr?rows-1:rows][columns];
                String everything = builder.toString();
			
                Scanner scan = new Scanner( everything );
                rows = 0;
                
                while(scan.hasNextLine()){
                    String[] strVector = scan.nextLine().split(this.separator,-1);
                    if(cr && rows==0){
                        System.arraycopy(strVector, 0, columnNames, 0, strVector.length);
                    }
                    else{
                        for (int i = 0; i < strVector.length; i++) {
                            try{
                                matrix[cr?rows-1:rows][i] = Double.parseDouble(strVector[i]);
                            }
                            catch(Exception e){
                                matrix[cr?rows-1:rows][i] = this.missingValue;
                            }
                        }
                    }
                    rows++;
                }
		scan.close();
			
		return matrix;

            } finally {
		buffer.close();
            }

	}
        
        public void save() throws IOException{
            if(fullFilePath==null)
                fullFilePath=defineAbsoluteFilePath();
            try(FileWriter w = new FileWriter(fullFilePath)) {
                StringBuilder sb = new StringBuilder();
                if(columnsInFirstRow){
                    for(int i=0;i<columnNames.length;i++){
                        if(i>0)
                            sb.append(separator);
                        sb.append(columnNames[i]);
                    }
                    sb.append(System.lineSeparator());
                }
                for(int i=0;i<dataMatrix.length;i++){
                    for(int j=0;j<dataMatrix[i].length;j++){
                        if(j>0)
                            sb.append(separator);
                        try{
                            if(!Double.isNaN(dataMatrix[i][j]))
                                sb.append(String.valueOf(dataMatrix[i][j]));
                            else
                                sb.append("");
                        }
                        catch(Exception e){
                            sb.append("");
                        }
                    }
                    if(i<dataMatrix.length-1)
                        sb.append(System.lineSeparator());
                }
                w.append(sb.toString());
                w.flush();
                w.close();
            }
            
        }
        
        public static void dataMatrix2csv(double[][] data,String[] columnNames,String path,String filename,String separator){
            LoadCsv lcsv = new LoadCsv(path, filename);
            try{
                lcsv.fullFilePath=lcsv.defineAbsoluteFilePath();
                lcsv.dataMatrix=data;
                lcsv.columnsInFirstRow=true;
                lcsv.columnNames=columnNames;
                lcsv.separator=separator;
                lcsv.save();
                System.out.println("File "+filename+" saved succesfully!");
            }
            catch(IOException ioe){
                System.err.println("Error while saving "+filename+":"+ioe.getMessage());
            }
        }
        
        public static void dataMatrix2csv(double[][] data,String[] columnNames,String filename,String separator){
            LoadCsv lcsv = new LoadCsv(filename, true, separator);
            lcsv.dataMatrix=data;
            lcsv.columnNames=columnNames;
            try{
                lcsv.save();
                System.out.println("File "+filename+" saved succesfully!");
            }
            catch(IOException ioe){
                System.err.println("Error while saving "+filename+":"+ioe.getMessage());
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
			System.err.println("File not found...");
		}

		return absoluteFilePath;

	}
        
        public String[] getColumnNames(){
            return columnNames;
        }

}
