import javax.swing.JFrame;

/**
    Digital Automated Teller Machine.
*/
public class DisplayDriver {
    private int state;
    private int prevState;
    private int accountNumber;
    private int newAccountNumber;
    private int accountPin;
    private Bank theBank;
    
    public static final int START = 1;
    public static final int ACCTFAIL = 2;
    public static final int PIN = 3;
    public static final int PINFAIL = 4;
    public static final int CREATEACCT = 5;
    public static final int CREATEPIN = 6;
    public static final int TRANSACT = 7;
    public static final int WITHDRAW = 8;
    public static final int DEPOSIT = 9;
    
    /**
        Constructor for the DisplayDriver class
        @param aBank the bank where all accounts reside
    */     
    public DisplayDriver(Bank aBank) {
        theBank = aBank;
        
        init();
    }
    
    /**
        Initialization method to keep the constructor clean
    */   
    private void init() {
        accountNumber = -1;
        newAccountNumber = -1;
        accountPin = -1;
        state = START;
        prevState = 0;
        
        JFrame frame = new ATMFrame(this);
        frame.setTitle("Group 8 Bank ATM");        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /** 
        Sets the current account number and sets state to PIN. 
        (Precondition: state is START or ACCTFAIL)
        @param number the account number.
    */
    public void setAccountNumber(int number) {
        assert state == START || state == ACCTFAIL;
        prevState = state;
        if (theBank.findAccount(number)) {
            accountNumber = number;
            state = PIN;
        }
        else
            state = ACCTFAIL;
    }
    
    /** 
        Gets the current account number. 
        (Precondition: state is TRANSACT)
        @return the balance
    */
    public int getAccountNumber() {
        assert state == TRANSACT;
        return accountNumber;
    }

    /** 
        Tries a PIN for the current account.
        If found sets state to TRANSACT, else to PINFAIL.
        (Precondition: state is PIN)
        @param pin the PIN to attempt
    */
    public void attemptPin(int pin) {
        assert state == PIN;
        prevState = state;
        if (theBank.tryPin(accountNumber, pin)) {
            accountPin = pin;
            state = TRANSACT;
        }
        else
            state = PINFAIL;
    }
    
    public void createAccount() {
    	assert state == START || state == ACCTFAIL;
    	prevState = state;
    	state = CREATEACCT;
    }
    
    /** 
	    Sets the account number for the new account
	    (Precondition: state is CREATEACCT)
	    @param accountNumber the number of the new account
	*/
    public boolean createAccountNumber(int aNumber) {
    	assert state == CREATEACCT;
    	if (theBank.findAccount(aNumber)) {
    		back();
    		return false;
    	}
    	else {
	    	newAccountNumber = aNumber;
	    	prevState = state;
	    	state = CREATEPIN;
	    	return true;
    	}
    }
    
    /** 
	    Sets the PIN for the new account.
	    (Precondition: state is CREATEPIN)
	    @param accountPin the new PIN
	*/
    public boolean createPin(int aPin) {
    	assert state == CREATEPIN;
    	if (aPin >= 1000 && aPin <= 9999) {
    		accountNumber = newAccountNumber;
    		accountPin = aPin;
    		theBank.createAccount(accountNumber, accountPin);
    		back();
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /** 
        Changes the state to WITHDRAW. 
        (Precondition: state is TRANSACT)
    */
    public void selectWithdraw() {
        assert state == TRANSACT;
        prevState = state;
        state = WITHDRAW;
    }
    
    /** 
        Withdraws amount from current account. 
        (Precondition: state is TRANSACT)
        @param value the amount to withdraw
    */
    public double withdraw(double value) {  
        assert state == TRANSACT;
        double result = theBank.withdraw(accountNumber, accountPin, value);
        back();
        if (result >= 0) {
        	return value;
        }
        else {
        	return -1.0;
        }
    }
    
    /** 
        Changes the state to DEPOSIT. 
        (Precondition: state is TRANSACT)
    */
    public void selectDeposit() {
        assert state == TRANSACT;
        prevState = state;
        state = DEPOSIT;
    }
    
    /** 
        Deposits amount to current account. 
        (Precondition: state is TRANSACT)
        @param value the amount to deposit
    */
    public double deposit(double value) {  
        assert state == TRANSACT;
        double result = theBank.deposit(accountNumber, accountPin, value);
        back();
        if (result >= 0) {
        	return value;
        }
        else {
        	return -1.0;
        }
    }

    /** 
        Gets the balance of the current account. 
        (Precondition: state is TRANSACT)
        @return the balance
    */
    public double getBalance() {  
        assert state == TRANSACT;
        return theBank.getBalance(accountNumber, accountPin);
    }

    /**
        Moves back to the initial state.
    */
    public void back() {
    	prevState = state;
    	if (state > TRANSACT) {
            state = TRANSACT;
        }
        else {
            state = START;
        }
    }

    /**
        Gets the current state of the ATM display.
        @return the current state
    */
    public int getState() {
        return state;
    }
    
    /**
	    Gets the previous state of the ATM display.
	    @return the previous state
	*/
	public int getPrevState() {
	    return prevState;
	}
}
