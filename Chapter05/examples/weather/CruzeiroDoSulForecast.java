/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.examples.weather;

import edu.packt.neuralnet.data.LoadCsv;
import edu.packt.neuralnet.data.TimeSeries;

/**
 *
 * @author fab
 */
public class CruzeiroDoSulForecast extends WeatherExample {
    
    public static void main(String[] args){
        TimeSeries dataCS = new TimeSeries(LoadCsv.getDataSet("data", "cruzeirodosul2010daily.txt", true, ";"));
        
    }
   
}
