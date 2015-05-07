import java.awt.geom.Arc2D.Double;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.Timestamp;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import javax.imageio.IIOException;
public class Control {
//global variables
     String password;
     int accn;
	 String sBalance;
	 String filename;
	 Model model;
	
	//checks database for an account by the name given, throws a not found exception, returning false
	//should the account not exist yet.
	public boolean findAccount(int accountNumber)
	{
		accn = accountNumber;
		//call for name
		String name = Integer.toString(accountNumber);
		BufferedReader br = null;
		 if (true)
		 {
		try {
			name += ".txt";
			filename = name;
			double balance = 0;
			br = new BufferedReader(new FileReader(name));
			password = br.readLine();
			sBalance = br.readLine();
			balance = java.lang.Double.parseDouble(sBalance);
			//creates a new model object giving it the new balance
			model = new Model(balance);
			//calls log function
			LogWriter("Login", accountNumber, balance, 0);
			//closes file reader
			br.close();
			}
		 catch (IOException e) {
			 return false;
			//account doesn't exist
			
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
	public boolean tryPin(int account, int pin)
	{
		if (Integer.parseInt(password) == pin)
		    return true;
		return false;
	}
	//creates a file to simulate a bank database
    public boolean createAccount(int accountNumber, int accountPin) {
        try {
        	String filename = Integer.toString(accountNumber);
			//appends .txt to the account number, which is the name of the file in the database
        	filename += ".txt";
        	PrintWriter writer = new PrintWriter(filename, "UTF-8");
        	writer.println(Integer.toString(accountPin));
        	password = Integer.toString(accountPin);
			//writes the file with the format
			//pin
			//balance
        	sBalance = "0.0";
        	writer.println("0.0");
        	writer.close();
        	model = new Model(0.0);
        }
        catch (IOException e)
        {
        	return false;
        }
    return true;
  }
    
    public double withdraw(int accountNumber, int accountPin, double amount) {
    	double balance = java.lang.Double.parseDouble(sBalance);
        if (password.equals(Integer.toString(accountPin))) {
		//calls model to get the new balance
    		balance = model.withdrawal(amount);
			if (balance < 0)
			{
				return -1;
			}
			//updates the file with the new amount
			else if(FileWrite(balance))
            {
            	sBalance = java.lang.Double.toString(balance);
				//update the log
            	LogWriter("Withdrawal", accountNumber, balance, amount);
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
			writer.println(password);
			writer.println(String.format("%.2f", balance));
			writer.close();
		}
		
		catch(Exception e){
			return false;
		}
		return true;
	}
public double getBalance(int accountNumber, int accountPin) {
    if (Integer.parseInt(password) == accountPin)
    {
    	double balance = java.lang.Double.parseDouble(sBalance);
        return balance;
    }
    else
        return -1.0;
}
public double deposit(int accountNumber, int accountPin, double amount) {
    if (password.equals(Integer.toString(accountPin))) {
	//calls model for the new balance
    	double balance = java.lang.Double.parseDouble(sBalance);
        balance = model.deposit(amount);
        if(FileWrite(balance))
        {
            sBalance = java.lang.Double.toString(balance);
			//updates the logfile
            LogWriter("Deposit", accountNumber, balance, amount);
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
	//log file function.
	public void LogWriter(String Operation, int actNum, double bal, double change){
		String LogString = "";
		//model gives a string that is the action and results 
			 LogString = model.log(Operation, actNum, bal, change);
				try {
					PrintWriter out = new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter("logfile.log", true)));
					String timeStamp = "Action Taken at: ";
					timeStamp += new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
					//this is the exact time the action was completed.
				    java.util.Date date= new java.util.Date();
				    out.println(timeStamp);
				    out.println(LogString);
				    out.close();
				}
				
				catch (IOException e) {

				System.out.println("handled");
			}	
	}
	//close account function
    public void closeAccount() {
    	try {
    	    
    	    File file = new File(filename);
			//logs action.
       	    LogWriter("Closed", accn, 0, 0);
			//deletes file
    	    file.delete();
 
    	} catch (Exception e) {
    	}
    }

}
	

