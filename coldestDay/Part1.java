
/**
 * Write a description of Part1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class Part1 {
    
    public CSVRecord getLowestTempOfTwo (CSVRecord currentRow, CSVRecord lowestSoFar) {
        //get lowest number of two when they are within reasonable range to avoid bug
        if (lowestSoFar == null) {
            lowestSoFar = currentRow;
        }
        else {
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            double lowestTemp = Double.parseDouble(lowestSoFar.get("TemperatureF"));
            if (currentTemp > -100 && currentTemp < lowestTemp) {
                lowestSoFar = currentRow;
            }
        }
        return lowestSoFar;
    }
    
    public CSVRecord coldestHourInFile(CSVParser parser) {
        CSVRecord lowestSoFar = null;
        for (CSVRecord currentRow : parser) {
            lowestSoFar = getLowestTempOfTwo(currentRow, lowestSoFar);
        }
        return lowestSoFar;
    }
    
    public void testColdestHourInFile() {
        FileResource fr = new FileResource();
        CSVRecord lowest = coldestHourInFile(fr.getCSVParser());
        System.out.println("lowest temperature was " + lowest.get("TemperatureF")
                           + " at " + " DateUTC " + lowest.get ("DateUTC"));
    }
    
    public String fileWithColdestTemperature() {
        CSVRecord lowestSoFar = null;
        String FilelowestTemp = "";
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
            lowestSoFar = getLowestTempOfTwo(currentRow, lowestSoFar);
            if (currentRow == lowestSoFar) {    
                FilelowestTemp = f.getName();
            }
        }
        return FilelowestTemp;
    }
    
    public void testFileWithColdestTemperature() {
        String fileLowest = fileWithColdestTemperature();
        String fileLocation = "nc_weather/2013/"+fileLowest;
        FileResource fr = new FileResource(fileLocation);
        CSVParser parser = fr.getCSVParser();
        CSVRecord lowest = coldestHourInFile(fr.getCSVParser());
        double lowestTemp = Double.parseDouble(lowest.get("TemperatureF"));
        System.out.println("Coldest day was in file " + fileLowest);
        System.out.println("Coldest temperature on that day was " + lowestTemp);
        System.out.println("All the Temperatures on the coldest day were: ");
        for (CSVRecord currentRow : parser) {
            double TemperatureF = Double.parseDouble(currentRow.get("TemperatureF"));
            String DateUTC = currentRow.get("DateUTC");
            System.out.println(DateUTC + " " + TemperatureF);
        }
    }
    
    public CSVRecord getLowestHumidityOfTwo (CSVRecord currentRow, CSVRecord lowestSoFar) {
        //get lowest number of two when they are within reasonable range to avoid bug
        if (lowestSoFar == null) {
            lowestSoFar = currentRow;
        }
        else {
            String curHumidity = currentRow.get("Humidity");
            int currentHumidity = 0;
            int lowestHumidity = Integer.parseInt(lowestSoFar.get("Humidity"));
            if (curHumidity.equals("N/A")) {
                currentHumidity = 101;
            } else {
                currentHumidity = Integer.parseInt(currentRow.get("Humidity"));
            }
            if (currentHumidity < lowestHumidity) {
                lowestSoFar = currentRow;
            }
        }
        return lowestSoFar;
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser) {
        CSVRecord lowestSoFar = null;
        for (CSVRecord currentRow : parser) {
            lowestSoFar = getLowestHumidityOfTwo(currentRow, lowestSoFar);
        }
        return lowestSoFar;
    }
    
    public void testLowestHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csvLowest = lowestHumidityInFile(parser);
        int lowestHumidity = Integer.parseInt(csvLowest.get("Humidity"));
        String DateUTC = csvLowest.get("DateUTC");
        System.out.println("Lowest Humidity was " + lowestHumidity + " at " + DateUTC);
    }
    
    public CSVRecord lowestHumidityInManyFiles() {
        CSVRecord lowestSoFar = null;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
            lowestSoFar = getLowestHumidityOfTwo(currentRow, lowestSoFar);
        }
        return lowestSoFar;
    }
    
    public void testLowestHumidityInManyFiles() {
        CSVRecord lowest = lowestHumidityInManyFiles();
        System.out.println("Lowest Humidity was " + lowest.get("Humidity") 
                           + " at " + lowest.get("DateUTC"));
    }
    
    public double averageTemperatureInFile(CSVParser parser) {
        double averageTemp = 0;
        double totalTemp = 0;
        int numOfTemp = 0;
        for (CSVRecord currentRow : parser) {
           double currentTemp = Double.parseDouble(currentRow.get("TemperatureF")); 
           totalTemp += currentTemp;
           numOfTemp += 1;
        }
        averageTemp = totalTemp/numOfTemp;
        return averageTemp;
    }
    
    public void testAverageTemperatureInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();        
        double averageTemp = averageTemperatureInFile(parser);
        System.out.println("Average temperature in file is " + averageTemp);
    }
    
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value) {
        double averageTemp = 0;
        double totalTemp = 0;
        int numOfTemp = 0;
        for (CSVRecord currentRow : parser) { 
           double currentTemp = Double.parseDouble(currentRow.get("TemperatureF")); 
           double currentHumidity = Double.parseDouble(currentRow.get("Humidity"));
           if (currentHumidity >= value) {
             totalTemp += currentTemp;
             numOfTemp += 1;
           }
        }
        averageTemp = totalTemp/numOfTemp;
        return averageTemp;
    }    
    
    public void testAverageTemperatureWithHighHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();        
        double averageTemp = averageTemperatureWithHighHumidityInFile(parser, 80);
        System.out.println("Average temperature with high Humidity is " + averageTemp);
    }    
}
