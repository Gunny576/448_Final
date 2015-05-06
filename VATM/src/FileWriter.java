import java.awt.geom.Arc2D.Double;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class FileWriter {
	
	String fileName = null;
	
	public boolean FileWrite(String actPin, double balance){
		
		
		try{
			PrintWriter writer = new PrintWriter(fileName , "UTF-8");
			writer.write(actPin);
			writer.write((int) balance);
			writer.close();
		}
		
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	public boolean LogWriter(String actNum, double balance){
		
		String LogText = null;
		
		try{
			PrintWriter writer = new PrintWriter(fileName , "UTF-8");
			writer.println(LogText); // logText will be created later.
			writer.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}


