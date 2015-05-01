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
	 Model model;
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
			model = new Model(balance);
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
        	password = Integer.toString(accountPin);
        	sBalance = "0";
        	writer.println("0");
        	writer.close();
        	model = new Model(0);
        }
        catch (IOException e)
        {
        	return false;
        }
    return true;
  }
    
    public double withdraw(int accountNumber, int accountPin, double amount) {
    	double balance = Integer.parseInt(sBalance);
        if (password.equals(Integer.toString(accountPin))) {
        	double x = balance;
        		balance = model.withdrawal(amount);
                //call write
        			if (balance == x)
        			{
        				return -1;
        			}
                if(FileWrite(balance))
                {
                	return balance;
                }
                else
                {
                	return -1;
                }
        }
        else
            return -1.0;
    }
    
boolean FileWrite(double balance){
		
		
		try{
			PrintWriter writer = new PrintWriter(filename , "UTF-8");
			writer.write(password);
			writer.write((int) balance);
			writer.close();
		}
		
		catch(Exception e){
			return false;
		}
		return true;
	}
public double getBalance(int accountNumber, int accountPin) {
    if (password.equals(accountPin))
    {
    	double balance = Integer.parseInt(sBalance);
        return balance;
    }
    else
        return -1.0;
}
public double deposit(int accountNumber, int accountPin, double amount) {
    if (password.equals(Integer.toString(accountPin))) {
    	double balance = Integer.parseInt(sBalance);
        balance = (int) model.deposit(amount);
        if(FileWrite(balance))
        {
        	return balance;
        }
        else
        {
        	return -1;
        }
    }
    else
        return -1.0;
}
	
	public boolean LogWriter(String actNum, double balance){
		
		String LogText = null;
		
		try{
			PrintWriter writer = new PrintWriter(filename , "UTF-8");
			writer.println(LogText); // logText will be created later.
			writer.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}

