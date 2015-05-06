import java.awt.geom.Arc2D.Double;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.imageio.IIOException;
public class Control {
	 String password;
	 String sBalance;
	 String filename;
	public  void main(String Args[])
	{

	}
	
	
	public boolean findAccount(int accountNumber)
	{
		//call for name
		String name = Integer.toString(accountNumber);
		BufferedReader br = null;
		 if (true)
		 {
		try {
			name += ".txt";
			filename = name;
			double balance = 0;
			
			//br = new BufferedReader(new FileReader("src/database.txt"));
			br = new BufferedReader(new FileReader("database.txt"));
			password = br.readLine();
			sBalance = br.readLine();
			//call for pass from user
			String input = "";
			balance = Integer.parseInt(sBalance);
			while (password.equals(input))
			{
				//get new input
			}
			}
		 catch (IOException e) {
			 return false;
			//account needs making
			
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
		 return true;
	}
	public boolean trypin(int pin)
	{
		if (password.equals(pin))
		return true;
		return false;
	}
    public boolean createAccount(int accountNumber, int accountPin) {
        try {
        	String filename = Integer.toString(accountNumber);
        	filename += ".txt";
        	PrintWriter writer = new PrintWriter(filename, "UTF-8");
        	writer.println(Integer.toString(accountPin));
        	writer.println("0");
        	writer.close();
        }
        catch (IOException e)
        {
        	return false;
        }
    return true;
  }
}
