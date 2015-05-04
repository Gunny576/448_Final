//Control Module
//Handles all operations adjusting the balacnce of an account.
public class Model {
	public Model()
	{
		
	}
	double accountBalance = 0;
	
	//sets the balance
	public Model(double balance){
		
		accountBalance = balance;
		
	}
	
	//adds money to the balance.
	public double deposit (double cash){
		
		accountBalance += cash;
		return accountBalance;
		
	}
	
	//subtracts money from the balance.
	public double withdrawal (double cash){
		
		//makes sure the user doesn't withdrawal more than they have.
		if(accountBalance - cash >= 0){
			
			accountBalance -= cash;
			return accountBalance;
		}
		else {
		    return -1.0;
		}
		
	}
	public String log(String Operation, int accountNum, double balance, double change){
		String LogString = null;
		if(Operation == "Deposit"){
			LogString = ("Account: "+ accountNum + " has made a deposit of: " + change + " the new balance is: $" + balance);
		}
		if(Operation == "Withdrawal"){
			LogString = ("Account: "+ accountNum + " has made a withdrawal of: " + change + " the new balance is: $" + balance);
		}
		if(Operation == "Closed"){
			LogString = ("Account: "+ accountNum + " has closed their account.");
		}
		if(Operation == "Login"){
			LogString = ("Account: " + accountNum + " has logged in.");
		}
		return LogString;
	}
}
